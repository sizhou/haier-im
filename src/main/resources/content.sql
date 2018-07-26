/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50640
Source Host           : localhost:3306
Source Database       : jingrui-manager

Target Server Type    : MYSQL
Target Server Version : 50640
File Encoding         : 65001

Date: 2018-07-23 12:59:06
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for content
-- ----------------------------
DROP TABLE IF EXISTS `content`;
CREATE TABLE `content` (
  `id` bigint(19) NOT NULL AUTO_INCREMENT,
  `city` varchar(255) DEFAULT NULL,
  `entry_time` varchar(25) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `content` varchar(200) DEFAULT NULL,
  `status` int(11) DEFAULT '1' COMMENT '1：上线，2：下线',
  `createdBy` bigint(20) DEFAULT NULL,
  `createdTime` datetime DEFAULT CURRENT_TIMESTAMP,
  `updatedBy` bigint(20) DEFAULT NULL,
  `updatedTime` datetime DEFAULT NULL,
  `deletedBy` bigint(20) DEFAULT NULL,
  `deletedTime` datetime DEFAULT NULL,
  `isDeleted` tinyint(1) unsigned zerofill NOT NULL DEFAULT '0' COMMENT '是否删除，默认是0表示未删除，1表示被删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
