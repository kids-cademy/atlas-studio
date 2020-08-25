-- Add display column to feature table. Display column text is formated from feature content using server logic. Display is updated every time feature values are changed. 

ALTER TABLE `atlas`.`feature` 
ADD COLUMN `display` VARCHAR(45) NOT NULL AFTER `maximum`;

