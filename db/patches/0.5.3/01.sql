-- Enlarge APIs column size from link source to accomodate external source size.

ALTER TABLE `atlas`.`linksource` 
CHANGE COLUMN `apis` `apis` TINYTEXT NOT NULL ;
