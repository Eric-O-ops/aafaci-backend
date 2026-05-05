package org.example.affaci.Service;

import lombok.RequiredArgsConstructor;
import org.example.affaci.Models.DTO.RecipeDTO;
import org.example.affaci.Models.Entity.Recipe;
import org.example.affaci.Models.Entity.Products;
import org.example.affaci.Repo.RecipeRepo;
import org.example.affaci.Repo.ProductsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepo dishIngredientRepository;
    private final ProductsRepository productsRepository;

    // Получить все ингредиенты блюда
    public List<RecipeDTO> getIngredientsByDishId(UUID dishId) {
        Products dish = productsRepository.findById(dishId)
                .orElseThrow(() -> new RuntimeException("Блюдо не найдено"));
        return dishIngredientRepository.findByDishOrderByDisplayOrderAsc(dish)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Добавить новый ингредиент
    public RecipeDTO addIngredient(UUID dishId, RecipeDTO dto) {
        Products dish = productsRepository.findById(dishId)
                .orElseThrow(() -> new RuntimeException("Блюдо не найдено"));
        Recipe ingredient = new Recipe();
        ingredient.setDish(dish);
        ingredient.setIngredientName(dto.getIngredientName());
        ingredient.setQuantity(dto.getQuantity());
        ingredient.setUnit(dto.getUnit());
        ingredient.setDisplayOrder(dto.getDisplayOrder());
        return toDTO(dishIngredientRepository.save(ingredient));
    }

    // Обновить существующий ингредиент
    public RecipeDTO updateIngredient(UUID ingredientId, RecipeDTO dto) {
        Recipe ingredient = dishIngredientRepository.findById(ingredientId)
                .orElseThrow(() -> new RuntimeException("Ингредиент не найден"));
        ingredient.setIngredientName(dto.getIngredientName());
        ingredient.setQuantity(dto.getQuantity());
        ingredient.setUnit(dto.getUnit());
        ingredient.setDisplayOrder(dto.getDisplayOrder());
        return toDTO(dishIngredientRepository.save(ingredient));
    }

    // Удалить ингредиент
    @Transactional
    public void deleteIngredient(UUID ingredientId) {
        dishIngredientRepository.deleteById(ingredientId);
    }

    // Удалить все ингредиенты блюда (при удалении самого блюда)
    @Transactional
    public void deleteAllIngredientsForDish(UUID dishId) {
        Products dish = productsRepository.findById(dishId)
                .orElseThrow(() -> new RuntimeException("Блюдо не найдено"));
        dishIngredientRepository.deleteByDish(dish);
    }

    private RecipeDTO toDTO(Recipe entity) {
        RecipeDTO dto = new RecipeDTO();
        dto.setId(entity.getId());
        dto.setDishId(entity.getDish().getId());
        dto.setIngredientName(entity.getIngredientName());
        dto.setQuantity(entity.getQuantity());
        dto.setUnit(entity.getUnit());
        dto.setDisplayOrder(entity.getDisplayOrder());
        return dto;
    }
}
