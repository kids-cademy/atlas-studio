-- Create text translation table.

CREATE TABLE IF NOT EXISTS `atlas`.`translation` (
  `discriminator` INT(11) NOT NULL,
  `objectId` INT(11) NOT NULL,
  `language` VARCHAR(2) NOT NULL,
  `text` TEXT NOT NULL,
  PRIMARY KEY (`discriminator`, `objectId`, `language`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;
