/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50540
Source Host           : localhost:3306
Source Database       : seckill

Target Server Type    : MYSQL
Target Server Version : 50540
File Encoding         : 65001

Date: 2017-07-11 20:40:46
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `switch`
-- ----------------------------
DROP TABLE IF EXISTS `switch`;
CREATE TABLE `switch` (
  `id` varchar(50) NOT NULL COMMENT '主键',
  `status` char(1) NOT NULL COMMENT '开关。1-开；0-关',
  `type` varchar(2) NOT NULL COMMENT '开关类型。1-秒杀总开关',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=gbk COMMENT='开关表';

-- ----------------------------
-- Records of switch
-- ----------------------------
INSERT INTO `switch` VALUES ('c2bb954d395047f39163a8cd090525bc', '1', '1');
