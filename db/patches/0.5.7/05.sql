-- Remove no longer used name column from taxon table. After this update taxon name is retrieved from taxonmeta.

ALTER TABLE `atlas`.`taxon` 
DROP COLUMN `name`,
DROP INDEX `UQ_TAXON_NAME`;