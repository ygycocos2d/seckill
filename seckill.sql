/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50540
Source Host           : 127.0.0.1:3306
Source Database       : seckill

Target Server Type    : MYSQL
Target Server Version : 50540
File Encoding         : 65001

Date: 2017-05-17 21:42:21
*/

SET FOREIGN_KEY_CHECKS=0;

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

-- ----------------------------
-- Table structure for `img`
-- ----------------------------
DROP TABLE IF EXISTS `img`;
CREATE TABLE `img` (
  `img_id` varchar(32) NOT NULL COMMENT '秒杀商品展示图ID',
  `task_Id` varchar(32) NOT NULL COMMENT '秒杀任务ID',
  `img_url` varchar(256) NOT NULL COMMENT '展示图片url',
  PRIMARY KEY (`img_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='秒杀任务展示图片信息表';

-- ----------------------------
-- Records of img
-- ----------------------------

-- ----------------------------
-- Table structure for `success_log`
-- ----------------------------
DROP TABLE IF EXISTS `success_log`;
CREATE TABLE `success_log` (
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `task_id` varchar(32) NOT NULL COMMENT '秒杀任务ID',
  `status` char(1) NOT NULL COMMENT '秒杀商品发放状态',
  PRIMARY KEY (`user_id`,`task_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='用户秒杀成功记录表';

-- ----------------------------
-- Records of success_log
-- ----------------------------

-- ----------------------------
-- Table structure for `task`
-- ----------------------------
DROP TABLE IF EXISTS `task`;
CREATE TABLE `task` (
  `task_id` varchar(32) NOT NULL COMMENT '秒杀任务ID',
  `task_gid` varchar(32) NOT NULL COMMENT '秒杀任务组ID',
  `task_startTime` datetime NOT NULL COMMENT '秒杀任务开始时间',
  `task_endTime` datetime NOT NULL COMMENT '秒杀任务结束时间',
  `task_goodsId` varchar(32) NOT NULL COMMENT '秒杀商品ID',
  `task_goodsNum` int(11) NOT NULL COMMENT '秒杀商品数量',
  `task_status` char(1) NOT NULL COMMENT '秒杀任务状态',
  `task_appNum` int(11) NOT NULL COMMENT '如果使用集群，部署的应用数',
  `task_NumInApp` varchar(512) NOT NULL COMMENT '当使用集群时，每个应用处理的秒杀商品数',
  `task_goodsPrice` int(11) NOT NULL COMMENT '秒杀商品的秒伤价',
  `task_describt` varchar(1024) DEFAULT NULL COMMENT '秒杀描述',
  PRIMARY KEY (`task_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='秒杀任务信息表';

-- ----------------------------
-- Records of task
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
