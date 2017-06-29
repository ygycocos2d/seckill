/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50540
Source Host           : localhost:3306
Source Database       : seckill

Target Server Type    : MYSQL
Target Server Version : 50540
File Encoding         : 65001

Date: 2017-06-29 10:04:17
*/

create database seckill;

use seckill;

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
  `limit_number` int(11) NOT NULL DEFAULT '1' COMMENT '每个用户至多秒杀的限制数量',
  `pay_delay` int(11) DEFAULT '1800' COMMENT '秒杀成功不支付过期时间，单位为秒；当为0-永不过期；默认30分钟，即1800秒',
  `status` char(1) NOT NULL COMMENT '秒杀任务状态，0-启动，1-暂停，2-删除',
  `describt` varchar(1024) DEFAULT NULL COMMENT '秒杀描述',
  PRIMARY KEY (`activity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='秒杀任务信息表';

-- ----------------------------
-- Records of activity
-- ----------------------------
INSERT INTO `activity` VALUES ('a0397652140c40deacba69cbeabfe973', '52f0fbbc14ab4e45a4fe58b8854ce11a', '2017-06-27 17:48:00', '2017-06-27 17:48:30', '123456789101213141516171819203', '10', '10', '1', '60', '0', '天下武功唯快不破');
INSERT INTO `activity` VALUES ('e2c9b3ca88ca48948d2d65c126972c47', 'd215349fa060425c8198bbc40f65d597', '2017-06-24 12:59:00', '2017-06-24 11:07:30', '123456789101213141516171819203', '10', '10', '1', '6000', '1', '天下武功唯快不破');

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
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='活动减还库存记录表';

-- ----------------------------
-- Records of activity_goods_inventory_log
-- ----------------------------
INSERT INTO `activity_goods_inventory_log` VALUES ('0f629f0d09324272af14b60a5474d174', 'e2c9b3ca88ca48948d2d65c126972c47', '123456789101213141516171819203', '9', '活动-->商品，活动结束还库存，活动剩余=9,用户多抢=0', '0000-00-00 00:00:00');
INSERT INTO `activity_goods_inventory_log` VALUES ('1f192e15a9824a90966148ada48d9a68', 'e2c9b3ca88ca48948d2d65c126972c47', '123456789101213141516171819203', '9', '活动-->商品，活动结束还库存，活动剩余=9,用户多抢=0', '0000-00-00 00:00:00');
INSERT INTO `activity_goods_inventory_log` VALUES ('201946ebc93d40bf95a70b390c34bb7b', 'e2c9b3ca88ca48948d2d65c126972c47', '123456789101213141516171819203', '8', '活动-->商品，活动结束还库存，活动剩余=8,用户多抢=0', '0000-00-00 00:00:00');
INSERT INTO `activity_goods_inventory_log` VALUES ('23ee6892f1504b5681662cd191d9bd5d', 'a0397652140c40deacba69cbeabfe973', '123456789101213141516171819203', '10', '活动-->商品，活动结束还库存，活动剩余=10,用户多抢=0', '0000-00-00 00:00:00');
INSERT INTO `activity_goods_inventory_log` VALUES ('394fc71c0d1142479125d1b4777aec1b', 'e2c9b3ca88ca48948d2d65c126972c47', '123456789101213141516171819203', '10', '活动-->商品，活动结束还库存，活动剩余=10,用户多抢=0', '0000-00-00 00:00:00');
INSERT INTO `activity_goods_inventory_log` VALUES ('475488d1bda741e89cfc2b8852102930', 'a0397652140c40deacba69cbeabfe973', '123456789101213141516171819203', '9', '活动-->商品，活动结束还库存，活动剩余=9,用户多抢=0', '2017-06-27 17:48:35');
INSERT INTO `activity_goods_inventory_log` VALUES ('6c3b83f7b07a433e9d86c5fa2daafdf3', 'e2c9b3ca88ca48948d2d65c126972c47', '123456789101213141516171819203', '9', '活动-->商品，活动结束还库存，活动剩余=9,用户多抢=0', '0000-00-00 00:00:00');
INSERT INTO `activity_goods_inventory_log` VALUES ('816b1bebd22f411895d9a87220e64fbc', 'e2c9b3ca88ca48948d2d65c126972c47', '123456789101213141516171819203', '-10', '商品-->活动', null);
INSERT INTO `activity_goods_inventory_log` VALUES ('8a0cd9e2adab4e7bb3ce255d51bde0d7', 'e2c9b3ca88ca48948d2d65c126972c47', '123456789101213141516171819203', '10', '活动-->商品', '0000-00-00 00:00:00');
INSERT INTO `activity_goods_inventory_log` VALUES ('adb8f4abd8fc4beba2a6c0a1ea8db70e', 'e2c9b3ca88ca48948d2d65c126972c47', '123456789101213141516171819203', '9', '活动-->商品，活动结束还库存，活动剩余=9,用户多抢=0', '0000-00-00 00:00:00');
INSERT INTO `activity_goods_inventory_log` VALUES ('c3c6dfa9d9cd472d9af47fab9afd2e52', 'e2c9b3ca88ca48948d2d65c126972c47', '123456789101213141516171819203', '9', '活动-->商品，活动结束还库存，活动剩余=9,用户多抢=0', '0000-00-00 00:00:00');
INSERT INTO `activity_goods_inventory_log` VALUES ('c5839ca441ec41bba5706cace0eef46d', 'e2c9b3ca88ca48948d2d65c126972c47', '123456789101213141516171819203', '10', '活动-->商品，活动结束还库存，活动剩余=10,用户多抢=0', '0000-00-00 00:00:00');
INSERT INTO `activity_goods_inventory_log` VALUES ('e4973bf8d14a448a987036ba94208424', 'e2c9b3ca88ca48948d2d65c126972c47', '123456789101213141516171819203', '-10', '商品-->活动', '0000-00-00 00:00:00');
INSERT INTO `activity_goods_inventory_log` VALUES ('f7647b2757294a36bd48ffa7c4df8eb2', 'e2c9b3ca88ca48948d2d65c126972c47', '123456789101213141516171819203', '10', '活动-->商品', null);

-- ----------------------------
-- Table structure for `activity_order_relation`
-- ----------------------------
DROP TABLE IF EXISTS `activity_order_relation`;
CREATE TABLE `activity_order_relation` (
  `activity_id` varchar(32) NOT NULL COMMENT '秒杀活动ID',
  `order_id` varchar(32) NOT NULL COMMENT '订单ID',
  PRIMARY KEY (`activity_id`,`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='活动订单关联表';

-- ----------------------------
-- Records of activity_order_relation
-- ----------------------------
INSERT INTO `activity_order_relation` VALUES ('a0397652140c40deacba69cbeabfe973', '92657adb76b2472c931b9fc516faa836');

-- ----------------------------
-- Table structure for `goods`
-- ----------------------------
DROP TABLE IF EXISTS `goods`;
CREATE TABLE `goods` (
  `goods_id` varchar(32) NOT NULL COMMENT '商品ID',
  `goods_name` varchar(512) NOT NULL COMMENT '商品名称',
  `goods_price` int(11) NOT NULL COMMENT '商品价格',
  `goods_inventory` int(11) NOT NULL COMMENT '商品库存',
  PRIMARY KEY (`goods_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品信息表';

-- ----------------------------
-- Records of goods
-- ----------------------------
INSERT INTO `goods` VALUES ('123456789101213141516171819203', 'rio275', '10000', '18');

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='秒杀任务展示图片信息表';

-- ----------------------------
-- Records of img
-- ----------------------------

-- ----------------------------
-- Table structure for `orders`
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
  `order_id` varchar(32) NOT NULL COMMENT '主键ID',
  `user_id` varchar(32) NOT NULL,
  `goods_id` varchar(32) NOT NULL,
  `goods_number` int(11) NOT NULL,
  `status` varchar(2) NOT NULL COMMENT '订单状态，0-创建，1-已支付，2-过期，9-删除',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单基本信息表';

-- ----------------------------
-- Records of orders
-- ----------------------------
INSERT INTO `orders` VALUES ('6ec34d00e44045a999060799d48b29ee', '622b3431c58248fa83a35671095552b1', '123456789101213141516171819203', '1', '0', '2017-06-27 17:29:08');
INSERT INTO `orders` VALUES ('92657adb76b2472c931b9fc516faa836', '622b3431c58248fa83a35671095552b1', '123456789101213141516171819203', '1', '0', '2017-06-27 17:48:33');
INSERT INTO `orders` VALUES ('de62d7e5f74149d282bc175853d1bb40', '622b3431c58248fa83a35671095552b1', '123456789101213141516171819203', '1', '0', '2017-06-27 17:44:50');

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户秒杀成功记录表';

-- ----------------------------
-- Records of success_log
-- ----------------------------
INSERT INTO `success_log` VALUES ('0413386b3fac496488966a0155829d10', '622b3431c58248fa83a35671095552b1', 'a0397652140c40deacba69cbeabfe973', '1', '2017-06-27 16:41:33');
INSERT INTO `success_log` VALUES ('08dec41a4508484b84b9a2b63f9caf93', '622b3431c58248fa83a35671095552b1', 'e2c9b3ca88ca48948d2d65c126972c47', '1', '2017-06-21 23:07:38');
INSERT INTO `success_log` VALUES ('0a86e872973446018b075bf2dde8e413', '622b3431c58248fa83a35671095552b1', 'a0397652140c40deacba69cbeabfe973', '1', '2017-06-27 17:29:08');
INSERT INTO `success_log` VALUES ('166740ef47fc4342bbae31f1caf63b4a', '622b3431c58248fa83a35671095552b1', 'e2c9b3ca88ca48948d2d65c126972c47', '1', '2017-06-21 23:03:37');
INSERT INTO `success_log` VALUES ('23a9d6c65057485389bdb19464df622c', '622b3431c58248fa83a35671095552b1', 'a0397652140c40deacba69cbeabfe973', '1', '2017-06-27 16:20:44');
INSERT INTO `success_log` VALUES ('654993a8961a49b38b78511605feab6d', '622b3431c58248fa83a35671095552b1', 'a0397652140c40deacba69cbeabfe973', '1', '2017-06-27 09:41:59');
INSERT INTO `success_log` VALUES ('86676bd2a2dd4cd6b5fc99644c9006f7', '622b3431c58248fa83a35671095552b1', 'a0397652140c40deacba69cbeabfe973', '1', '2017-06-27 10:10:45');
INSERT INTO `success_log` VALUES ('8dd236775f824cb8b5fe25a7063a6d83', '622b3431c58248fa83a35671095552b1', 'a0397652140c40deacba69cbeabfe973', '1', '2017-06-27 16:36:34');
INSERT INTO `success_log` VALUES ('c6cc2c7579874d5db52a96ded34470ef', '622b3431c58248fa83a35671095552b1', 'a0397652140c40deacba69cbeabfe973', '1', '2017-06-27 17:44:50');
INSERT INTO `success_log` VALUES ('cc507b08528a4a1aa082595fa2ddc59a', '622b3431c58248fa83a35671095552b1', 'a0397652140c40deacba69cbeabfe973', '1', '2017-06-27 09:57:39');
INSERT INTO `success_log` VALUES ('f00a91df673f4d2f8753c02ecc95b69b', '622b3431c58248fa83a35671095552b1', 'a0397652140c40deacba69cbeabfe973', '1', '2017-06-27 17:48:33');
INSERT INTO `success_log` VALUES ('sjdf', 'asdfaj', 'jsdahfk', '10', '0000-00-00 00:00:00');

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户信息表';

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('622b3431c58248fa83a35671095552b1', 'ygy', '123456');
