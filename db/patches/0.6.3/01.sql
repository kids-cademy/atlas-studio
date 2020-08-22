-- Add languages column to android application table. App languages are a subset of release languages.

ALTER TABLE `atlas`.`androidapp` 
ADD COLUMN `languages` VARCHAR(45) NOT NULL AFTER `versionCode`;
