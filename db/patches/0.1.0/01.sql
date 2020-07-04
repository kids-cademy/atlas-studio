ALTER TABLE `atlas`.`featuremeta` 
ADD COLUMN `display` VARCHAR(45) NOT NULL DEFAULT "" AFTER `quantity`;
