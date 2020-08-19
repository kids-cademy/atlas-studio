-- Refactor atlas object facts from embeddable to entity.

ALTER TABLE `atlas`.`atlasobject_facts` 
RENAME TO  `atlas`.`fact` ;

ALTER TABLE `atlas`.`fact` 
CHANGE COLUMN `facts_key` `title` VARCHAR(128) NOT NULL ,
CHANGE COLUMN `facts` `text` TEXT NOT NULL ;

ALTER TABLE `atlas`.`fact` 
DROP FOREIGN KEY `fk_fact_object1`;

ALTER TABLE `atlas`.`fact` 
CHANGE COLUMN `atlasobject_id` `atlasobject_id` INT(11) NULL DEFAULT NULL ,
DROP INDEX `id_atlasobject_facts_object_id` ,
ADD INDEX `IX_FACT_ATLASOBJECT_ID` (`atlasobject_id` ASC);

ALTER TABLE `atlas`.`fact` 
ADD CONSTRAINT `FK_FACT_ATLASOBJECT_ID`
  FOREIGN KEY (`atlasobject_id`)
  REFERENCES `atlas`.`atlasobject` (`id`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;

ALTER TABLE `atlas`.`fact` 
ADD COLUMN `facts_order` INT(11) NULL DEFAULT NULL AFTER `atlasobject_id`;

ALTER TABLE `atlas`.`fact` 
DROP INDEX `uq_atlasobject_facts_key` ,
ADD UNIQUE INDEX `UQ_FACT_TITLE` (`atlasobject_id` ASC, `title` ASC);
