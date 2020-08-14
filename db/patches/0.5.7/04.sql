-- Add taxon foreign key constrains: taxon record is removed when parent meta records is removed.

ALTER TABLE `atlas`.`taxon` 
ADD CONSTRAINT `FK_TAXON_META_ID`
  FOREIGN KEY (`meta_id`)
  REFERENCES `atlas`.`taxonmeta` (`id`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;
