-- Add timestamp column to translation table. It is managed by database.

ALTER TABLE `atlas`.`translation` 
ADD COLUMN `timestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP AFTER `language`;
