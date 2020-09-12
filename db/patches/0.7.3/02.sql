-- Populate taxonunit table from taxonmeta.

INSERT INTO `atlas`.`taxonunit` (`name`,`display`) (SELECT DISTINCT `name`,`display` FROM taxonmeta);