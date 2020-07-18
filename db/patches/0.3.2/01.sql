-- Rename foreign keys from release_atlasitem table and cascade remove operation.

ALTER TABLE `atlas`.`release_atlasitem` 
DROP FOREIGN KEY `fk_release_atlasobject_atlasobject1`;

ALTER TABLE `atlas`.`release_atlasitem` 
DROP FOREIGN KEY `fk_release_atlasobject_release1`;

ALTER TABLE `atlas`.`release_atlasitem` 
ADD CONSTRAINT `FK_RELEASE_ATLASITEM_RELEASE_ID`
  FOREIGN KEY (`release_id`)
  REFERENCES `atlas`.`release` (`id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;
  
ALTER TABLE `atlas`.`release_atlasitem` 
ADD CONSTRAINT `FK_RELEASE_ATLASITEM_OBJECTS_ID`
  FOREIGN KEY (`objects_id`)
  REFERENCES `atlas`.`atlasobject` (`id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;
