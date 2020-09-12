-- Rename taxon meta foreign key to atlas collection table by droping and re-creating it.

ALTER TABLE `atlas`.`taxonmeta` 
DROP FOREIGN KEY `FK_TAXONOMYMETA_COLLECTION_ID`;

ALTER TABLE `atlas`.`taxonmeta` 
ADD CONSTRAINT `FK_TAXONMETA_COLLECTION_ID`
  FOREIGN KEY (`atlascollection_id`)
  REFERENCES `atlas`.`atlascollection` (`id`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;

