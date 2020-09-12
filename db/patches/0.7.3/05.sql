-- Add index and constraint to taxon meta foreign key to taxon unit primary key.

ALTER TABLE `atlas`.`taxonmeta` 
ADD INDEX `IX_TAXONMETA_UNIT_ID` (`unit_id` ASC);

ALTER TABLE `atlas`.`taxonmeta` 
ADD CONSTRAINT `FK_TAXONMETA_UNIT_ID`
  FOREIGN KEY (`unit_id`)
  REFERENCES `atlas`.`taxonunit` (`id`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;