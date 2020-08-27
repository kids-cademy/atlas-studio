-- Update translation text format for 'Up to' values.

UPDATE `atlas`.`translation` SET text=REPLACE(text, "Până", "până") WHERE text LIKE 'Până la %';
