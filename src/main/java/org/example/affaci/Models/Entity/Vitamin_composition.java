package org.example.affaci.Models.Entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.affaci.Models.Enum.Unit;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class Vitamin_composition {

    @Id
    @UuidGenerator
    UUID id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    Products product;

    String vitamin_name;
    String vitamin_group;
    Double quantity;
    Double error;
    @Enumerated(EnumType.STRING)
    @Column(name = "unit", columnDefinition = "VARCHAR(255)")
    Unit unit;
}
