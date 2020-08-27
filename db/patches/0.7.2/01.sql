-- Update feature display format for 'Up to' values.

UPDATE `atlas`.`feature` SET display=REPLACE(display, "Up", "up") WHERE display LIKE 'Up to %';