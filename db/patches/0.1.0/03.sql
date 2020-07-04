ALTER TABLE `atlas`.`linkmeta` 
CHANGE COLUMN `features` `features` TINYTEXT NOT NULL ;

ALTER TABLE `atlas`.`link` 
CHANGE COLUMN `features` `features` TINYTEXT NOT NULL ;
