-- Add theme column to release table. Rely on MySQL RDBMS to initialize column with first enumeration value, that is, CLASSIC.

ALTER TABLE `atlas`.`release` 
ADD COLUMN `theme` ENUM('CLASSIC', 'MODERN') NOT NULL AFTER `edition`;
