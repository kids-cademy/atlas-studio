-- Remove deprecated languages column from atlas collection table. Translation is managed exclusively by releases.

ALTER TABLE `atlas`.`atlascollection` 
DROP COLUMN `languages`;