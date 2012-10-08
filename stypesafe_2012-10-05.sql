# ************************************************************
# Sequel Pro SQL dump
# Version 3408
#
# http://www.sequelpro.com/
# http://code.google.com/p/sequel-pro/
#
# Host: isnow.de (MySQL 5.1.63-0+squeeze1-log)
# Database: stypesafe
# Generation Time: 2012-10-05 19:56:24 +0200
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table activitylog
# ------------------------------------------------------------

DROP TABLE IF EXISTS `activitylog`;

CREATE TABLE `activitylog` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `timestamp` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  `activity` varchar(255) NOT NULL DEFAULT '',
  `result` varchar(20000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table author
# ------------------------------------------------------------

DROP TABLE IF EXISTS `author`;

CREATE TABLE `author` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `stypiuid` varchar(255) NOT NULL DEFAULT '',
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table document
# ------------------------------------------------------------

DROP TABLE IF EXISTS `document`;

CREATE TABLE `document` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `stypiuid` varchar(255) NOT NULL DEFAULT '',
  `url` varchar(20000) NOT NULL DEFAULT '',
  `title` varchar(255) DEFAULT NULL,
  `before_rename` int(11) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `document_history` (`before_rename`),
  CONSTRAINT `document_history` FOREIGN KEY (`before_rename`) REFERENCES `document` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table documentfragment
# ------------------------------------------------------------

DROP TABLE IF EXISTS `documentfragment`;

CREATE TABLE `documentfragment` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `text` mediumtext NOT NULL,
  `fingerprint` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table documentversion
# ------------------------------------------------------------

DROP TABLE IF EXISTS `documentversion`;

CREATE TABLE `documentversion` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `fk_document` int(11) unsigned NOT NULL,
  `version` int(11) unsigned NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `fingerprint` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  KEY `document_version` (`fk_document`),
  CONSTRAINT `document_version` FOREIGN KEY (`fk_document`) REFERENCES `document` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table error
# ------------------------------------------------------------

DROP TABLE IF EXISTS `error`;

CREATE TABLE `error` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `timestamp` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  `error` varchar(20000) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table fragmentXversion
# ------------------------------------------------------------

DROP TABLE IF EXISTS `fragmentXversion`;

CREATE TABLE `fragmentXversion` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `fk_documentversion` int(11) unsigned NOT NULL,
  `fk_documentfragment` int(11) unsigned NOT NULL,
  `fk_author` int(11) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `version` (`fk_documentversion`),
  KEY `fragment` (`fk_documentfragment`),
  KEY `author` (`fk_author`),
  CONSTRAINT `author` FOREIGN KEY (`fk_author`) REFERENCES `author` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `fragment` FOREIGN KEY (`fk_documentfragment`) REFERENCES `documentfragment` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `version` FOREIGN KEY (`fk_documentversion`) REFERENCES `documentversion` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;




/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
