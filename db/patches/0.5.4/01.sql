-- Fix ELECTRIC_CHARGE enumeration constant from feature meta quantity in three steps:

-- First step relax enumeration constraints.
ALTER TABLE `atlas`.`featuremeta` 
CHANGE COLUMN `quantity` `quantity` VARCHAR(45) NOT NULL ;

-- Second step is to corrent enumeration constant.
UPDATE `atlas`.`featuremeta` SET `quantity`='ELECTRIC_CHARGE' WHERE `quantity`='ELECTRC_CHARGE';

-- Last step is to restore corrected enumeration to qunatity column.
ALTER TABLE `atlas`.`featuremeta` 
CHANGE COLUMN `quantity` `quantity` ENUM('NONE', 'SCALAR', 'MASS', 'TIME', 'LENGTH', 'SPEED', 'POWER', 'ELECTRIC_CHARGE', 'FOOD_ENERGY', 'DENSITY', 'ACCELERATION', 'ANGLE', 'AREA', 'POPULATION_DENSITY', 'CURRENCY') NOT NULL ;
