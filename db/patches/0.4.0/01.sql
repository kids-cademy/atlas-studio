-- Change theme column from atlas collection table to enumeration. Rely on MySQL RDBMS to update existing lower case theme values to enumeration constants.

ALTER TABLE `atlas`.`atlascollection` 
CHANGE COLUMN `theme` `theme` ENUM('CLASSIC', 'MODERN') NOT NULL ;
