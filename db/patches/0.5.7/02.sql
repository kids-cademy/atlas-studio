-- Infer meta_id foreign key.

UPDATE `atlas`.`atlasobject` o 
JOIN `atlas`.`taxon` t ON o.id=t.atlasobject_id 
JOIN `atlas`.`atlascollection_taxonomymeta` m ON o.collection_id=m.atlascollection_id

SET t.`meta_id`=m.`id`

WHERE t.name=m.name;
 