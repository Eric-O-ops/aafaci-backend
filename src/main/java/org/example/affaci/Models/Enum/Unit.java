package org.example.affaci.Models.Enum;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Unit {
    g("г"),
    mg("мг"),
    µg("мкг"),
    IU("МЕ"),
    kcal("ккал"),
    kJ("кДж"),
    PERCENT("%"),
    GRAM("г"),
    MILLILITER("мл"),
    KILOGRAM("кг"),
    LITER("л"),
    PIECE("шт"),
    TEASPOON("ч.л"),
    TABLESPOON("ст.л");

    private final String name;

    Unit(String name) {
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }
}
