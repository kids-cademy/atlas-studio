-- Convert collection taxonomy meta from embeddable to entity.

ALTER TABLE `atlas`.`atlascollection_taxonomymeta` 
DROP FOREIGN KEY `fk_taxon_meta_atlascollection1`;

ALTER TABLE `atlas`.`atlascollection_taxonomymeta` 
CHANGE COLUMN `atlascollection_id` `atlascollection_id` INT(11) NULL DEFAULT NULL ,
CHANGE COLUMN `taxonomymeta_order` `taxonomymeta_order` INT(11) NULL DEFAULT NULL ,
DROP INDEX `FK_TAXONOMYMETA_COLLECTION_ID` ,
ADD INDEX `IX_TAXONOMYMETA_COLLECTION_ID` (`atlascollection_id` ASC),
ADD UNIQUE INDEX `UQ_TAXONMETA_DISPLAY` (`atlascollection_id` ASC, `display` ASC);

ALTER TABLE `atlas`.`atlascollection_taxonomymeta` 
ADD CONSTRAINT `FK_TAXONOMYMETA_COLLECTION_ID`
  FOREIGN KEY (`atlascollection_id`)
  REFERENCES `atlas`.`atlascollection` (`id`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;

ALTER TABLE `atlas`.`atlascollection_taxonomymeta` 
RENAME TO  `atlas`.`taxonmeta`;