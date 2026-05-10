-- Аминокислотный состав: Субпродукты птицы: печень куриная (Иссык-Кульская область)
-- Все значения в г/100г, unit = 'g'

DO $$
DECLARE
    v_product_id UUID;
BEGIN
    SELECT p.id INTO v_product_id FROM products p
    JOIN categories c ON c.id = p.categories_id
    WHERE p.name = 'Субпродукты птицы: печень куриная'
      AND p.regions_id = (SELECT id FROM regions WHERE name = 'Иссык-Кульская область')
      AND c.name = 'Субпродукты'
      AND c.regions_id = p.regions_id
    LIMIT 1;

    IF v_product_id IS NULL THEN
        RAISE NOTICE 'Продукт "Субпродукты птицы: печень куриная" не найден для Иссык-Кульской области';
        RETURN;
    END IF;

    -- 1. Аспарагиновая кислота
    INSERT INTO amino_acid_composition (id, product_id, amino_acid_name, quantity, error, unit)
    VALUES (gen_random_uuid(), v_product_id, 'Аспарагиновая кислота', 1870.412, 93.521, 'g');

    -- 2. Глутаминовая кислота
    INSERT INTO amino_acid_composition (id, product_id, amino_acid_name, quantity, error, unit)
    VALUES (gen_random_uuid(), v_product_id, 'Глутаминовая кислота', 2783.185, 139.009, 'g');

    -- 3. Серин
    INSERT INTO amino_acid_composition (id, product_id, amino_acid_name, quantity, error, unit)
    VALUES (gen_random_uuid(), v_product_id, 'Серин', 491.923, 24.546, 'g');

    -- 4. Гистидин
    INSERT INTO amino_acid_composition (id, product_id, amino_acid_name, quantity, error, unit)
    VALUES (gen_random_uuid(), v_product_id, 'Гистидин', 424.557, 21.028, 'g');

    -- 5. Глицин (нет данных)
    INSERT INTO amino_acid_composition (id, product_id, amino_acid_name, quantity, error, unit)
    VALUES (gen_random_uuid(), v_product_id, 'Глицин', NULL, NULL, 'g');

    -- 6. Треонин
    INSERT INTO amino_acid_composition (id, product_id, amino_acid_name, quantity, error, unit)
    VALUES (gen_random_uuid(), v_product_id, 'Треонин', 723.648, 36.032, 'g');

    -- 7. Аргинин
    INSERT INTO amino_acid_composition (id, product_id, amino_acid_name, quantity, error, unit)
    VALUES (gen_random_uuid(), v_product_id, 'Аргинин', 1018.719, 50.536, 'g');

    -- 8. Аланин
    INSERT INTO amino_acid_composition (id, product_id, amino_acid_name, quantity, error, unit)
    VALUES (gen_random_uuid(), v_product_id, 'Аланин', 1455.236, 72.512, 'g');

    -- 9. Тирозин
    INSERT INTO amino_acid_composition (id, product_id, amino_acid_name, quantity, error, unit)
    VALUES (gen_random_uuid(), v_product_id, 'Тирозин', 67.841, 33.542, 'g');

    -- 10. Цистин
    INSERT INTO amino_acid_composition (id, product_id, amino_acid_name, quantity, error, unit)
    VALUES (gen_random_uuid(), v_product_id, 'Цистин', 231.112, 11.505, 'g');

    -- 11. Валин
    INSERT INTO amino_acid_composition (id, product_id, amino_acid_name, quantity, error, unit)
    VALUES (gen_random_uuid(), v_product_id, 'Валин', 1266.573, 63.029, 'g');

    -- 12. Метионин
    INSERT INTO amino_acid_composition (id, product_id, amino_acid_name, quantity, error, unit)
    VALUES (gen_random_uuid(), v_product_id, 'Метионин', 424.295, 21.015, 'g');

    -- 13. Триптофан
    INSERT INTO amino_acid_composition (id, product_id, amino_acid_name, quantity, error, unit)
    VALUES (gen_random_uuid(), v_product_id, 'Триптофан', 408.064, 20.003, 'g');

    -- 14. Фенилаланин
    INSERT INTO amino_acid_composition (id, product_id, amino_acid_name, quantity, error, unit)
    VALUES (gen_random_uuid(), v_product_id, 'Фенилаланин', 985.482, 49.024, 'g');

    -- 15. Изолейцин
    INSERT INTO amino_acid_composition (id, product_id, amino_acid_name, quantity, error, unit)
    VALUES (gen_random_uuid(), v_product_id, 'Изолейцин', 941.317, 47.016, 'g');

    -- 16. Лейцин
    INSERT INTO amino_acid_composition (id, product_id, amino_acid_name, quantity, error, unit)
    VALUES (gen_random_uuid(), v_product_id, 'Лейцин', 1933.759, 96.538, 'g');

    -- 17. Лизин
    INSERT INTO amino_acid_composition (id, product_id, amino_acid_name, quantity, error, unit)
    VALUES (gen_random_uuid(), v_product_id, 'Лизин', 1072.821, 53.541, 'g');

    -- 18. Пролин
    INSERT INTO amino_acid_composition (id, product_id, amino_acid_name, quantity, error, unit)
    VALUES (gen_random_uuid(), v_product_id, 'Пролин', 1011.594, 50.530, 'g');

    RAISE NOTICE 'Аминокислотный состав добавлен для продукта "Субпродукты птицы: печень куриная" (ID: %)', v_product_id;
END $$;
