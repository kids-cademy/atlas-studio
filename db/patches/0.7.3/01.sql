-- Create taxonunit table to store unique taxon name and display. Also create primary key and unique index for name column.

CREATE TABLE IF NOT EXISTS `atlas`.`taxonunit` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `display` VARCHAR(45) NOT NULL,
  `definition` TINYTEXT NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `UQ_TAXONUNIT_NAME` (`name` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;
