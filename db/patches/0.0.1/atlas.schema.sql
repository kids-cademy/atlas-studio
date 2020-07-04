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
-- Table structure for table `androidapp`
--

DROP TABLE IF EXISTS `androidapp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `androidapp` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `release_id` int(11) NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `packageName` varchar(45) NOT NULL,
  `versionCode` int(11) NOT NULL,
  `gitRepository` tinytext NOT NULL,
  `gitUserName` varchar(45) NOT NULL,
  `gitPassword` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UQ_ANDROIDAPP_PACKAGE_NAME` (`packageName`),
  KEY `FK_ANDROIDAPP_RELEASE_idx` (`release_id`),
  CONSTRAINT `FK_ANDROIDAPP_RELEASE` FOREIGN KEY (`release_id`) REFERENCES `release` (`id`) ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `atlascollection`
--

DROP TABLE IF EXISTS `atlascollection`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `atlascollection` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `name` varchar(45) NOT NULL,
  `display` varchar(45) NOT NULL,
  `definition` tinytext NOT NULL,
  `endDate` tinyint(1) NOT NULL DEFAULT '1' COMMENT 'Flag for object end date, default to true. If this flag is false user interface should not display controls for end date.',
  `progenitor` tinyint(1) NOT NULL DEFAULT '1',
  `conservationStatus` tinyint(1) NOT NULL DEFAULT '1' COMMENT 'Flag for object conservation status, default to true. If this flag is false user interface should not display controls for conservation status.',
  `audioSample` tinyint(1) NOT NULL DEFAULT '1' COMMENT 'Flag for object audio sample, default to true. If this flag is false user interface should not display controls for audio sample.',
  `spreading` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UQ_ATLAS_COLLECTION_NAME` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `atlascollection_descriptionmeta`
--

DROP TABLE IF EXISTS `atlascollection_descriptionmeta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `atlascollection_descriptionmeta` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `atlascollection_id` int(11) NOT NULL,
  `descriptionmeta_order` int(11) NOT NULL,
  `name` varchar(45) NOT NULL,
  `definition` tinytext NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_atlascollection_descriptionmeta_atlascollection1_idx` (`atlascollection_id`),
  CONSTRAINT `fk_atlascollection_descriptionmeta_atlascollection1` FOREIGN KEY (`atlascollection_id`) REFERENCES `atlascollection` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `atlascollection_featuremeta`
--

DROP TABLE IF EXISTS `atlascollection_featuremeta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `atlascollection_featuremeta` (
  `atlascollection_id` int(11) NOT NULL,
  `featuresmeta_id` int(11) NOT NULL,
  `featuresmeta_order` int(11) NOT NULL,
  PRIMARY KEY (`atlascollection_id`,`featuresmeta_id`),
  KEY `fk_atlascollection_featuremeta_atlascollection1_idx` (`atlascollection_id`),
  KEY `fk_atlascollection_featuremeta_featuremeta1_idx` (`featuresmeta_id`),
  CONSTRAINT `fk_atlascollection_featuremeta_atlascollection1` FOREIGN KEY (`atlascollection_id`) REFERENCES `atlascollection` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_atlascollection_featuremeta_featuremeta1` FOREIGN KEY (`featuresmeta_id`) REFERENCES `featuremeta` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `atlascollection_taxonomymeta`
--

DROP TABLE IF EXISTS `atlascollection_taxonomymeta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `atlascollection_taxonomymeta` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `atlascollection_id` int(11) NOT NULL,
  `taxonomymeta_order` int(11) NOT NULL,
  `name` varchar(45) NOT NULL,
  `values` tinytext,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UQ_TAXONOMYMETA_NAME` (`name`,`atlascollection_id`),
  KEY `FK_TAXONOMYMETA_COLLECTION_ID` (`atlascollection_id`),
  CONSTRAINT `fk_taxon_meta_atlascollection1` FOREIGN KEY (`atlascollection_id`) REFERENCES `atlascollection` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=129 DEFAULT CHARSET=utf8;
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
  `definition` tinyint NOT NULL,
  `iconName` tinyint NOT NULL,
  `state` tinyint NOT NULL,
  `timestamp` tinyint NOT NULL
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
  `collection_id` int(11) NOT NULL,
  `state` enum('NONE','CREATED','DEVELOPMENT','RELEASED','PUBLISHED') NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `name` varchar(45) NOT NULL COMMENT 'Object name unique per dtype. This value is used internally and is not meant to be displayed to user.',
  `display` varchar(45) NOT NULL COMMENT 'Object name as displayed on user interface. It is subject to internationalization.',
  `definition` text NOT NULL,
  `description` text COMMENT 'Object description is rich text, that is, it can contains images, links and text formatting. It is stored as HTML.',
  `sampleTitle` varchar(128) DEFAULT NULL,
  `sampleName` varchar(45) DEFAULT NULL,
  `waveformName` varchar(45) DEFAULT NULL,
  `startDateValue` double DEFAULT NULL,
  `startDateMask` int(11) DEFAULT NULL,
  `endDateValue` double DEFAULT NULL,
  `endDateMask` int(11) DEFAULT NULL,
  `progenitor` varchar(45) DEFAULT NULL COMMENT 'Progenitor is the entity that somehow originates this object. For example, if this atlas object is an airplaine, progenitor is the manufacturer.',
  `conservation` int(11) DEFAULT NULL COMMENT 'Optional conservation status for life forms.',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UQ_ATLASOBJECT_NAME` (`name`),
  KEY `IX_ATLASOBJECT_COLLECTION_ID` (`collection_id`),
  CONSTRAINT `FK_ATLASOBJECT_COLLECTION_ID` FOREIGN KEY (`collection_id`) REFERENCES `atlascollection` (`id`) ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=856 DEFAULT CHARSET=utf8;
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
  `aliases_order` int(11) NOT NULL,
  `aliases` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_alias_atlas_object1_idx` (`atlasobject_id`),
  CONSTRAINT `fk_alias_objec_id` FOREIGN KEY (`atlasobject_id`) REFERENCES `atlasobject` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=486 DEFAULT CHARSET=utf8;
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
  `facts_key` varchar(128) NOT NULL,
  `facts` text NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_atlasobject_facts_key` (`atlasobject_id`,`facts_key`),
  KEY `id_atlasobject_facts_object_id` (`atlasobject_id`),
  CONSTRAINT `fk_fact_object1` FOREIGN KEY (`atlasobject_id`) REFERENCES `atlasobject` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=4389 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `atlasobject_images`
--

DROP TABLE IF EXISTS `atlasobject_images`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `atlasobject_images` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `atlasobject_id` int(11) NOT NULL COMMENT 'Atlas object foreign key for pictures list. It cannot be not null because of the way update queries are generated by JPA.',
  `imageKey` varchar(45) NOT NULL,
  `uploadDate` datetime NOT NULL,
  `source` text,
  `caption` text,
  `fileName` varchar(45) NOT NULL,
  `fileSize` int(11) NOT NULL,
  `width` smallint(6) NOT NULL,
  `height` smallint(6) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UQ_IMAGE_FILE_NAME` (`atlasobject_id`,`fileName`),
  UNIQUE KEY `UQ_IMAGE_KEY` (`atlasobject_id`,`imageKey`),
  KEY `IX_IMAGE_ATLASOBJECT_ID` (`atlasobject_id`),
  CONSTRAINT `FK_ATLASOBJECT_IMAGES_ATLASOBJECT_ID` FOREIGN KEY (`atlasobject_id`) REFERENCES `atlasobject` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=4305 DEFAULT CHARSET=utf8;
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
  `links_order` int(11) DEFAULT NULL,
  `url` tinytext NOT NULL,
  `domain` varchar(45) NOT NULL,
  `display` varchar(45) NOT NULL,
  `definition` tinytext NOT NULL,
  `features` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `IX_ATLASOBJECT_LINKS_ATLASOBJECT_ID` (`atlasobject_id`),
  CONSTRAINT `fk_link_atlasobject_id` FOREIGN KEY (`atlasobject_id`) REFERENCES `atlasobject` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2812 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `atlasobject_related`
--

DROP TABLE IF EXISTS `atlasobject_related`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `atlasobject_related` (
  `atlasobject_id` int(11) NOT NULL,
  `related_order` int(11) NOT NULL,
  `related` varchar(45) NOT NULL COMMENT 'The name of related object. Should reference an existing object. There is foreign key constraint that update this column when related object name is changed. Similar for remove.',
  PRIMARY KEY (`atlasobject_id`,`related`),
  KEY `ix_atlasobject_related_id` (`atlasobject_id`),
  KEY `IX_ATLASOBJECT_RELATED_NAME` (`related`),
  CONSTRAINT `FK_ATLASOBJECT_RELATED_ID` FOREIGN KEY (`atlasobject_id`) REFERENCES `atlasobject` (`id`) ON DELETE CASCADE,
  CONSTRAINT `FK_ATLASOBJECT_RELATED_NAME` FOREIGN KEY (`related`) REFERENCES `atlasobject` (`name`) ON DELETE CASCADE ON UPDATE CASCADE
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
  `spreading_order` int(11) NOT NULL,
  `name` varchar(45) NOT NULL,
  `area` int(11) NOT NULL,
  `less` varchar(45) DEFAULT NULL,
  `lessArea` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_region_area` (`atlasobject_id`,`name`,`area`),
  KEY `idx_region_atlasobject_id` (`atlasobject_id`),
  CONSTRAINT `fk_region_atlasobject_id` FOREIGN KEY (`atlasobject_id`) REFERENCES `atlasobject` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=329 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `atlasobject_taxonomy`
--

DROP TABLE IF EXISTS `atlasobject_taxonomy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `atlasobject_taxonomy` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `atlasobject_id` int(11) NOT NULL,
  `taxonomy_order` int(11) NOT NULL,
  `name` varchar(45) NOT NULL,
  `value` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_atlasobject_facts_key` (`atlasobject_id`,`name`),
  KEY `id_atlasobject_facts_object_id` (`atlasobject_id`),
  CONSTRAINT `fk_fact_object11` FOREIGN KEY (`atlasobject_id`) REFERENCES `atlasobject` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=3999 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Temporary table structure for view `exportitem`
--

DROP TABLE IF EXISTS `exportitem`;
/*!50001 DROP VIEW IF EXISTS `exportitem`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `exportitem` (
  `id` tinyint NOT NULL,
  `collection_id` tinyint NOT NULL,
  `name` tinyint NOT NULL,
  `display` tinyint NOT NULL,
  `state` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `feature`
--

DROP TABLE IF EXISTS `feature`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `feature` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `atlasobject_id` int(11) DEFAULT NULL COMMENT 'Foreign key to parent atlas object. It cannot be not null because of the way JPA mappings works.',
  `meta_id` int(11) NOT NULL,
  `features_order` int(11) DEFAULT NULL COMMENT 'Feature list order. It cannot be not null because of the way JPA mappings works.',
  `value` double NOT NULL,
  `maximum` double DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `IX_FEATURE_ATLASOBJECT_ID` (`atlasobject_id`),
  KEY `IX_FEATURE_FEATUREMETA_ID` (`meta_id`),
  CONSTRAINT `FK_FEATURE_ATLASOBJECT_ID` FOREIGN KEY (`atlasobject_id`) REFERENCES `atlasobject` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `FK_FEATURE_FEATUREMETA_ID` FOREIGN KEY (`meta_id`) REFERENCES `featuremeta` (`id`) ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=17664 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `featuremeta`
--

DROP TABLE IF EXISTS `featuremeta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `featuremeta` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `quantity` enum('NONE','SCALAR','MASS','TIME','LENGTH','SPEED','POWER','FOOD_ENERGY','DENSITY','ACCELERATION','ANGLE','AREA','POPULATION_DENSITY','CURRENCY') NOT NULL,
  `definition` tinytext NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UQ_FEATUREMETA_NAME` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=85 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Temporary table structure for view `imageslist`
--

DROP TABLE IF EXISTS `imageslist`;
/*!50001 DROP VIEW IF EXISTS `imageslist`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `imageslist` (
  `id` tinyint NOT NULL,
  `atlasobject_id` tinyint NOT NULL,
  `imageKey` tinyint NOT NULL,
  `uploadDate` tinyint NOT NULL,
  `source` tinyint NOT NULL,
  `caption` tinyint NOT NULL,
  `fileName` tinyint NOT NULL,
  `fileSize` tinyint NOT NULL,
  `width` tinyint NOT NULL,
  `height` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `linkmeta`
--

DROP TABLE IF EXISTS `linkmeta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `linkmeta` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `domain` varchar(45) NOT NULL,
  `display` varchar(45) NOT NULL,
  `definition` tinytext NOT NULL,
  `features` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_linkmeta_domain` (`domain`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `quantity`
--

DROP TABLE IF EXISTS `quantity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `quantity` (
  `id` int(11) NOT NULL,
  `name` enum('NONE','SCALAR','MASS','TIME','LENGTH','SPEED','POWER','FOOD_ENERGY','DENSITY','ACCELERATION','ANGLE','AREA','POPULATION_DENSITY','CURRENCY') NOT NULL,
  `display` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `release`
--

DROP TABLE IF EXISTS `release`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `release` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `contentTimestamp` timestamp NOT NULL DEFAULT '2019-12-31 22:00:00',
  `name` varchar(45) NOT NULL,
  `display` varchar(45) NOT NULL,
  `brief` varchar(45) NOT NULL,
  `definition` tinytext NOT NULL,
  `graphicsBackground` varchar(6) NOT NULL DEFAULT 'FFFFFF',
  `publisher` varchar(45) NOT NULL,
  `edition` varchar(45) NOT NULL,
  `version` varchar(45) NOT NULL,
  `license` varchar(45) NOT NULL,
  `readme` text NOT NULL,
  `privacy` text NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UQ_RELEASE_NAME` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `release_atlasitem`
--

DROP TABLE IF EXISTS `release_atlasitem`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `release_atlasitem` (
  `release_id` int(11) NOT NULL,
  `objects_id` int(11) NOT NULL,
  PRIMARY KEY (`release_id`,`objects_id`),
  KEY `fk_release_atlasobject_atlasobject1_idx` (`objects_id`),
  CONSTRAINT `fk_release_atlasobject_atlasobject1` FOREIGN KEY (`objects_id`) REFERENCES `atlasobject` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_release_atlasobject_release1` FOREIGN KEY (`release_id`) REFERENCES `release` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `units`
--

DROP TABLE IF EXISTS `units`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `units` (
  `id` int(11) NOT NULL,
  `quantity_id` int(11) NOT NULL,
  `factor` varchar(45) NOT NULL,
  `symbol` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_units_quantity1_idx` (`quantity_id`),
  CONSTRAINT `fk_units_quantity1` FOREIGN KEY (`quantity_id`) REFERENCES `quantity` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
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
  `roles` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `emailAddress_UNIQUE` (`emailAddress`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
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
/*!50001 VIEW `atlasitem` AS select `o`.`id` AS `id`,`o`.`collection_id` AS `collection_id`,`o`.`name` AS `name`,`o`.`display` AS `display`,`o`.`definition` AS `definition`,`i`.`fileName` AS `iconName`,`o`.`state` AS `state`,`o`.`timestamp` AS `timestamp` from (`atlasobject` `o` left join `atlasobject_images` `i` on(((`o`.`id` = `i`.`atlasobject_id`) and (`i`.`imageKey` = 'icon')))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `exportitem`
--

/*!50001 DROP TABLE IF EXISTS `exportitem`*/;
/*!50001 DROP VIEW IF EXISTS `exportitem`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `exportitem` AS select `o`.`id` AS `id`,`o`.`collection_id` AS `collection_id`,`o`.`name` AS `name`,`o`.`display` AS `display`,`o`.`state` AS `state` from `atlasobject` `o` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `imageslist`
--

/*!50001 DROP TABLE IF EXISTS `imageslist`*/;
/*!50001 DROP VIEW IF EXISTS `imageslist`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `imageslist` AS select `atlasobject_images`.`id` AS `id`,`atlasobject_images`.`atlasobject_id` AS `atlasobject_id`,`atlasobject_images`.`imageKey` AS `imageKey`,`atlasobject_images`.`uploadDate` AS `uploadDate`,`atlasobject_images`.`source` AS `source`,`atlasobject_images`.`caption` AS `caption`,`atlasobject_images`.`fileName` AS `fileName`,`atlasobject_images`.`fileSize` AS `fileSize`,`atlasobject_images`.`width` AS `width`,`atlasobject_images`.`height` AS `height` from `atlasobject_images` order by (case `atlasobject_images`.`imageKey` when 'icon' then 0 when 'cover' then 1 when 'contextual' then 2 when 'featured' then 3 when 'trivia' then 4 end) */;
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

-- Dump completed on 2020-05-14 19:49:35
