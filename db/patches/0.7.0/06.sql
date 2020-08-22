-- Add audio sample timestamp to atlas object table. It is optional and is maintained by persistence logic.

ALTER TABLE `atlas`.`atlasobject` 
ADD COLUMN `sampleTimestamp` TIMESTAMP NOT NULL DEFAULT '1970-01-02 12:00:00' AFTER `description`;
