package org.example.affaci.Controller;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.example.affaci.Models.DTO.RecipeDTO;
import org.example.affaci.Models.Entity.Products;
import org.example.affaci.Repo.ProductsRepository;
import org.example.affaci.Service.RecipeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recipe")
public class RecipeController {
    private final RecipeService dishIngredientService;
    private final ProductsRepository productsRepository;

    @GetMapping("/debug/dishes")
    public ResponseEntity<?> debugDishes() {
        return ResponseEntity.ok(productsRepository.findAllByNationalIsTrue()
                .stream().map(p -> p.getName()).toList());
    }

    // Получить все ингредиенты для блюда
    @GetMapping("/{recipeId}")
    public ResponseEntity<RecipeResponse> getIngredients(@PathVariable UUID recipeId) {
        Products dish = productsRepository.findById(recipeId).orElse(null);
        if (dish == null) {
            return ResponseEntity.ok(new RecipeResponse("Продукт не найден", List.of()));
        }
        List<RecipeDTO> ingredients = dishIngredientService.getIngredientsByDishId(recipeId);
        return ResponseEntity.ok(new RecipeResponse(dish.getName(), ingredients));
    }

    // Добавить ингредиент
    @PostMapping("/{recipeId}")
    public ResponseEntity<RecipeDTO> addIngredient(@PathVariable UUID recipeId,
                                                   @RequestBody RecipeDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(dishIngredientService.addIngredient(recipeId, dto));
    }

    // Обновить ингредиент
    @PutMapping("/{ingredientId}")
    public ResponseEntity<RecipeDTO> updateIngredient(@PathVariable UUID ingredientId,
                                                      @RequestBody RecipeDTO dto) {
        return ResponseEntity.ok(dishIngredientService.updateIngredient(ingredientId, dto));
    }

    // Удалить ингредиент
    @DeleteMapping("/{ingredientId}")
    public ResponseEntity<Void> deleteIngredient(@PathVariable UUID ingredientId) {
        dishIngredientService.deleteIngredient(ingredientId);
        return ResponseEntity.noContent().build();
    }
}

@Data
class RecipeResponse {
    private String dishName;
    private List<RecipeDTO> ingredients;

    RecipeResponse(String dishName, List<RecipeDTO> ingredients) {
        this.dishName = dishName;
        this.ingredients = ingredients;
    }
}
