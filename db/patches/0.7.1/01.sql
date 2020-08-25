-- Synchronize database schema with EER model from MySQL Workbench.

ALTER TABLE `atlas`.`androidapp` 
CHANGE COLUMN `buildTimestamp` `buildTimestamp` TIMESTAMP NOT NULL DEFAULT '1970-01-02 12:00:00' ;

ALTER TABLE `atlas`.`atlasobject` 
CHANGE COLUMN `sampleTimestamp` `sampleTimestamp` TIMESTAMP NULL DEFAULT NULL ;
