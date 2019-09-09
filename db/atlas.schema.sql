-- MySQL dump 10.13  Distrib 5.5.60, for Win64 (AMD64)
--
-- Host: localhost    Database: atlas
-- ------------------------------------------------------
-- Server version	5.5.60

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `atlascollection`
--

DROP TABLE IF EXISTS `atlascollection`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `atlascollection` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `display` varchar(45) NOT NULL,
  `iconName` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Temporary table structure for view `atlasitem`
--

DROP TABLE IF EXISTS `atlasitem`;
/*!50001 DROP VIEW IF EXISTS `atlasitem`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `atlasitem` (
  `id` tinyint NOT NULL,
  `collection_id` tinyint NOT NULL,
  `name` tinyint NOT NULL,
  `display` tinyint NOT NULL,
  `iconName` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `atlasobject`
--

DROP TABLE IF EXISTS `atlasobject`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `atlasobject` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `collection_id` int(11) NOT NULL,
  `state` enum('DEVELOPMENT','PUBLISHED') NOT NULL,
  `lastUpdated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `rank` int(11) NOT NULL,
  `name` varchar(45) NOT NULL COMMENT 'Object name unique per dtype. This value is used internally and is not meant to be displayed to user.',
  `display` varchar(45) NOT NULL COMMENT 'Object name as displayed on user interface. It is subject to internationalization.',
  `definition` text,
  `description` text COMMENT 'Object description is rich text, that is, it can contains images, links and text formatting. It is stored as HTML.',
  `sampleTitle` varchar(80) DEFAULT NULL,
  `sampleName` varchar(45) DEFAULT NULL,
  `waveformName` varchar(45) DEFAULT NULL,
  `start_date_value` bigint(20) DEFAULT NULL,
  `start_date_mask` int(11) DEFAULT NULL,
  `end_date_value` bigint(20) DEFAULT NULL,
  `end_date_mask` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_atlasobject_name` (`name`,`collection_id`),
  KEY `fk_atlasobject_user1_idx` (`user_id`),
  KEY `fk_atlasobject_atlascategory1_idx` (`collection_id`),
  CONSTRAINT `fk_atlasobject_atlascategory1` FOREIGN KEY (`collection_id`) REFERENCES `atlascollection` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_atlasobject_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=102 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `atlasobject_aliases`
--

DROP TABLE IF EXISTS `atlasobject_aliases`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `atlasobject_aliases` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `atlasobject_id` int(11) NOT NULL,
  `aliases` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_alias_atlas_object1_idx` (`atlasobject_id`),
  CONSTRAINT `fk_alias_objec_id` FOREIGN KEY (`atlasobject_id`) REFERENCES `atlasobject` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=89 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `atlasobject_classification`
--

DROP TABLE IF EXISTS `atlasobject_classification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `atlasobject_classification` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `atlasobject_id` int(11) NOT NULL,
  `classification_key` varchar(64) NOT NULL,
  `classification` text NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_atlasobject_facts_key` (`atlasobject_id`,`classification_key`),
  KEY `id_atlasobject_facts_object_id` (`atlasobject_id`),
  CONSTRAINT `fk_fact_object11` FOREIGN KEY (`atlasobject_id`) REFERENCES `atlasobject` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=82 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `atlasobject_facts`
--

DROP TABLE IF EXISTS `atlasobject_facts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `atlasobject_facts` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `atlasobject_id` int(11) NOT NULL,
  `facts_key` varchar(64) NOT NULL,
  `facts` text NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_atlasobject_facts_key` (`atlasobject_id`,`facts_key`),
  KEY `id_atlasobject_facts_object_id` (`atlasobject_id`),
  CONSTRAINT `fk_fact_object1` FOREIGN KEY (`atlasobject_id`) REFERENCES `atlasobject` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=144 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `atlasobject_features`
--

DROP TABLE IF EXISTS `atlasobject_features`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `atlasobject_features` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `atlasobject_id` int(11) NOT NULL,
  `features_key` varchar(64) NOT NULL,
  `features` text NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_atlasobject_facts_key` (`atlasobject_id`,`features_key`),
  KEY `id_atlasobject_facts_object_id` (`atlasobject_id`),
  CONSTRAINT `fk_fact_object10` FOREIGN KEY (`atlasobject_id`) REFERENCES `atlasobject` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `atlasobject_links`
--

DROP TABLE IF EXISTS `atlasobject_links`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `atlasobject_links` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `atlasobject_id` int(11) NOT NULL,
  `url` varchar(128) NOT NULL,
  `domain` varchar(45) NOT NULL,
  `display` varchar(45) NOT NULL,
  `description` text NOT NULL,
  `iconName` varchar(45) NOT NULL,
  `features` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_link_url` (`url`,`atlasobject_id`),
  KEY `idx_link_atlasobject_id` (`atlasobject_id`),
  CONSTRAINT `fk_link_atlasobject_id` FOREIGN KEY (`atlasobject_id`) REFERENCES `atlasobject` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=150 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `atlasobject_related`
--

DROP TABLE IF EXISTS `atlasobject_related`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `atlasobject_related` (
  `atlasobject_id` int(11) NOT NULL,
  `related` varchar(45) NOT NULL,
  PRIMARY KEY (`atlasobject_id`,`related`),
  KEY `ix_atlasobject_related_id` (`atlasobject_id`),
  KEY `ix_atlastobject_related_name` (`related`),
  CONSTRAINT `fk_atlastobject_related_name` FOREIGN KEY (`related`) REFERENCES `atlasobject` (`name`) ON DELETE CASCADE,
  CONSTRAINT `fk_atlasobject_related_id` FOREIGN KEY (`atlasobject_id`) REFERENCES `atlasobject` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `atlasobject_spreading`
--

DROP TABLE IF EXISTS `atlasobject_spreading`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `atlasobject_spreading` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `atlasobject_id` int(11) NOT NULL,
  `name` varchar(45) NOT NULL,
  `area` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_region_area` (`atlasobject_id`,`name`,`area`),
  KEY `idx_region_atlasobject_id` (`atlasobject_id`),
  CONSTRAINT `fk_region_atlasobject_id` FOREIGN KEY (`atlasobject_id`) REFERENCES `atlasobject` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=65 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `image`
--

DROP TABLE IF EXISTS `image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `image` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `images_id` int(11) DEFAULT NULL COMMENT 'Atlas object foreign key for pictures list. It cannot be not null because of the way update queries are generated by JPA.',
  `images_order` smallint(6) DEFAULT NULL,
  `name` varchar(45) NOT NULL,
  `uploadDate` datetime NOT NULL,
  `source` text,
  `caption` text,
  `fileName` varchar(45) NOT NULL,
  `fileSize` int(11) NOT NULL,
  `width` smallint(6) NOT NULL,
  `height` smallint(6) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UQ_PICTURE_FILE_NAME` (`images_id`,`fileName`),
  KEY `IX_PICTURE_ATLASOBJECT_ID` (`images_id`),
  CONSTRAINT `FK_ATLASOBJECT_PICTUES_ATLASOBJECT_ID` FOREIGN KEY (`images_id`) REFERENCES `atlasobject` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=279 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `emailAddress` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `emailAddress_UNIQUE` (`emailAddress`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Final view structure for view `atlasitem`
--

/*!50001 DROP TABLE IF EXISTS `atlasitem`*/;
/*!50001 DROP VIEW IF EXISTS `atlasitem`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `atlasitem` AS select `o`.`id` AS `id`,`o`.`collection_id` AS `collection_id`,`o`.`name` AS `name`,`o`.`display` AS `display`,`i`.`fileName` AS `iconName` from (`atlasobject` `o` left join `image` `i` on(((`o`.`id` = `i`.`images_id`) and (`i`.`name` = 'icon')))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-09-09 14:53:25
