-- Add timestamp column to feature table. Its value is managed by JPA persistence logic via @PreUpdate hook. 
-- Since features_order column is managed transparently by JPA it is guaranteed not updated when features are changing their order. 
-- This column is used for translation logic to detect when translation is dirty and is critical to not generate false positives.

ALTER TABLE `atlas`.`feature` 
ADD COLUMN `timetsamp` DATETIME NOT NULL AFTER `features_order`;

-- Initialize default value somewhere in the past.

UPDATE `atlas`.`feature` SET `timestamp`='1970-01-02 12:00:00';