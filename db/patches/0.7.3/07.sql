-- Rename taxon meta index for foreign key to atlas collection.

ALTER TABLE `atlas`.`taxonmeta` 
DROP INDEX `IX_TAXONOMYMETA_COLLECTION_ID` ,
ADD INDEX `IX_TAXONMETA_COLLECTION_ID` (`atlascollection_id` ASC);
