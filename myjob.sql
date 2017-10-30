/*
Navicat MySQL Data Transfer

Source Server         : qqzone
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : myjob

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2017-10-30 14:33:18
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for myjob
-- ----------------------------
DROP TABLE IF EXISTS `myjob`;
CREATE TABLE `myjob` (
  `Id` int(32) NOT NULL AUTO_INCREMENT,
  `JobName` varchar(32) DEFAULT NULL,
  `Salary` varchar(10) DEFAULT NULL,
  `Education` varchar(50) DEFAULT NULL,
  `Experience` varchar(50) DEFAULT NULL,
  `JobTemptation` varchar(100) DEFAULT NULL,
  `CompanyName` varchar(60) DEFAULT NULL,
  `Filed` varchar(50) NOT NULL,
  `Scale` varchar(50) DEFAULT NULL,
  `Financing` varchar(50) DEFAULT NULL,
  `City` varchar(50) DEFAULT NULL,
  `Address` varchar(255) DEFAULT NULL,
  `Website` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=136 DEFAULT CHARSET=utf8;
