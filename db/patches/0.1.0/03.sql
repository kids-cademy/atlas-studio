ALTER TABLE `atlas`.`linkmeta` 
CHANGE COLUMN `features` `features` TINYTEXT NOT NULL ;

ALTER TABLE `atlas`.`atlasobject_links` 
CHANGE COLUMN `features` `features` TINYTEXT NOT NULL ;
