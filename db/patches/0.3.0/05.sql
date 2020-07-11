-- Remove deprecated columns, not longer used because of relation with linksource table

ALTER TABLE `atlas`.`atlasobject_links` 
DROP COLUMN `features`,
DROP COLUMN `display`,
DROP COLUMN `domain`;
