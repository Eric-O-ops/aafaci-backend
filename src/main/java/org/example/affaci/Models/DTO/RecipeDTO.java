package org.example.affaci.Models.DTO;

import lombok.Data;
import org.example.affaci.Models.Enum.Unit;
import java.util.UUID;

@Data
public class RecipeDTO {
    private UUID id;
    private UUID dishId;
    private String dishName;
    private String ingredientName;
    private Double quantity;
    private Unit unit;
    private Integer displayOrder;
}
