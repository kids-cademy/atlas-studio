-- Create atlas collection languages table. These are languages that all objects from collection should support and for which there are translation records created.
-- Since there can a pretty small number of languages supported there is no need to use separated table. 
-- Instead store langauge codes as comma separated strings list. See com.kidscademy.atlas.studio.dao.StringsListConverter class.
-- Language is two letters code ISO 639-1.

ALTER TABLE `atlas`.`atlascollection` 
ADD COLUMN `languages` VARCHAR(45) NOT NULL AFTER `definition`;
