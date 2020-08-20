-- Add language to translation table primary key. An unique record is identified by the tuple (discriminator, objectId, language).

ALTER TABLE `atlas`.`translation` 
DROP PRIMARY KEY,
ADD PRIMARY KEY (`discriminator`, `objectId`, `language`);
