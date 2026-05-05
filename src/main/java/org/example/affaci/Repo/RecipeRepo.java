package org.example.affaci.Repo;

import org.example.affaci.Models.Entity.Products;
import org.example.affaci.Models.Entity.Recipe;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RecipeRepo extends JpaRepository<Recipe, UUID> {
    List<Recipe> findByDishOrderByDisplayOrderAsc(Products dish);
    void deleteByDish(Products dish);
}
