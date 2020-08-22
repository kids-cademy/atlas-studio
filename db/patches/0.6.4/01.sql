-- Remove content time stamp column from release table. Use only timestamp to detect all changes on release, including content.  

ALTER TABLE `atlas`.`release` 
DROP COLUMN `contentTimestamp`;
