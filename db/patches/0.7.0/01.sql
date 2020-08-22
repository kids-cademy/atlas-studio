-- Add timestamnp column to atlas object images table.

ALTER TABLE `atlas`.`atlasobject_images` 
ADD COLUMN `timestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP AFTER `imageKey`;
