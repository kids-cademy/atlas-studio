-- Migrate atlas object aliases from separated many-to-one table to single column of comma separated strings.
-- Create aliases column on atlas object table. 

ALTER TABLE `atlas`.`atlasobject` 
ADD COLUMN `aliases` TINYTEXT NOT NULL AFTER `display`;