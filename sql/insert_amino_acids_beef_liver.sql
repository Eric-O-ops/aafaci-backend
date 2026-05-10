-- Аминокислотный состав: Печень говяжья (Чуйская область)
-- Все значения в г/100г, unit = 'g'

DO $$
DECLARE
    v_product_id UUID;
BEGIN
    SELECT p.id INTO v_product_id FROM products p
    JOIN categories c ON c.id = p.categories_id
    WHERE p.name = 'Печень говяжья'
      AND p.regions_id = (SELECT id FROM regions WHERE name = 'Чуйская область')
      AND c.name = 'Субпродукты'
      AND c.regions_id = p.regions_id
    LIMIT 1;

    IF v_product_id IS NULL THEN
        RAISE NOTICE 'Продукт "Печень говяжья" не найден для Чуйской области';
        RETURN;
    END IF;

    -- 1. Аспарагиновая кислота (нет данных)
    INSERT INTO amino_acid_composition (id, product_id, amino_acid_name, quantity, error, unit)
    VALUES (gen_random_uuid(), v_product_id, 'Аспарагиновая кислота', NULL, NULL, 'g');

    -- 2. Глутаминовая кислота
    INSERT INTO amino_acid_composition (id, product_id, amino_acid_name, quantity, error, unit)
    VALUES (gen_random_uuid(), v_product_id, 'Глутаминовая кислота', 3588.24, 179.412, 'g');

    -- 3. Серин
    INSERT INTO amino_acid_composition (id, product_id, amino_acid_name, quantity, error, unit)
    VALUES (gen_random_uuid(), v_product_id, 'Серин', 697.927, 34.896, 'g');

    -- 4. Гистидин
    INSERT INTO amino_acid_composition (id, product_id, amino_acid_name, quantity, error, unit)
    VALUES (gen_random_uuid(), v_product_id, 'Гистидин', 551.207, 27.560, 'g');

    -- 5. Глицин
    INSERT INTO amino_acid_composition (id, product_id, amino_acid_name, quantity, error, unit)
    VALUES (gen_random_uuid(), v_product_id, 'Глицин', 1431.593, 71.579, 'g');

    -- 6. Треонин
    INSERT INTO amino_acid_composition (id, product_id, amino_acid_name, quantity, error, unit)
    VALUES (gen_random_uuid(), v_product_id, 'Треонин', 1029.449, 51.472, 'g');

    -- 7. Аргинин
    INSERT INTO amino_acid_composition (id, product_id, amino_acid_name, quantity, error, unit)
    VALUES (gen_random_uuid(), v_product_id, 'Аргинин', 1500.534, 75.027, 'g');

    -- 8. Аланин
    INSERT INTO amino_acid_composition (id, product_id, amino_acid_name, quantity, error, unit)
    VALUES (gen_random_uuid(), v_product_id, 'Аланин', 1247.758, 62.388, 'g');

    -- 9. Тирозин
    INSERT INTO amino_acid_composition (id, product_id, amino_acid_name, quantity, error, unit)
    VALUES (gen_random_uuid(), v_product_id, 'Тирозин', 1080.138, 54.007, 'g');

    -- 10. Цистин
    INSERT INTO amino_acid_composition (id, product_id, amino_acid_name, quantity, error, unit)
    VALUES (gen_random_uuid(), v_product_id, 'Цистин', 272.303, 16.615, 'g');

    -- 11. Валин
    INSERT INTO amino_acid_composition (id, product_id, amino_acid_name, quantity, error, unit)
    VALUES (gen_random_uuid(), v_product_id, 'Валин', 1501.738, 75.086, 'g');

    -- 12. Метионин
    INSERT INTO amino_acid_composition (id, product_id, amino_acid_name, quantity, error, unit)
    VALUES (gen_random_uuid(), v_product_id, 'Метионин', 583.001, 29.150, 'g');

    -- 13. Триптофан
    INSERT INTO amino_acid_composition (id, product_id, amino_acid_name, quantity, error, unit)
    VALUES (gen_random_uuid(), v_product_id, 'Триптофан', 228.00, 11.40, 'g');

    -- 14. Фенилаланин
    INSERT INTO amino_acid_composition (id, product_id, amino_acid_name, quantity, error, unit)
    VALUES (gen_random_uuid(), v_product_id, 'Фенилаланин', 1318.217, 65.911, 'g');

    -- 15. Изолейцин (нет данных)
    INSERT INTO amino_acid_composition (id, product_id, amino_acid_name, quantity, error, unit)
    VALUES (gen_random_uuid(), v_product_id, 'Изолейцин', NULL, NULL, 'g');

    -- 16. Лейцин
    INSERT INTO amino_acid_composition (id, product_id, amino_acid_name, quantity, error, unit)
    VALUES (gen_random_uuid(), v_product_id, 'Лейцин', 1222.827, 61.141, 'g');

    -- 17. Лизин
    INSERT INTO amino_acid_composition (id, product_id, amino_acid_name, quantity, error, unit)
    VALUES (gen_random_uuid(), v_product_id, 'Лизин', 2047.458, 102.372, 'g');

    -- 18. Пролин
    INSERT INTO amino_acid_composition (id, product_id, amino_acid_name, quantity, error, unit)
    VALUES (gen_random_uuid(), v_product_id, 'Пролин', 1046.888, 52.344, 'g');

    -- 19. Аспарагин
    INSERT INTO amino_acid_composition (id, product_id, amino_acid_name, quantity, error, unit)
    VALUES (gen_random_uuid(), v_product_id, 'Аспарагин', 932.915, 46.646, 'g');

    -- 20. Глутамин
    INSERT INTO amino_acid_composition (id, product_id, amino_acid_name, quantity, error, unit)
    VALUES (gen_random_uuid(), v_product_id, 'Глутамин', 555.528, 27.776, 'g');

    RAISE NOTICE 'Аминокислотный состав добавлен для продукта "Печень говяжья" (ID: %)', v_product_id;
END $$;
