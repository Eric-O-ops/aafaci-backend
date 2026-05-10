-- Переименование type_of_fatty_acid в таблице fatty_acid_composition
UPDATE fatty_acid_composition
SET type_of_fatty_acid = 'Соотношения метиловых эфиров жирных кислот молочного жира'
WHERE type_of_fatty_acid = 'Соотношения аминокислот';
