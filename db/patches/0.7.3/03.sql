-- Change taxon meta table definition to use taxon unit for name and display.
-- Add foreign key to taxon unit table but not create constrains yet.

ALTER TABLE `atlas`.`taxonmeta` 
ADD COLUMN `unit_id` INT(11) NULL DEFAULT NULL AFTER `taxonomymeta_order`;
