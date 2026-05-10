package org.example.affaci.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.affaci.Models.Entity.*;
import org.example.affaci.Models.Enum.Mineral;
import org.example.affaci.Models.Enum.Unit;
import org.example.affaci.Repo.CategoriesRepository;
import org.example.affaci.Repo.ProductsRepository;
import org.example.affaci.Repo.RegionsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CsvImportService {

    private final ProductsRepository productsRepository;
    private final RegionsRepository regionsRepository;
    private final CategoriesRepository categoriesRepository;

    @Transactional
    public void importFromCsvFile(Path filePath) throws IOException {
        List<String> lines = Files.readAllLines(filePath);
        if (lines.isEmpty()) {
            log.warn("Файл пуст");
            return;
        }

        String[] headers = lines.get(0).split(";");
        log.info("Заголовки: {}", Arrays.toString(headers));

        Map<String, Products> productCache = new HashMap<>();

        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.trim().isEmpty()) continue;

            String[] parts = parseCsvLine(line);
            if (parts.length < 8) {
                log.warn("Строка {} имеет недостаточно колонок: {}", i, line);
                continue;
            }

            try {
                String regionName = parts[0].trim();
                String categoryName = parts[1].trim();
                String productName = parts[2].trim();
                String indicatorType = parts[3].trim();
                String component = parts[4].trim();
                String valueStr = parts[5].trim();
                String errorStr = parts[6].trim();
                String unitStr = parts[7].trim();

                String productKey = regionName + "|" + categoryName + "|" + productName;
                Products product = productCache.get(productKey);

                if (product == null) {
                    product = createOrGetProduct(regionName, categoryName, productName);
                    productCache.put(productKey, product);
                }

                Double value = parseDouble(valueStr);
                Double error = errorStr.isEmpty() ? null : parseDouble(errorStr);

                processIndicator(product, indicatorType, component, value, error, unitStr);

            } catch (Exception e) {
                log.error("Ошибка при обработке строки {}: {}", i, e.getMessage());
            }
        }

        for (Products product : productCache.values()) {
            productsRepository.save(product);
        }

        log.info("Импорт завершен. Обработано продуктов: {}", productCache.size());
    }

    private String[] parseCsvLine(String line) {
        List<String> result = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (char c : line.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ';' && !inQuotes) {
                result.add(current.toString());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }
        result.add(current.toString());

        return result.toArray(new String[0]);
    }

    private Products createOrGetProduct(String regionName, String categoryName, String productName) {
        Regions region = regionsRepository.findByNameIgnoreCase(regionName).orElseGet(() -> {
            Regions newRegion = new Regions();
            newRegion.setName(regionName);
            newRegion.setLevel(1);
            return regionsRepository.save(newRegion);
        });

        Categories category = categoriesRepository.findByNameAndRegion(categoryName, region).orElseGet(() -> {
            Categories newCategory = new Categories();
            newCategory.setName(categoryName);
            newCategory.setRegion(region);
            return categoriesRepository.save(newCategory);
        });

        Products product = new Products();
        product.setName(productName);
        product.setRegion(region);
        product.setCategories(category);
        product.setNational(false);
        return product;
    }

    private void processIndicator(Products product, String indicatorType, String component,
                                   Double value, Double error, String unitStr) {
        Unit unit = parseUnit(unitStr);

        switch (indicatorType) {
            case "Хим. состав" -> handleChemicalComposition(product, component, value, error, unit, unitStr);
            case "Минералы" -> handleMineralComposition(product, component, value, error, unit);
            case "Витамины" -> handleVitaminComposition(product, component, value, error, unit);
            case "Насыщенные ЖК" -> handleFattyAcidComposition(product, component, value, error, unit, "Насыщенные ЖК");
            case "Мононенасыщенные ЖК" -> handleFattyAcidComposition(product, component, value, error, unit, "Мононенасыщенные ЖК");
            case "Полиненасыщенные ЖК" -> handleFattyAcidComposition(product, component, value, error, unit, "Полиненасыщенные ЖК");
            case "Аминокислотный состав" -> handleAminoAcidComposition(product, component, value, error, unit);
            case "Соотношения метиловых эфиров жирных кислот молочного жира" -> handleFattyAcidComposition(product, component, value, error, unit, "Соотношения метиловых эфиров жирных кислот молочного жира");
            default -> log.warn("Неизвестный тип показателя: {}", indicatorType);
        }
    }

    private void handleChemicalComposition(Products product, String component, Double value, Double error, Unit unit, String unitStr) {
        if ("Энерг. ценность".equals(component) && unitStr.contains("/")) {
            Chemical_composition chemKcal = new Chemical_composition();
            chemKcal.setProduct(product);
            chemKcal.setCompound_name("Энерг. ценность");
            chemKcal.setCompound_category("Хим. состав");
            chemKcal.setQuantity(value);
            chemKcal.setError(error);
            chemKcal.setUnit(Unit.kcal);
            product.getChemicalCompositions().add(chemKcal);

            String kjPart = unitStr.split("/")[1].trim();
            Double kjValue = parseDouble(kjPart.replace("кДж", "").trim());
            if (kjValue != null) {
                Chemical_composition chemKj = new Chemical_composition();
                chemKj.setProduct(product);
                chemKj.setCompound_name("Энерг. ценность");
                chemKj.setCompound_category("Хим. состав");
                chemKj.setQuantity(kjValue);
                chemKj.setError(error);
                chemKj.setUnit(Unit.kJ);
                product.getChemicalCompositions().add(chemKj);
            }
        } else {
            Chemical_composition chem = new Chemical_composition();
            chem.setProduct(product);
            chem.setCompound_name(component);
            chem.setCompound_category("Хим. состав");
            chem.setQuantity(value);
            chem.setError(error);
            chem.setUnit(unit);
            product.getChemicalCompositions().add(chem);
        }
    }

    private void handleMineralComposition(Products product, String component, Double value, Double error, Unit unit) {
        Mineral mineral = mapMineralName(component);
        if (mineral == null) {
            log.warn("Не удалось сопоставить минерал: {}", component);
            return;
        }

        Mineral_composition mineralComp = new Mineral_composition();
        mineralComp.setProduct(product);
        mineralComp.setMineral_name(mineral);
        mineralComp.setQuantity(value);
        mineralComp.setError(error);
        mineralComp.setUnit(unit);
        product.getMineralCompositions().add(mineralComp);
    }

    private void handleVitaminComposition(Products product, String component, Double value, Double error, Unit unit) {
        Vitamin_composition vitamin = new Vitamin_composition();
        vitamin.setProduct(product);
        vitamin.setVitamin_name(component);
        vitamin.setVitamin_group("Витамины");
        vitamin.setQuantity(value);
        vitamin.setError(error);
        vitamin.setUnit(unit);
        product.getVitaminCompositions().add(vitamin);
    }

    private void handleFattyAcidComposition(Products product, String component, Double value, Double error, Unit unit, String type) {
        Fatty_acid_composition fattyAcid = new Fatty_acid_composition();
        fattyAcid.setProduct(product);
        fattyAcid.setFatty_acid_name(component);
        fattyAcid.setType_of_fatty_acid(type);
        fattyAcid.setQuantity(value);
        fattyAcid.setError(error);
        fattyAcid.setUnit(unit);
        product.getFattyAcidCompositions().add(fattyAcid);
    }

    private void handleAminoAcidComposition(Products product, String component, Double value, Double error, Unit unit) {
        Amino_acid_composition aminoAcid = new Amino_acid_composition();
        aminoAcid.setProduct(product);
        aminoAcid.setAmino_acid_name(component);
        aminoAcid.setQuantity(value);
        aminoAcid.setError(error);
        aminoAcid.setUnit(unit);
        product.getAminoAcidCompositions().add(aminoAcid);
    }

    private Mineral mapMineralName(String name) {
        return switch (name) {
            case "Кальций" -> Mineral.Ca;
            case "Железо" -> Mineral.Fe;
            case "Фосфор" -> Mineral.P;
            case "Калий" -> Mineral.K;
            case "Натрий" -> Mineral.Na;
            default -> null;
        };
    }

    private Unit parseUnit(String unitStr) {
        if (unitStr == null || unitStr.isEmpty()) return Unit.g;
        return switch (unitStr.trim()) {
            case "г" -> Unit.g;
            case "мг" -> Unit.mg;
            case "мкг" -> Unit.µg;
            case "%" -> Unit.PERCENT;
            case "ккал" -> Unit.kcal;
            case "кДж" -> Unit.kJ;
            default -> Unit.g;
        };
    }

    private Double parseDouble(String str) {
        if (str == null || str.isEmpty()) return null;
        try {
            return Double.parseDouble(str.replace(",", ".").trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}