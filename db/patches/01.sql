-- Add electric charge to quantity enumeration.

ALTER TABLE `atlas`.`featuremeta` 
CHANGE COLUMN `quantity` `quantity` ENUM('NONE', 'SCALAR', 'MASS', 'TIME', 'LENGTH', 'SPEED', 'POWER', 'ELECTRC_CHARGE', 'FOOD_ENERGY', 'DENSITY', 'ACCELERATION', 'ANGLE', 'AREA', 'POPULATION_DENSITY', 'CURRENCY') NOT NULL ;
