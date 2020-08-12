-- Add display column to collection taxonomy meta table. 

ALTER TABLE `atlas`.`atlascollection_taxonomymeta` 
ADD COLUMN `display` VARCHAR(45) NOT NULL AFTER `name`;
