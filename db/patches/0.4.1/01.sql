-- Add features type column to atlas collection. See AtlasCollection#featuresType for details.

ALTER TABLE `atlas`.`atlascollection` 
ADD COLUMN `featuresType` VARCHAR(45) NOT NULL AFTER `definition`;
