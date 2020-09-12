-- Update TAXON_UNIT_DISPLAY enumeration constant on translation table, discriminator column.

ALTER TABLE `atlas`.`translation` 
CHANGE COLUMN `discriminator` `discriminator` ENUM('OBJECT_DISPLAY', 'OBJECT_ALIASES', 'OBJECT_DEFINITION', 'OBJECT_DESCRIPTION', 'OBJECT_SAMPLE_TITLE', 'FACT_TITLE', 'FACT_TEXT', 'TAXON_UNIT_DISPLAY', 'TAXON_VALUE', 'FEATURE_META_DISPLAY', 'FEATURE_VALUE', 'EXTERNAL_SOURCE_DISPLAY', 'EXTERNAL_SOURCE_DEFINITION_TEMPLATE') NOT NULL ;
