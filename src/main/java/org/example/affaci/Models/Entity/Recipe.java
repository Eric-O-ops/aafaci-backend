package org.example.affaci.Models.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.affaci.Models.Enum.Unit;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "recipes")
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class Recipe {
    @Id
    @UuidGenerator
    UUID id;

    // Блюдо (национальное блюдо) – к какому продукту относятся ингредиенты
    @ManyToOne
    @JoinColumn(name = "recipe_id", nullable = false)
    Products dish;

    // Название ингредиента (просто текст, без связи с другой таблицей)
    @Column(nullable = false)
    String ingredientName;

    // Количество
    @Column(nullable = false)
    Double quantity;

    // Единица измерения (используем существующий Enum Unit)
    @Enumerated(EnumType.STRING)
    @Column(name = "unit", columnDefinition = "VARCHAR(255)")
    Unit unit;

    // Необязательное поле для порядка сортировки (как на фронтенде)
    Integer displayOrder;
}
