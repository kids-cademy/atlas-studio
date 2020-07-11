-- Create LinkSource table.
-- Link source is and external source with a subset of APIs. Although is an entity, a LinkSource life cycle is controlled by AtlasCollection.
-- If parent AtlasCollection is removed child LinkSource is removed too. The same if refered ExternalSource is removed. 

CREATE TABLE IF NOT EXISTS `atlas`.`linksource` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `atlascollection_id` INT(11) DEFAULT NULL,
  `linksources_order` INT(11) DEFAULT '0',
  `externalsource_id` INT(11) NOT NULL,
  `apis` VARCHAR(45) NOT NULL,
  
  INDEX `IX_LINKSOURCE_ATLASCOLLECTION_ID` (`atlascollection_id` ASC),
  INDEX `IX_LINKSOURCE_EXTERNALSOURCE_ID` (`externalsource_id` ASC),
  
  PRIMARY KEY (`id`),
  
  CONSTRAINT `FK_LINKSOURCE_ATLASCOLLECTION_ID`
    FOREIGN KEY (`atlascollection_id`)
    REFERENCES `atlas`.`atlascollection` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_LINKSOURCE_EXTERNALSOURCE_ID`
    FOREIGN KEY (`externalsource_id`)
    REFERENCES `atlas`.`externalsource` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;
