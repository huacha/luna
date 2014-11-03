/*
 Navicat Premium Data Transfer

 Source Server         : MySQL
 Source Server Type    : MySQL
 Source Server Version : 50620
 Source Host           : localhost
 Source Database       : luna

 Target Server Type    : MySQL
 Target Server Version : 50620
 File Encoding         : utf-8

 Date: 11/03/2014 10:24:21 AM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `BPM_TASK_BUSSINESS`
-- ----------------------------
DROP TABLE IF EXISTS `BPM_TASK_BUSSINESS`;
CREATE TABLE `BPM_TASK_BUSSINESS` (
  `TASK_ID` varchar(100) NOT NULL,
  `TASK_NAME` varchar(100) NOT NULL,
  `TABLE_NAME` varchar(100) NOT NULL,
  `BUSSI_ID` bigint(20) NOT NULL,
  `PROCESSINST_ID` varchar(100) NOT NULL,
  `BUSSI_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`TASK_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;
