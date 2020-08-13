-- Remove NOT NULL constrain from atlas object foreign key and taxonomy order columns to obey JPA mappings constraints.

ALTER TABLE `atlas`.`taxon` 
DROP FOREIGN KEY `fk_fact_object11`;

ALTER TABLE `atlas`.`taxon` 
CHANGE COLUMN `atlasobject_id` `atlasobject_id` INT(11) NULL DEFAULT NULL ,
CHANGE COLUMN `taxonomy_order` `taxonomy_order` INT(11) NULL DEFAULT NULL ,
DROP INDEX `uq_atlasobject_facts_key` ,
ADD UNIQUE INDEX `UQ_TAXON_NAME` (`atlasobject_id` ASC, `name` ASC),
DROP INDEX `id_atlasobject_facts_object_id` ,
ADD INDEX `IX_TAXON_ATLASOBJECT_ID` (`atlasobject_id` ASC);

ALTER TABLE `atlas`.`taxon` 
ADD CONSTRAINT `FK_TAXON_ATLASOBJECT_ID`
  FOREIGN KEY (`atlasobject_id`)
  REFERENCES `atlas`.`atlasobject` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
  