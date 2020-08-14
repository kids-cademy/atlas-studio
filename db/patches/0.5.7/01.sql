-- First update taxon table: add foreign key to taxon meta table.

ALTER TABLE `atlas`.`taxon` 
ADD COLUMN `meta_id` INT(11) NULL DEFAULT NULL AFTER `atlasobject_id`,
ADD INDEX `IX_TAXON_META_ID` (`meta_id` ASC);
