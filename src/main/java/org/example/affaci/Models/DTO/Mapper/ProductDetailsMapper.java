package org.example.affaci.Models.DTO.Mapper;


import org.example.affaci.Models.DTO.DetailProductResponseDTO;
import org.example.affaci.Models.Entity.*;
import org.example.affaci.Models.Enum.Language;
import org.example.affaci.Models.Enum.Mineral;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ProductDetailsMapper {


    public DetailProductResponseDTO toDto(Products p, String lng) {
        DetailProductResponseDTO dto = new DetailProductResponseDTO();
        dto.setId(p.getId());
//        dto.setName(p.getName());
        if ("en".equalsIgnoreCase(lng)) {
            if (p.getTranslates() != null) {
                p.getTranslates().stream()
                        .filter(t -> t.getLanguage() == Language.EN)
                        .findFirst()
                        .ifPresentOrElse(
                                t -> dto.setName(t.getProduct_name()),
                                () -> dto.setName(p.getName()) // fallback на оригинальное имя
                        );
            } else {
                dto.setName(p.getName());
            }
        } else {
            dto.setName(p.getName());
        }
        dto.setRegionName(p.getRegion().getName());
        dto.setCategoryName(p.getCategories().getName());


        dto.setChemicalComposition(
                p.getChemicalCompositions().stream()
                        .map(this::toChemicalDto)
                        .collect(Collectors.toList())
        );
        dto.setMineralComposition(
                p.getMineralCompositions().stream()
                        .map(this::toMineralDto)
                        .collect(Collectors.toList())
        );

        if (p.getVitaminCompositions() != null) {
            dto.setVitaminComposition(
                    p.getVitaminCompositions().stream()
                            .map(this::toVitaminDto)
                            .collect(Collectors.toList())
            );
        }

        if (p.getFattyAcidCompositions() != null) {
            dto.setFattyAcidComposition(
                    p.getFattyAcidCompositions().stream()
                            .filter(f -> !"Соотношения метиловых эфиров жирных кислот молочного жира".equals(f.getType_of_fatty_acid()))
                            .map(this::toFattyAcidDto)
                            .collect(Collectors.toList())
            );
            dto.setFattyAcidMethylEsterRatios(
                    p.getFattyAcidCompositions().stream()
                            .filter(f -> "Соотношения метиловых эфиров жирных кислот молочного жира".equals(f.getType_of_fatty_acid()))
                            .map(this::toFattyAcidMethylEsterRatioDto)
                            .collect(Collectors.toList())
            );
        }

        if (p.getAminoAcidCompositions() != null) {
            dto.setAminoAcidComposition(
                    p.getAminoAcidCompositions().stream()
                            .map(this::toAminoAcidDto)
                            .collect(Collectors.toList())
            );
        }

        return dto;
    }


    private DetailProductResponseDTO.ChemicalDTO toChemicalDto(Chemical_composition chem){
        DetailProductResponseDTO.ChemicalDTO dto = new DetailProductResponseDTO.ChemicalDTO();
        dto.setId(chem.getId());
        dto.setCompoundName(chem.getCompound_name());
        dto.setCompoundCategory(chem.getCompound_category());
        dto.setQuantity(chem.getQuantity());
        dto.setError(chem.getError());
        dto.setUnit(chem.getUnit());
        return dto;
    }

    private DetailProductResponseDTO.MineralDTO toMineralDto(Mineral_composition m){
        DetailProductResponseDTO.MineralDTO dto = new DetailProductResponseDTO.MineralDTO();
        dto.setId(m.getId());
        dto.setMineral(m.getMineral_name().getName());
        dto.setQuantity(m.getQuantity());
        dto.setError(m.getError());
        dto.setUnit(m.getUnit());
        return dto;
    }

    private DetailProductResponseDTO.VitaminDTO toVitaminDto(Vitamin_composition v){
        DetailProductResponseDTO.VitaminDTO dto = new DetailProductResponseDTO.VitaminDTO();
        dto.setId(v.getId());
        dto.setVitaminName(v.getVitamin_name());
        dto.setVitaminGroup(v.getVitamin_group());
        dto.setQuantity(v.getQuantity());
        dto.setError(v.getError());
        dto.setUnit(v.getUnit());
        return dto;
    }

    private DetailProductResponseDTO.FattyAcidDTO toFattyAcidDto(Fatty_acid_composition f){
        DetailProductResponseDTO.FattyAcidDTO dto = new DetailProductResponseDTO.FattyAcidDTO();
        dto.setId(f.getId());
        dto.setFattyAcidName(f.getFatty_acid_name());
        dto.setTypeOfFattyAcid(f.getType_of_fatty_acid());
        dto.setQuantity(f.getQuantity());
        dto.setError(f.getError());
        dto.setUnit(f.getUnit());
        return dto;
    }

    private DetailProductResponseDTO.AminoAcidDTO toAminoAcidDto(Amino_acid_composition a){
        DetailProductResponseDTO.AminoAcidDTO dto = new DetailProductResponseDTO.AminoAcidDTO();
        dto.setId(a.getId());
        dto.setAminoAcidName(a.getAmino_acid_name());
        dto.setQuantity(a.getQuantity());
        dto.setError(a.getError());
        dto.setUnit(a.getUnit());
        return dto;
    }

    private DetailProductResponseDTO.FattyAcidMethylEsterRatioDTO toFattyAcidMethylEsterRatioDto(Fatty_acid_composition f){
        DetailProductResponseDTO.FattyAcidMethylEsterRatioDTO dto = new DetailProductResponseDTO.FattyAcidMethylEsterRatioDTO();
        dto.setId(f.getId());
        dto.setRatioName(f.getFatty_acid_name());
        dto.setQuantity(f.getQuantity());
        dto.setError(f.getError());
        dto.setUnit(f.getUnit());
        return dto;
    }
}
