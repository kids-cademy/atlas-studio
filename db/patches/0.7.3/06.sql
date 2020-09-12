-- Drop not longer used columns, and related indices, from taxon meta table.

ALTER TABLE `atlas`.`taxonmeta`
DROP `name`,
DROP COLUMN `display`,
DROP INDEX `UQ_TAXONOMYMETA_NAME`,
DROP INDEX `UQ_TAXONMETA_DISPLAY`;
