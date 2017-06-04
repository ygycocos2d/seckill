/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50540
Source Host           : 127.0.0.1:3306
Source Database       : seckill

Target Server Type    : MYSQL
Target Server Version : 50540
File Encoding         : 65001

Date: 2017-06-04 22:14:02
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `activity`
-- ----------------------------
DROP TABLE IF EXISTS `activity`;
CREATE TABLE `activity` (
  `activity_id` varchar(32) NOT NULL COMMENT '秒杀任务ID',
  `group_id` varchar(32) NOT NULL COMMENT '秒杀任务组ID',
  `start_time` datetime NOT NULL COMMENT '秒杀任务开始时间',
  `end_time` datetime NOT NULL COMMENT '秒杀任务结束时间',
  `goods_id` varchar(32) NOT NULL COMMENT '秒杀商品ID',
  `goods_price` int(11) NOT NULL COMMENT '秒杀商品的秒伤价,以分为单位',
  `goods_number` int(11) NOT NULL COMMENT '秒杀商品数量',
  `left_goods_number` int(11) DEFAULT NULL COMMENT '整个秒杀活动没分配到应用处理的库存',
  `limit_number` int(11) NOT NULL DEFAULT '1' COMMENT '每个用户至多秒杀的限制数量',
  `pay_delay` int(11) NOT NULL COMMENT '秒杀成功不支付失效时间，时间为秒',
  `status` char(1) NOT NULL COMMENT '秒杀任务状态，0-启动，1-暂停，2-删除',
  `describt` varchar(1024) DEFAULT NULL COMMENT '秒杀描述',
  PRIMARY KEY (`activity_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='秒杀任务信息表';

-- ----------------------------
-- Records of activity
-- ----------------------------
INSERT INTO `activity` VALUES ('e2c9b3ca88ca48948d2d65c126972c47', 'd215349fa060425c8198bbc40f65d597', '2017-06-03 08:00:00', '2017-06-04 08:00:00', '123456789101213141516171819203', '10', '10', null, '1', '6000', '0', '天下武功唯快不破');

-- ----------------------------
-- Table structure for `goods`
-- ----------------------------
DROP TABLE IF EXISTS `goods`;
CREATE TABLE `goods` (
  `goods_id` varchar(32) NOT NULL COMMENT '商品ID',
  `goods_name` varchar(512) NOT NULL COMMENT '商品名称',
  `goods_price` int(11) NOT NULL COMMENT '商品价格',
  `goods_number` int(11) NOT NULL COMMENT '商品库存',
  PRIMARY KEY (`goods_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='商品信息表';

-- ----------------------------
-- Records of goods
-- ----------------------------
INSERT INTO `goods` VALUES ('123456789101213141516171819203', 'rio275', '10000', '100');

-- ----------------------------
-- Table structure for `img`
-- ----------------------------
DROP TABLE IF EXISTS `img`;
CREATE TABLE `img` (
  `img_id` varchar(32) NOT NULL COMMENT '秒杀商品展示图ID',
  `activity_id` varchar(32) NOT NULL COMMENT '秒杀任务ID',
  `img_url` varchar(256) NOT NULL COMMENT '展示图片url',
  `img_type` varchar(2) NOT NULL COMMENT '图片类型',
  PRIMARY KEY (`img_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='秒杀任务展示图片信息表';

-- ----------------------------
-- Records of img
-- ----------------------------

-- ----------------------------
-- Table structure for `order`
-- ----------------------------
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order` (
  `order_id` varchar(32) NOT NULL COMMENT '主键ID',
  `user_id` varchar(32) NOT NULL,
  `goods_id` varchar(32) NOT NULL,
  `goods_number` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of order
-- ----------------------------

-- ----------------------------
-- Table structure for `success_log`
-- ----------------------------
DROP TABLE IF EXISTS `success_log`;
CREATE TABLE `success_log` (
  `succlog_id` varchar(32) NOT NULL COMMENT '主键',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `activity_id` varchar(32) NOT NULL COMMENT '秒杀活动ID',
  `goods_number` int(11) NOT NULL COMMENT '实际秒杀到的商品数，有可能超过规定限制数',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`succlog_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='用户秒杀成功记录表';

-- ----------------------------
-- Records of success_log
-- ----------------------------

-- ----------------------------
-- Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `user_id` varchar(32) NOT NULL COMMENT '用户唯一ID',
  `user_account` varchar(32) NOT NULL COMMENT '用户账户',
  `user_pwd` varchar(32) NOT NULL COMMENT '用户密码',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_id` (`user_id`) USING BTREE
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='用户信息表';

-- ----------------------------
-- Records of user
-- ----------------------------
