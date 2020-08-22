-- Add build timestamp column to android appliation table. It is maintained by business logic since database takes care of application dirty timestamp.

ALTER TABLE `atlas`.`androidapp` 
ADD COLUMN `buildTimestamp` TIMESTAMP NOT NULL DEFAULT '1970-01-02 12:00:00' AFTER `timestamp`;
