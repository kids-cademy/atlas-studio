-- Add foreign key from atlasobject_links table to linksource table
-- Disable keys check so that we can create columns and add constrain in a single statement
  
SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

ALTER TABLE `atlas`.`atlasobject_links` 
ADD COLUMN `linksource_id` INT(11) NOT NULL AFTER `links_order`,

ADD INDEX `IX_ATLASOBJECT_LINKS_LINKSOURCE_ID` (`linksource_id` ASC),

ADD CONSTRAINT `FK_ATLASOBJECT_LINKS_LINKSOURCE_ID`
  FOREIGN KEY (`linksource_id`)
  REFERENCES `atlas`.`linksource` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
