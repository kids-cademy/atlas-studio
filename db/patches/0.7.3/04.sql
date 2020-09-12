-- Update taxon meta foreign key values to taxon unit primary key.

UPDATE `atlas`.`taxonmeta` meta SET `unit_id`=(SELECT unit.`id` FROM `atlas`.`taxonunit` unit WHERE unit.`name`=meta.`name`);
