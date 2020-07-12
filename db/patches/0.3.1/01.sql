-- Rename external source definition to definition template.

ALTER TABLE `atlas`.`externalsource` 
CHANGE COLUMN `definition` `definitionTemplate` TINYTEXT NOT NULL ;