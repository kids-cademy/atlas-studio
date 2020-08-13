-- Convert atlas object taxonomy from embeddable to entity.
-- For that first rename table to 'taxon', that is the case insensitive simple name of the class com.kidscademy.atlas.studio.model.Taxon .

ALTER TABLE `atlas`.`atlasobject_taxonomy` 
RENAME TO  `atlas`.`taxon` ;
