-- Add theme column to atlas collection table. A theme has minor estetical customizations on atlas reader. 

ALTER TABLE `atlas`.`atlascollection` 
ADD COLUMN `theme` VARCHAR(45) NOT NULL AFTER `definition`;