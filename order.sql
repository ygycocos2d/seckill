/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50540
Source Host           : 127.0.0.1:3306
Source Database       : seckill

Target Server Type    : MYSQL
Target Server Version : 50540
File Encoding         : 65001

Date: 2017-06-13 22:11:38
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `order`
-- ----------------------------
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order` (
  `order_id` varchar(32) NOT NULL COMMENT '主键ID',
  `user_id` varchar(32) NOT NULL,
  `goods_id` varchar(32) NOT NULL,
  `goods_number` int(11) NOT NULL,
  `status` varchar(2) NOT NULL COMMENT '订单状态，0-创建，1-已支付，2-过期，9-删除'
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of order
-- ----------------------------
