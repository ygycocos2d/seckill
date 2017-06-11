/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50540
Source Host           : 127.0.0.1:3306
Source Database       : seckill

Target Server Type    : MYSQL
Target Server Version : 50540
File Encoding         : 65001

Date: 2017-06-11 21:31:40
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `activity_goods_inventory_log`
-- ----------------------------
DROP TABLE IF EXISTS `activity_goods_inventory_log`;
CREATE TABLE `activity_goods_inventory_log` (
  `id` varchar(32) NOT NULL COMMENT '主键ID',
  `activity_id` varchar(32) NOT NULL COMMENT '秒杀活动id',
  `goods_id` varchar(32) NOT NULL COMMENT '商品id',
  `goods_inventory` int(11) NOT NULL COMMENT '负数：秒杀活动从商品中减库存，正数：秒杀活动给商品还库存',
  `describt` varchar(256) DEFAULT NULL COMMENT '商品库存去向简单说明',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='活动减还库存记录表';

-- ----------------------------
-- Records of activity_goods_inventory_log
-- ----------------------------
