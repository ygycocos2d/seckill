/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50540
Source Host           : localhost:3306
Source Database       : seckill

Target Server Type    : MYSQL
Target Server Version : 50540
File Encoding         : 65001

Date: 2017-07-13 11:43:02
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `activity`
-- ----------------------------
DROP TABLE IF EXISTS `activity`;
CREATE TABLE `activity` (
  `activity_id` varchar(50) NOT NULL COMMENT '秒杀任务ID',
  `group_id` varchar(50) NOT NULL COMMENT '秒杀任务组ID',
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
INSERT INTO `activity` VALUES ('a0397652140c40deacba69cbeabfe973', '52f0fbbc14ab4e45a4fe58b8854ce11a', '2017-07-11 20:45:00', '2017-07-11 20:46:30', '123456789101213141516171819203', '10', '10', '1', '120', '0', '天下武功唯快不破');
INSERT INTO `activity` VALUES ('e2c9b3ca88ca48948d2d65c126972c47', 'd215349fa060425c8198bbc40f65d597', '2017-06-24 12:59:00', '2017-06-24 11:07:30', '123456789101213141516171819203', '10', '10', '1', '6000', '1', '天下武功唯快不破');

-- ----------------------------
-- Table structure for `activity_goods_inventory_log`
-- ----------------------------
DROP TABLE IF EXISTS `activity_goods_inventory_log`;
CREATE TABLE `activity_goods_inventory_log` (
  `id` varchar(50) NOT NULL COMMENT '主键ID',
  `activity_id` varchar(50) NOT NULL COMMENT '秒杀活动id',
  `goods_id` varchar(50) NOT NULL COMMENT '商品id',
  `goods_inventory` int(11) NOT NULL COMMENT '负数：秒杀活动从商品中减库存，正数：秒杀活动给商品还库存',
  `describt` varchar(256) DEFAULT NULL COMMENT '商品库存去向简单说明',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='活动减还库存记录表';

-- ----------------------------
-- Records of activity_goods_inventory_log
-- ----------------------------
INSERT INTO `activity_goods_inventory_log` VALUES ('287abf1f8eb4419ab1d47604466e899b', 'a0397652140c40deacba69cbeabfe973', '123456789101213141516171819203', '30', '活动-->商品,活动订单超时未支付自动取消', '2017-07-11 20:30:30');

-- ----------------------------
-- Table structure for `activity_order_relation`
-- ----------------------------
DROP TABLE IF EXISTS `activity_order_relation`;
CREATE TABLE `activity_order_relation` (
  `activity_id` varchar(50) NOT NULL COMMENT '秒杀活动ID',
  `order_id` varchar(50) NOT NULL COMMENT '订单ID',
  PRIMARY KEY (`activity_id`,`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='活动订单关联表';

-- ----------------------------
-- Records of activity_order_relation
-- ----------------------------
INSERT INTO `activity_order_relation` VALUES ('a0397652140c40deacba69cbeabfe973', '0a2d23d128884ba9bf2fa20362525912');
INSERT INTO `activity_order_relation` VALUES ('a0397652140c40deacba69cbeabfe973', '0b4e9f9ef74e4cde9e23d92888b79a27');
INSERT INTO `activity_order_relation` VALUES ('a0397652140c40deacba69cbeabfe973', '15e4c36ed4eb46718f23e9cc8ff3f8b2');
INSERT INTO `activity_order_relation` VALUES ('a0397652140c40deacba69cbeabfe973', '1753276a7afa4debac42cb1e90c2fdac');
INSERT INTO `activity_order_relation` VALUES ('a0397652140c40deacba69cbeabfe973', '18524603a354469da841a4f404710e0a');
INSERT INTO `activity_order_relation` VALUES ('a0397652140c40deacba69cbeabfe973', '1955430b83394fb4bb8b5f3b9307449b');
INSERT INTO `activity_order_relation` VALUES ('a0397652140c40deacba69cbeabfe973', '196e1f1b06474f2cb4d2e80a1db16832');
INSERT INTO `activity_order_relation` VALUES ('a0397652140c40deacba69cbeabfe973', '1e05a1daad5c4472bed996aaea12799d');
INSERT INTO `activity_order_relation` VALUES ('a0397652140c40deacba69cbeabfe973', '26df5abd7b1d4ea3ac94425f7c5f0edb');
INSERT INTO `activity_order_relation` VALUES ('a0397652140c40deacba69cbeabfe973', '31de3f4455c64952bd046ab2ba1f8175');
INSERT INTO `activity_order_relation` VALUES ('a0397652140c40deacba69cbeabfe973', '3483424274d04cf0b5b872c71da8688c');
INSERT INTO `activity_order_relation` VALUES ('a0397652140c40deacba69cbeabfe973', '34c63686093c4f87af84b0108971c75f');
INSERT INTO `activity_order_relation` VALUES ('a0397652140c40deacba69cbeabfe973', '34deb69e23164ba8b4911224b3fa6177');
INSERT INTO `activity_order_relation` VALUES ('a0397652140c40deacba69cbeabfe973', '351af24a720b49a4908250cc7f22ef69');
INSERT INTO `activity_order_relation` VALUES ('a0397652140c40deacba69cbeabfe973', '365ab8e62f3c46398572010be30c4c61');
INSERT INTO `activity_order_relation` VALUES ('a0397652140c40deacba69cbeabfe973', '3982626ff75945a6b936575ecf0657b2');
INSERT INTO `activity_order_relation` VALUES ('a0397652140c40deacba69cbeabfe973', '39ba7b020f6a4f539e4d25992fd01e81');
INSERT INTO `activity_order_relation` VALUES ('a0397652140c40deacba69cbeabfe973', '3bb3b534c1714140bd88a62b68d5e424');
INSERT INTO `activity_order_relation` VALUES ('a0397652140c40deacba69cbeabfe973', '3e02e26e57c74bdc9d166e2b1bafc2f8');
INSERT INTO `activity_order_relation` VALUES ('a0397652140c40deacba69cbeabfe973', '435df40844d049958becabb9bf908c82');
INSERT INTO `activity_order_relation` VALUES ('a0397652140c40deacba69cbeabfe973', '44a01e967b2542cc8ae1a8e9dabb40e6');
INSERT INTO `activity_order_relation` VALUES ('a0397652140c40deacba69cbeabfe973', '46d839d41d5341dc935b1a32d7e64133');
INSERT INTO `activity_order_relation` VALUES ('a0397652140c40deacba69cbeabfe973', '50d6e278235a42519fc6e071f62516c1');
INSERT INTO `activity_order_relation` VALUES ('a0397652140c40deacba69cbeabfe973', '566da74c41734235bd0d0506cb0d8ccb');
INSERT INTO `activity_order_relation` VALUES ('a0397652140c40deacba69cbeabfe973', '623c3efc5c1444e3be13ce38ccc135ff');
INSERT INTO `activity_order_relation` VALUES ('a0397652140c40deacba69cbeabfe973', '6ea2236877b54939a66dd7b7ee351ca0');
INSERT INTO `activity_order_relation` VALUES ('a0397652140c40deacba69cbeabfe973', '70228b6b262c44e496a25c9ef68ec58a');
INSERT INTO `activity_order_relation` VALUES ('a0397652140c40deacba69cbeabfe973', '78d53d20e4664a0486365976afeec8f2');
INSERT INTO `activity_order_relation` VALUES ('a0397652140c40deacba69cbeabfe973', '840930f8fd694a7995dbbfb38c3ae6f0');
INSERT INTO `activity_order_relation` VALUES ('a0397652140c40deacba69cbeabfe973', '8520bb1b06a940fc91382a2bea6d11e3');
INSERT INTO `activity_order_relation` VALUES ('a0397652140c40deacba69cbeabfe973', '864d872ffb2549059d61d0a2b2295171');
INSERT INTO `activity_order_relation` VALUES ('a0397652140c40deacba69cbeabfe973', '87cc1cbfebd24dbfa30ebc2a0c9e9451');
INSERT INTO `activity_order_relation` VALUES ('a0397652140c40deacba69cbeabfe973', '8a68292c4d12470eae5fbab56d95c88f');
INSERT INTO `activity_order_relation` VALUES ('a0397652140c40deacba69cbeabfe973', '8efedda691764232b632de227bac42e0');
INSERT INTO `activity_order_relation` VALUES ('a0397652140c40deacba69cbeabfe973', '90eba25f931b41fda75e7418dcf155eb');
INSERT INTO `activity_order_relation` VALUES ('a0397652140c40deacba69cbeabfe973', '9cd658683dd9428ebfde7722ce42f4d7');
INSERT INTO `activity_order_relation` VALUES ('a0397652140c40deacba69cbeabfe973', '9ed5e5b5c3fd46ae8626bf95d8d3b883');
INSERT INTO `activity_order_relation` VALUES ('a0397652140c40deacba69cbeabfe973', 'a6bb1140484346ad83e75dc258bdbf01');
INSERT INTO `activity_order_relation` VALUES ('a0397652140c40deacba69cbeabfe973', 'a6d77a7f1d484e509ae2b2749f54a9b5');
INSERT INTO `activity_order_relation` VALUES ('a0397652140c40deacba69cbeabfe973', 'adde852f2bbd4dd98c69989a369d72f1');
INSERT INTO `activity_order_relation` VALUES ('a0397652140c40deacba69cbeabfe973', 'ae61e55343a0457697e4470eb6ef0b24');
INSERT INTO `activity_order_relation` VALUES ('a0397652140c40deacba69cbeabfe973', 'b200658b49a94841b253bd02c091b0b0');
INSERT INTO `activity_order_relation` VALUES ('a0397652140c40deacba69cbeabfe973', 'b7b82a4df29143a0bc0e748f909bb5ae');
INSERT INTO `activity_order_relation` VALUES ('a0397652140c40deacba69cbeabfe973', 'bb7af882743e44ada85abed6ba8fa891');
INSERT INTO `activity_order_relation` VALUES ('a0397652140c40deacba69cbeabfe973', 'bd24157327924b768a080cbf6e028535');
INSERT INTO `activity_order_relation` VALUES ('a0397652140c40deacba69cbeabfe973', 'bee15528f6a54b128e681067d30f612e');
INSERT INTO `activity_order_relation` VALUES ('a0397652140c40deacba69cbeabfe973', 'c244c68b72734a25a1f840787f34cb64');
INSERT INTO `activity_order_relation` VALUES ('a0397652140c40deacba69cbeabfe973', 'c24fdbfd26d64b57b2f14b7e6a06fe51');
INSERT INTO `activity_order_relation` VALUES ('a0397652140c40deacba69cbeabfe973', 'c89f85ef126348338cab33063f4db81a');
INSERT INTO `activity_order_relation` VALUES ('a0397652140c40deacba69cbeabfe973', 'cf590953fbf6443d8cbcf2bea4d29eb9');
INSERT INTO `activity_order_relation` VALUES ('a0397652140c40deacba69cbeabfe973', 'cf95739a8e8b4ccf98901d97c7edffe9');
INSERT INTO `activity_order_relation` VALUES ('a0397652140c40deacba69cbeabfe973', 'd2426834758d460e9e29057f86c85e44');
INSERT INTO `activity_order_relation` VALUES ('a0397652140c40deacba69cbeabfe973', 'd3dca3dad2a244d29a1bf700507d5915');
INSERT INTO `activity_order_relation` VALUES ('a0397652140c40deacba69cbeabfe973', 'd9c5e0a473e846eba509f432ed104c43');
INSERT INTO `activity_order_relation` VALUES ('a0397652140c40deacba69cbeabfe973', 'ee29ae3e1bb44b668f9abc9947503a83');
INSERT INTO `activity_order_relation` VALUES ('a0397652140c40deacba69cbeabfe973', 'ef896641c5dd4bac9e6069c0fe279dfd');
INSERT INTO `activity_order_relation` VALUES ('a0397652140c40deacba69cbeabfe973', 'f363e4c5d2874004b364616fa98507b5');
INSERT INTO `activity_order_relation` VALUES ('a0397652140c40deacba69cbeabfe973', 'f3907bf224a442a29c33b070d2703521');
INSERT INTO `activity_order_relation` VALUES ('a0397652140c40deacba69cbeabfe973', 'f6bf979213874c649a77863ad0179312');
INSERT INTO `activity_order_relation` VALUES ('a0397652140c40deacba69cbeabfe973', 'f6ce4e6a00bf425d9b4a01ecf0002b4e');

-- ----------------------------
-- Table structure for `goods`
-- ----------------------------
DROP TABLE IF EXISTS `goods`;
CREATE TABLE `goods` (
  `goods_id` varchar(50) NOT NULL COMMENT '商品ID',
  `goods_name` varchar(512) NOT NULL COMMENT '商品名称',
  `goods_price` int(11) NOT NULL COMMENT '商品价格',
  `goods_inventory` int(11) NOT NULL COMMENT '商品库存',
  PRIMARY KEY (`goods_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品信息表';

-- ----------------------------
-- Records of goods
-- ----------------------------
INSERT INTO `goods` VALUES ('123456789101213141516171819203', 'rio275', '10000', '69');

-- ----------------------------
-- Table structure for `img`
-- ----------------------------
DROP TABLE IF EXISTS `img`;
CREATE TABLE `img` (
  `img_id` varchar(50) NOT NULL COMMENT '秒杀商品展示图ID',
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
  `order_id` varchar(50) NOT NULL COMMENT '主键ID',
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
INSERT INTO `orders` VALUES ('0a2d23d128884ba9bf2fa20362525912', '20797677e8df4c6190e5dac8ac397dac', '123456789101213141516171819203', '1', '2', '2017-07-11 20:28:30');
INSERT INTO `orders` VALUES ('0b4e9f9ef74e4cde9e23d92888b79a27', 'dcc899ffacfd45e2ad465ca4dd9e4a9e', '123456789101213141516171819203', '1', '2', '2017-07-11 20:28:30');
INSERT INTO `orders` VALUES ('15e4c36ed4eb46718f23e9cc8ff3f8b2', 'f989cc8f635c4fdbb56e9d0a0a0e5a88', '123456789101213141516171819203', '1', '2', '2017-07-11 20:28:30');
INSERT INTO `orders` VALUES ('1753276a7afa4debac42cb1e90c2fdac', '988d231a13dd48f49c1a1166ae9092a1', '123456789101213141516171819203', '1', '0', '2017-07-11 20:46:30');
INSERT INTO `orders` VALUES ('18524603a354469da841a4f404710e0a', 'f989cc8f635c4fdbb56e9d0a0a0e5a88', '123456789101213141516171819203', '1', '0', '2017-07-11 20:46:30');
INSERT INTO `orders` VALUES ('1955430b83394fb4bb8b5f3b9307449b', '6f7d915c69f340ef85d10cc02fae3951', '123456789101213141516171819203', '1', '2', '2017-07-11 20:28:30');
INSERT INTO `orders` VALUES ('196e1f1b06474f2cb4d2e80a1db16832', 'a10798fb165049429b2da218f79834fb', '123456789101213141516171819203', '1', '2', '2017-07-11 20:28:30');
INSERT INTO `orders` VALUES ('1e05a1daad5c4472bed996aaea12799d', '590c78c66ebb44658bd863bd34a602ce', '123456789101213141516171819203', '1', '0', '2017-07-11 20:46:30');
INSERT INTO `orders` VALUES ('26df5abd7b1d4ea3ac94425f7c5f0edb', '5f6a14cc670042b588dc48be8051197e', '123456789101213141516171819203', '1', '2', '2017-07-11 20:28:30');
INSERT INTO `orders` VALUES ('31de3f4455c64952bd046ab2ba1f8175', 'acf15171ab6b4eb0a16679abb3b2b607', '123456789101213141516171819203', '1', '2', '2017-07-11 20:28:30');
INSERT INTO `orders` VALUES ('3483424274d04cf0b5b872c71da8688c', '1d6e8c026dfd4acc87edba26813914c5', '123456789101213141516171819203', '1', '0', '2017-07-11 20:46:30');
INSERT INTO `orders` VALUES ('34c63686093c4f87af84b0108971c75f', 'b0c804e5709f4526abdc50a9901ab8f5', '123456789101213141516171819203', '1', '0', '2017-07-11 20:46:30');
INSERT INTO `orders` VALUES ('34deb69e23164ba8b4911224b3fa6177', '28894a5e7080484abadb35d9083c222a', '123456789101213141516171819203', '1', '0', '2017-07-11 20:46:30');
INSERT INTO `orders` VALUES ('351af24a720b49a4908250cc7f22ef69', '5d2382c1f85447278b3983ee5b1c835a', '123456789101213141516171819203', '1', '0', '2017-07-11 20:46:30');
INSERT INTO `orders` VALUES ('365ab8e62f3c46398572010be30c4c61', '3a7dd735c6be4f10b522a6d639f1446f', '123456789101213141516171819203', '1', '0', '2017-07-11 20:46:30');
INSERT INTO `orders` VALUES ('3982626ff75945a6b936575ecf0657b2', '80716fba38ff4e4eac0a53de137b714b', '123456789101213141516171819203', '1', '2', '2017-07-11 20:28:30');
INSERT INTO `orders` VALUES ('39ba7b020f6a4f539e4d25992fd01e81', '057c973d1b8f47f3a0bc4817192aadda', '123456789101213141516171819203', '1', '2', '2017-07-11 20:28:30');
INSERT INTO `orders` VALUES ('3bb3b534c1714140bd88a62b68d5e424', 'b2b9b65141e8450d9fc48c9837655e39', '123456789101213141516171819203', '1', '2', '2017-07-11 20:28:30');
INSERT INTO `orders` VALUES ('3e02e26e57c74bdc9d166e2b1bafc2f8', 'e29a49f38f15461e8ba953209baf9d11', '123456789101213141516171819203', '1', '0', '2017-07-11 20:46:30');
INSERT INTO `orders` VALUES ('435df40844d049958becabb9bf908c82', 'e2b6f79408d44624bda2f02a83517ccb', '123456789101213141516171819203', '1', '0', '2017-07-11 20:46:30');
INSERT INTO `orders` VALUES ('44a01e967b2542cc8ae1a8e9dabb40e6', '4ce1600ce2af4c29bd3d09e8b7972bb4', '123456789101213141516171819203', '1', '0', '2017-07-11 20:46:30');
INSERT INTO `orders` VALUES ('46d839d41d5341dc935b1a32d7e64133', '43e669c6d9844e2fb30e5e0633fdd4c3', '123456789101213141516171819203', '1', '2', '2017-07-11 20:28:30');
INSERT INTO `orders` VALUES ('50d6e278235a42519fc6e071f62516c1', '5ef9ac2b5bb74faaa11d06cda27c40cc', '123456789101213141516171819203', '1', '2', '2017-07-11 20:28:30');
INSERT INTO `orders` VALUES ('566da74c41734235bd0d0506cb0d8ccb', '4e93fa307ff945fda195e240a1c2838f', '123456789101213141516171819203', '1', '2', '2017-07-11 20:28:30');
INSERT INTO `orders` VALUES ('623c3efc5c1444e3be13ce38ccc135ff', '04f123a50c2845e198de627cb972b7a4', '123456789101213141516171819203', '1', '2', '2017-07-11 20:28:30');
INSERT INTO `orders` VALUES ('6ea2236877b54939a66dd7b7ee351ca0', '20a67e2c88954d289c9face6f1ab0541', '123456789101213141516171819203', '1', '0', '2017-07-11 20:46:30');
INSERT INTO `orders` VALUES ('70228b6b262c44e496a25c9ef68ec58a', '8fc0bdea33484cbc99b0acae2b1f05f4', '123456789101213141516171819203', '1', '0', '2017-07-11 20:46:30');
INSERT INTO `orders` VALUES ('78d53d20e4664a0486365976afeec8f2', '7caac90e0a38450a8e1113264438005c', '123456789101213141516171819203', '1', '0', '2017-07-11 20:46:30');
INSERT INTO `orders` VALUES ('840930f8fd694a7995dbbfb38c3ae6f0', '8e361be399cc4dfebd4d5ed575b582a0', '123456789101213141516171819203', '1', '0', '2017-07-11 20:46:30');
INSERT INTO `orders` VALUES ('8520bb1b06a940fc91382a2bea6d11e3', '76487f0ce53d4578a70f6f552609dc90', '123456789101213141516171819203', '1', '2', '2017-07-11 20:28:30');
INSERT INTO `orders` VALUES ('864d872ffb2549059d61d0a2b2295171', '8fc0bdea33484cbc99b0acae2b1f05f4', '123456789101213141516171819203', '1', '2', '2017-07-11 20:28:30');
INSERT INTO `orders` VALUES ('87cc1cbfebd24dbfa30ebc2a0c9e9451', '09e58e0e5e0d4b908bcf5fa8c3f30b0e', '123456789101213141516171819203', '1', '2', '2017-07-11 20:28:30');
INSERT INTO `orders` VALUES ('8a68292c4d12470eae5fbab56d95c88f', '5f6a14cc670042b588dc48be8051197e', '123456789101213141516171819203', '1', '0', '2017-07-11 20:46:30');
INSERT INTO `orders` VALUES ('8efedda691764232b632de227bac42e0', '1f1c5a9bb0e74ad591f7ebe37e72ebc8', '123456789101213141516171819203', '1', '2', '2017-07-11 20:28:30');
INSERT INTO `orders` VALUES ('90eba25f931b41fda75e7418dcf155eb', '989d91a7cdf2440789ea3456890e9bef', '123456789101213141516171819203', '1', '2', '2017-07-11 20:28:30');
INSERT INTO `orders` VALUES ('9cd658683dd9428ebfde7722ce42f4d7', 'fbb3ac43710c42bcbe5bce9c32966a7b', '123456789101213141516171819203', '1', '2', '2017-07-11 20:28:30');
INSERT INTO `orders` VALUES ('9ed5e5b5c3fd46ae8626bf95d8d3b883', '382ce7e39c6a4870a54cd3ff067baf61', '123456789101213141516171819203', '1', '2', '2017-07-11 20:28:30');
INSERT INTO `orders` VALUES ('a6bb1140484346ad83e75dc258bdbf01', '366f486d19ee419d90859c2b3567a3df', '123456789101213141516171819203', '1', '2', '2017-07-11 20:28:30');
INSERT INTO `orders` VALUES ('a6d77a7f1d484e509ae2b2749f54a9b5', '0429d0c8345d41ae8961f9a0c667b0fa', '123456789101213141516171819203', '1', '0', '2017-07-11 20:46:30');
INSERT INTO `orders` VALUES ('adde852f2bbd4dd98c69989a369d72f1', 'dcc899ffacfd45e2ad465ca4dd9e4a9e', '123456789101213141516171819203', '1', '0', '2017-07-11 20:46:30');
INSERT INTO `orders` VALUES ('ae61e55343a0457697e4470eb6ef0b24', 'c4f6e7476ffe4dc6924b05492f3168d3', '123456789101213141516171819203', '1', '0', '2017-07-11 20:46:30');
INSERT INTO `orders` VALUES ('b200658b49a94841b253bd02c091b0b0', '8ac11caf1aa74bb2aa016be12e094330', '123456789101213141516171819203', '1', '0', '2017-07-11 20:46:30');
INSERT INTO `orders` VALUES ('b7b82a4df29143a0bc0e748f909bb5ae', '3fd67e80b19948c2937813fa6a14fc6b', '123456789101213141516171819203', '1', '0', '2017-07-11 20:46:30');
INSERT INTO `orders` VALUES ('bb7af882743e44ada85abed6ba8fa891', '988d231a13dd48f49c1a1166ae9092a1', '123456789101213141516171819203', '1', '2', '2017-07-11 20:28:30');
INSERT INTO `orders` VALUES ('bd24157327924b768a080cbf6e028535', '8a76b3cdfa6b4c76a6463570c101c6b2', '123456789101213141516171819203', '1', '0', '2017-07-11 20:46:30');
INSERT INTO `orders` VALUES ('bee15528f6a54b128e681067d30f612e', 'a49142fe26744bf9b7d274dbfd55ef3d', '123456789101213141516171819203', '1', '0', '2017-07-11 20:46:30');
INSERT INTO `orders` VALUES ('c244c68b72734a25a1f840787f34cb64', '808a97a2de2e494a92622c518763a2b9', '123456789101213141516171819203', '1', '0', '2017-07-11 20:46:30');
INSERT INTO `orders` VALUES ('c24fdbfd26d64b57b2f14b7e6a06fe51', '0268edb468ae4ae6ac76b6aeeb35f44c', '123456789101213141516171819203', '1', '2', '2017-07-11 20:28:30');
INSERT INTO `orders` VALUES ('c89f85ef126348338cab33063f4db81a', 'b1c7e5b00758419aa3816dab26059e0b', '123456789101213141516171819203', '1', '2', '2017-07-11 20:28:30');
INSERT INTO `orders` VALUES ('cf590953fbf6443d8cbcf2bea4d29eb9', 'd8656e32bbdd47ca9a77333f665dd5a9', '123456789101213141516171819203', '1', '0', '2017-07-11 20:46:30');
INSERT INTO `orders` VALUES ('cf95739a8e8b4ccf98901d97c7edffe9', '31d600ba84ba4287b7db06101fe2fac0', '123456789101213141516171819203', '1', '0', '2017-07-11 20:46:30');
INSERT INTO `orders` VALUES ('d2426834758d460e9e29057f86c85e44', 'b9820222bc454937b5a0e5cbeb202cc5', '123456789101213141516171819203', '1', '0', '2017-07-11 20:46:30');
INSERT INTO `orders` VALUES ('d3dca3dad2a244d29a1bf700507d5915', '989d91a7cdf2440789ea3456890e9bef', '123456789101213141516171819203', '1', '0', '2017-07-11 20:46:30');
INSERT INTO `orders` VALUES ('d9c5e0a473e846eba509f432ed104c43', '0268edb468ae4ae6ac76b6aeeb35f44c', '123456789101213141516171819203', '1', '0', '2017-07-11 20:46:30');
INSERT INTO `orders` VALUES ('ee29ae3e1bb44b668f9abc9947503a83', '9c87da4c9b3a4969b8c33e7d0cd00168', '123456789101213141516171819203', '1', '2', '2017-07-11 20:28:30');
INSERT INTO `orders` VALUES ('ef896641c5dd4bac9e6069c0fe279dfd', '2a46306a8c584d7f9e8b8ecf8ba7ddec', '123456789101213141516171819203', '1', '2', '2017-07-11 20:28:30');
INSERT INTO `orders` VALUES ('f363e4c5d2874004b364616fa98507b5', '3113b2d5823247feafcae8e597a4b400', '123456789101213141516171819203', '1', '2', '2017-07-11 20:28:30');
INSERT INTO `orders` VALUES ('f3907bf224a442a29c33b070d2703521', '681ba2578af549bcba6cb27a6948c809', '123456789101213141516171819203', '1', '2', '2017-07-11 20:28:30');
INSERT INTO `orders` VALUES ('f6bf979213874c649a77863ad0179312', '8e361be399cc4dfebd4d5ed575b582a0', '123456789101213141516171819203', '1', '2', '2017-07-11 20:28:30');
INSERT INTO `orders` VALUES ('f6ce4e6a00bf425d9b4a01ecf0002b4e', '622b3431c58248fa83a35671095552b1', '123456789101213141516171819203', '1', '0', '2017-07-11 20:46:30');

-- ----------------------------
-- Table structure for `success_log`
-- ----------------------------
DROP TABLE IF EXISTS `success_log`;
CREATE TABLE `success_log` (
  `succlog_id` varchar(50) NOT NULL COMMENT '主键',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `activity_id` varchar(32) NOT NULL COMMENT '秒杀活动ID',
  `goods_number` int(11) NOT NULL COMMENT '实际秒杀到的商品数，有可能超过规定限制数',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`succlog_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户秒杀成功记录表';

-- ----------------------------
-- Records of success_log
-- ----------------------------
INSERT INTO `success_log` VALUES ('0174a7d0aad448479f040c3a577f7252', 'acf15171ab6b4eb0a16679abb3b2b607', 'a0397652140c40deacba69cbeabfe973', '1', '2017-07-11 20:28:30');
INSERT INTO `success_log` VALUES ('03c3e708fc2f490bb8598268fac9410a', 'd8656e32bbdd47ca9a77333f665dd5a9', 'a0397652140c40deacba69cbeabfe973', '1', '2017-07-11 20:46:30');
INSERT INTO `success_log` VALUES ('076678fd3ae7429795c5b19a14a550dc', '366f486d19ee419d90859c2b3567a3df', 'a0397652140c40deacba69cbeabfe973', '1', '2017-07-11 20:28:30');
INSERT INTO `success_log` VALUES ('13b9f2f15db94b8e8e191e1e7727f2dd', '1f1c5a9bb0e74ad591f7ebe37e72ebc8', 'a0397652140c40deacba69cbeabfe973', '1', '2017-07-11 20:28:30');
INSERT INTO `success_log` VALUES ('14029a002bc146ea980034617ee68e50', '0429d0c8345d41ae8961f9a0c667b0fa', 'a0397652140c40deacba69cbeabfe973', '1', '2017-07-11 20:46:30');
INSERT INTO `success_log` VALUES ('173e370efe8548c5a160bffce7381811', '382ce7e39c6a4870a54cd3ff067baf61', 'a0397652140c40deacba69cbeabfe973', '1', '2017-07-11 20:28:30');
INSERT INTO `success_log` VALUES ('182b87c876f04e10996d03c823963326', '3fd67e80b19948c2937813fa6a14fc6b', 'a0397652140c40deacba69cbeabfe973', '1', '2017-07-11 20:46:30');
INSERT INTO `success_log` VALUES ('1aadd43fa5bc40acaa252d83c4ea8c3a', 'f989cc8f635c4fdbb56e9d0a0a0e5a88', 'a0397652140c40deacba69cbeabfe973', '1', '2017-07-11 20:46:30');
INSERT INTO `success_log` VALUES ('25cdecc24e2d4e40955e7f4e698e1ee1', 'a10798fb165049429b2da218f79834fb', 'a0397652140c40deacba69cbeabfe973', '1', '2017-07-11 20:28:30');
INSERT INTO `success_log` VALUES ('272a1d23fc324214ba48ee586a50a95d', '80716fba38ff4e4eac0a53de137b714b', 'a0397652140c40deacba69cbeabfe973', '1', '2017-07-11 20:28:30');
INSERT INTO `success_log` VALUES ('2be41de1fd5845678ea59a39c2d31d53', '988d231a13dd48f49c1a1166ae9092a1', 'a0397652140c40deacba69cbeabfe973', '1', '2017-07-11 20:46:30');
INSERT INTO `success_log` VALUES ('33f25af84434489babb70214dd68ee04', '09e58e0e5e0d4b908bcf5fa8c3f30b0e', 'a0397652140c40deacba69cbeabfe973', '1', '2017-07-11 20:28:30');
INSERT INTO `success_log` VALUES ('34dacdd719a2424ea6f06546f0f1a7c6', 'b2b9b65141e8450d9fc48c9837655e39', 'a0397652140c40deacba69cbeabfe973', '1', '2017-07-11 20:28:30');
INSERT INTO `success_log` VALUES ('354c97ba73c944f7b3874fba1897810a', '28894a5e7080484abadb35d9083c222a', 'a0397652140c40deacba69cbeabfe973', '1', '2017-07-11 20:46:30');
INSERT INTO `success_log` VALUES ('35d40e9d717f41a59f1a71265be5c2f3', '76487f0ce53d4578a70f6f552609dc90', 'a0397652140c40deacba69cbeabfe973', '1', '2017-07-11 20:28:30');
INSERT INTO `success_log` VALUES ('360e49755e314783b236904dc4f3d054', '8fc0bdea33484cbc99b0acae2b1f05f4', 'a0397652140c40deacba69cbeabfe973', '1', '2017-07-11 20:28:30');
INSERT INTO `success_log` VALUES ('3a5d75a8c24c4a23af907f4b2d597d67', '5f6a14cc670042b588dc48be8051197e', 'a0397652140c40deacba69cbeabfe973', '1', '2017-07-11 20:28:30');
INSERT INTO `success_log` VALUES ('46ef8f8b9069404cbe5274d5d0df066a', '04f123a50c2845e198de627cb972b7a4', 'a0397652140c40deacba69cbeabfe973', '1', '2017-07-11 20:28:30');
INSERT INTO `success_log` VALUES ('4eb9c1237f7b4c399610a33e9b92f605', '8e361be399cc4dfebd4d5ed575b582a0', 'a0397652140c40deacba69cbeabfe973', '1', '2017-07-11 20:28:30');
INSERT INTO `success_log` VALUES ('4fa9c5fc11154df5841e75396d2f5f21', '622b3431c58248fa83a35671095552b1', 'a0397652140c40deacba69cbeabfe973', '1', '2017-07-11 20:46:30');
INSERT INTO `success_log` VALUES ('506fdee14f3240d3850a354bb3a9a576', '989d91a7cdf2440789ea3456890e9bef', 'a0397652140c40deacba69cbeabfe973', '1', '2017-07-11 20:28:30');
INSERT INTO `success_log` VALUES ('52d4d320667f41d7b94b135556483526', 'dcc899ffacfd45e2ad465ca4dd9e4a9e', 'a0397652140c40deacba69cbeabfe973', '1', '2017-07-11 20:28:30');
INSERT INTO `success_log` VALUES ('6b8b31d5f79943b5b1c8c00a0cfbe32f', '989d91a7cdf2440789ea3456890e9bef', 'a0397652140c40deacba69cbeabfe973', '1', '2017-07-11 20:46:30');
INSERT INTO `success_log` VALUES ('7115f0ec527443e180c7594e82128453', '20797677e8df4c6190e5dac8ac397dac', 'a0397652140c40deacba69cbeabfe973', '1', '2017-07-11 20:28:30');
INSERT INTO `success_log` VALUES ('73385e9155344448b8c71266e0d7a514', 'dcc899ffacfd45e2ad465ca4dd9e4a9e', 'a0397652140c40deacba69cbeabfe973', '1', '2017-07-11 20:46:30');
INSERT INTO `success_log` VALUES ('77505680b6e745948e116c997ad184e6', 'e29a49f38f15461e8ba953209baf9d11', 'a0397652140c40deacba69cbeabfe973', '1', '2017-07-11 20:46:30');
INSERT INTO `success_log` VALUES ('7779df41b6cb4ad5a10ac63c14a4778f', '4ce1600ce2af4c29bd3d09e8b7972bb4', 'a0397652140c40deacba69cbeabfe973', '1', '2017-07-11 20:46:30');
INSERT INTO `success_log` VALUES ('7c9d76731fac4ffdbe21b67c80f934ee', '8e361be399cc4dfebd4d5ed575b582a0', 'a0397652140c40deacba69cbeabfe973', '1', '2017-07-11 20:46:30');
INSERT INTO `success_log` VALUES ('7e18eb581bc644b58682815428e3edab', 'f989cc8f635c4fdbb56e9d0a0a0e5a88', 'a0397652140c40deacba69cbeabfe973', '1', '2017-07-11 20:28:30');
INSERT INTO `success_log` VALUES ('850ade1094f84f149112c2dbdf1ca642', '20a67e2c88954d289c9face6f1ab0541', 'a0397652140c40deacba69cbeabfe973', '1', '2017-07-11 20:46:30');
INSERT INTO `success_log` VALUES ('86d416df3fb548efb92503cc8b85a434', '988d231a13dd48f49c1a1166ae9092a1', 'a0397652140c40deacba69cbeabfe973', '1', '2017-07-11 20:28:30');
INSERT INTO `success_log` VALUES ('87677e693b2c444aad6f0cbe0b80e9af', 'b0c804e5709f4526abdc50a9901ab8f5', 'a0397652140c40deacba69cbeabfe973', '1', '2017-07-11 20:46:30');
INSERT INTO `success_log` VALUES ('88c2c617b5a6457ca4110c352d5d31b8', 'b9820222bc454937b5a0e5cbeb202cc5', 'a0397652140c40deacba69cbeabfe973', '1', '2017-07-11 20:46:30');
INSERT INTO `success_log` VALUES ('89da32ffcc234f8ead11bdee49fe915a', '3113b2d5823247feafcae8e597a4b400', 'a0397652140c40deacba69cbeabfe973', '1', '2017-07-11 20:28:30');
INSERT INTO `success_log` VALUES ('8b58397a82984babaf0d48a7c201c568', '7caac90e0a38450a8e1113264438005c', 'a0397652140c40deacba69cbeabfe973', '1', '2017-07-11 20:46:30');
INSERT INTO `success_log` VALUES ('918a11f9791f43f4b9851034050b2041', 'b1c7e5b00758419aa3816dab26059e0b', 'a0397652140c40deacba69cbeabfe973', '1', '2017-07-11 20:28:30');
INSERT INTO `success_log` VALUES ('94622fbd452a4df9b2b41c7fa2bcf0a4', '5d2382c1f85447278b3983ee5b1c835a', 'a0397652140c40deacba69cbeabfe973', '1', '2017-07-11 20:46:30');
INSERT INTO `success_log` VALUES ('97434b22fc534d47a96d49717a5c1700', '0268edb468ae4ae6ac76b6aeeb35f44c', 'a0397652140c40deacba69cbeabfe973', '1', '2017-07-11 20:28:30');
INSERT INTO `success_log` VALUES ('a0c0587381474766aa8572bf1f1268e8', '4e93fa307ff945fda195e240a1c2838f', 'a0397652140c40deacba69cbeabfe973', '1', '2017-07-11 20:28:30');
INSERT INTO `success_log` VALUES ('a58a2ce396494b5faed5c47c5e2c82a5', '8a76b3cdfa6b4c76a6463570c101c6b2', 'a0397652140c40deacba69cbeabfe973', '1', '2017-07-11 20:46:30');
INSERT INTO `success_log` VALUES ('a83a7b7a18f042cc8f33f9bd99478e98', 'e2b6f79408d44624bda2f02a83517ccb', 'a0397652140c40deacba69cbeabfe973', '1', '2017-07-11 20:46:30');
INSERT INTO `success_log` VALUES ('a87c905a87574d5ca7cedcdbc45f6349', '0268edb468ae4ae6ac76b6aeeb35f44c', 'a0397652140c40deacba69cbeabfe973', '1', '2017-07-11 20:46:30');
INSERT INTO `success_log` VALUES ('a944be693eaf4c6ca0e1694763c32b12', '2a46306a8c584d7f9e8b8ecf8ba7ddec', 'a0397652140c40deacba69cbeabfe973', '1', '2017-07-11 20:28:30');
INSERT INTO `success_log` VALUES ('ade62af5179d4b558078560b4c65d173', '590c78c66ebb44658bd863bd34a602ce', 'a0397652140c40deacba69cbeabfe973', '1', '2017-07-11 20:46:30');
INSERT INTO `success_log` VALUES ('af27e95f52d34b29801839effa271ba7', '6f7d915c69f340ef85d10cc02fae3951', 'a0397652140c40deacba69cbeabfe973', '1', '2017-07-11 20:28:30');
INSERT INTO `success_log` VALUES ('b73d169e39dc4c7fbd7ccea8f5b96d9a', '31d600ba84ba4287b7db06101fe2fac0', 'a0397652140c40deacba69cbeabfe973', '1', '2017-07-11 20:46:30');
INSERT INTO `success_log` VALUES ('b95396a28d704d469f14c7b14cfef242', '057c973d1b8f47f3a0bc4817192aadda', 'a0397652140c40deacba69cbeabfe973', '1', '2017-07-11 20:28:30');
INSERT INTO `success_log` VALUES ('b9d56628cc0c4a88b05b1a09b654fdc1', '9c87da4c9b3a4969b8c33e7d0cd00168', 'a0397652140c40deacba69cbeabfe973', '1', '2017-07-11 20:28:30');
INSERT INTO `success_log` VALUES ('c6967e21316a40c08e0edf1a0c9f0191', 'c4f6e7476ffe4dc6924b05492f3168d3', 'a0397652140c40deacba69cbeabfe973', '1', '2017-07-11 20:46:30');
INSERT INTO `success_log` VALUES ('ceac39f3449c484e9f96dd2a76397b4f', '3a7dd735c6be4f10b522a6d639f1446f', 'a0397652140c40deacba69cbeabfe973', '1', '2017-07-11 20:46:30');
INSERT INTO `success_log` VALUES ('d189ef9c05294dfab796317306eccfee', '8fc0bdea33484cbc99b0acae2b1f05f4', 'a0397652140c40deacba69cbeabfe973', '1', '2017-07-11 20:46:30');
INSERT INTO `success_log` VALUES ('d22cedc63cf0478fa23428ad87ec1ff2', '1d6e8c026dfd4acc87edba26813914c5', 'a0397652140c40deacba69cbeabfe973', '1', '2017-07-11 20:46:30');
INSERT INTO `success_log` VALUES ('d3e95bd60f954dbf8fa0c9400857d2b0', '43e669c6d9844e2fb30e5e0633fdd4c3', 'a0397652140c40deacba69cbeabfe973', '1', '2017-07-11 20:28:30');
INSERT INTO `success_log` VALUES ('da0fbb7df23e4fcfa5972af70780224b', '5f6a14cc670042b588dc48be8051197e', 'a0397652140c40deacba69cbeabfe973', '1', '2017-07-11 20:46:30');
INSERT INTO `success_log` VALUES ('da9f371fda88403fa5993c38fa9b11cf', 'a49142fe26744bf9b7d274dbfd55ef3d', 'a0397652140c40deacba69cbeabfe973', '1', '2017-07-11 20:46:30');
INSERT INTO `success_log` VALUES ('de17a56ef85d4576b095ae9443829b8d', '8ac11caf1aa74bb2aa016be12e094330', 'a0397652140c40deacba69cbeabfe973', '1', '2017-07-11 20:46:30');
INSERT INTO `success_log` VALUES ('e03c20370be34abeaa06b834c8ba1e6b', '5ef9ac2b5bb74faaa11d06cda27c40cc', 'a0397652140c40deacba69cbeabfe973', '1', '2017-07-11 20:28:30');
INSERT INTO `success_log` VALUES ('e5c1fe284773488094af26343dc2aa1a', '808a97a2de2e494a92622c518763a2b9', 'a0397652140c40deacba69cbeabfe973', '1', '2017-07-11 20:46:30');
INSERT INTO `success_log` VALUES ('ea6682969780408591fd762054a1fc18', '681ba2578af549bcba6cb27a6948c809', 'a0397652140c40deacba69cbeabfe973', '1', '2017-07-11 20:28:30');
INSERT INTO `success_log` VALUES ('f2efd2ce0be946e695a958e88a3acca3', 'fbb3ac43710c42bcbe5bce9c32966a7b', 'a0397652140c40deacba69cbeabfe973', '1', '2017-07-11 20:28:30');

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

-- ----------------------------
-- Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `user_id` varchar(50) NOT NULL COMMENT '用户唯一ID',
  `user_account` varchar(32) NOT NULL COMMENT '用户账户',
  `user_pwd` varchar(32) NOT NULL COMMENT '用户密码',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户信息表';

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('0268edb468ae4ae6ac76b6aeeb35f44c', 'ygy85', '123456');
INSERT INTO `user` VALUES ('0429d0c8345d41ae8961f9a0c667b0fa', 'ygy31', '123456');
INSERT INTO `user` VALUES ('04f123a50c2845e198de627cb972b7a4', 'ygy18', '123456');
INSERT INTO `user` VALUES ('057c973d1b8f47f3a0bc4817192aadda', 'ygy94', '123456');
INSERT INTO `user` VALUES ('068387bc0106428ba8b33580ecee76bd', 'ygy1', '123456');
INSERT INTO `user` VALUES ('07c71a1aa51e47b5998254898fa3690c', 'ygy7', '123456');
INSERT INTO `user` VALUES ('09e58e0e5e0d4b908bcf5fa8c3f30b0e', 'ygy92', '123456');
INSERT INTO `user` VALUES ('0adf3e065022485397f881c9d4734c13', 'ygy81', '123456');
INSERT INTO `user` VALUES ('0b914655162a460792a19895e56de817', 'ygy43', '123456');
INSERT INTO `user` VALUES ('0bd5395080a6481e87ca1eb621f2f8a9', 'ygy87', '123456');
INSERT INTO `user` VALUES ('0fbfa3b75ae34c1db7ccb4be6fb99ce1', 'ygy39', '123456');
INSERT INTO `user` VALUES ('1d6e8c026dfd4acc87edba26813914c5', 'ygy90', '123456');
INSERT INTO `user` VALUES ('1ec00428b229427cb07025a93dc5f43f', 'ygy12', '123456');
INSERT INTO `user` VALUES ('1f1c5a9bb0e74ad591f7ebe37e72ebc8', 'ygy69', '123456');
INSERT INTO `user` VALUES ('20797677e8df4c6190e5dac8ac397dac', 'ygy89', '123456');
INSERT INTO `user` VALUES ('20a67e2c88954d289c9face6f1ab0541', 'ygy73', '123456');
INSERT INTO `user` VALUES ('21e33d62dcaa41379a146ad652cc4929', 'ygy74', '123456');
INSERT INTO `user` VALUES ('24a951d664504b848ebdf04e3a707b9f', 'ygy77', '123456');
INSERT INTO `user` VALUES ('282d7440e0ed479996af777a3dabd333', 'ygy56', '123456');
INSERT INTO `user` VALUES ('28894a5e7080484abadb35d9083c222a', 'ygy64', '123456');
INSERT INTO `user` VALUES ('2a46306a8c584d7f9e8b8ecf8ba7ddec', 'ygy53', '123456');
INSERT INTO `user` VALUES ('2dca5be6955140cbb46c6d7fe55dd2b8', 'ygy41', '123456');
INSERT INTO `user` VALUES ('3113b2d5823247feafcae8e597a4b400', 'ygy72', '123456');
INSERT INTO `user` VALUES ('31d600ba84ba4287b7db06101fe2fac0', 'ygy47', '123456');
INSERT INTO `user` VALUES ('33184555928e43b19493b23232a22c81', 'ygy14', '123456');
INSERT INTO `user` VALUES ('366f486d19ee419d90859c2b3567a3df', 'ygy83', '123456');
INSERT INTO `user` VALUES ('37da20baf3074343b7bf3e3937b8156c', 'ygy80', '123456');
INSERT INTO `user` VALUES ('382ce7e39c6a4870a54cd3ff067baf61', 'ygy5', '123456');
INSERT INTO `user` VALUES ('3a7dd735c6be4f10b522a6d639f1446f', 'ygy67', '123456');
INSERT INTO `user` VALUES ('3fd67e80b19948c2937813fa6a14fc6b', 'ygy9', '123456');
INSERT INTO `user` VALUES ('4079f1623db14c0fab9d5ac372f7ae2d', 'ygy30', '123456');
INSERT INTO `user` VALUES ('4125a9ca0c93477d96b4fca635efadc8', 'ygy61', '123456');
INSERT INTO `user` VALUES ('43e669c6d9844e2fb30e5e0633fdd4c3', 'ygy65', '123456');
INSERT INTO `user` VALUES ('4885a66ca90a4dc7938fcf090b1e0a94', 'ygy84', '123456');
INSERT INTO `user` VALUES ('4ce1600ce2af4c29bd3d09e8b7972bb4', 'ygy45', '123456');
INSERT INTO `user` VALUES ('4d932bc0561f476b8677eb08f87b02af', 'ygy48', '123456');
INSERT INTO `user` VALUES ('4e93fa307ff945fda195e240a1c2838f', 'ygy28', '123456');
INSERT INTO `user` VALUES ('590c78c66ebb44658bd863bd34a602ce', 'ygy96', '123456');
INSERT INTO `user` VALUES ('5d2382c1f85447278b3983ee5b1c835a', 'ygy26', '123456');
INSERT INTO `user` VALUES ('5ef9ac2b5bb74faaa11d06cda27c40cc', 'ygy0', '123456');
INSERT INTO `user` VALUES ('5f6a14cc670042b588dc48be8051197e', 'ygy49', '123456');
INSERT INTO `user` VALUES ('621942d3bbb944b7af26ca60bbeb51e7', 'ygy27', '123456');
INSERT INTO `user` VALUES ('622b3431c58248fa83a35671095552b1', 'ygy', '123456');
INSERT INTO `user` VALUES ('63ea5a3974694d679d4996eb842872de', 'ygy4', '123456');
INSERT INTO `user` VALUES ('6475c275b5394f3dadaf9e7d29adfcd1', 'ygy91', '123456');
INSERT INTO `user` VALUES ('64b1786bb62044caae8c559b1a0cd609', 'ygy46', '123456');
INSERT INTO `user` VALUES ('681ba2578af549bcba6cb27a6948c809', 'ygy40', '123456');
INSERT INTO `user` VALUES ('6a5d93b347424281b69e269412907fa8', 'ygy44', '123456');
INSERT INTO `user` VALUES ('6f7d915c69f340ef85d10cc02fae3951', 'ygy75', '123456');
INSERT INTO `user` VALUES ('76487f0ce53d4578a70f6f552609dc90', 'ygy63', '123456');
INSERT INTO `user` VALUES ('7caac90e0a38450a8e1113264438005c', 'ygy98', '123456');
INSERT INTO `user` VALUES ('80716fba38ff4e4eac0a53de137b714b', 'ygy11', '123456');
INSERT INTO `user` VALUES ('808a97a2de2e494a92622c518763a2b9', 'ygy3', '123456');
INSERT INTO `user` VALUES ('8a76b3cdfa6b4c76a6463570c101c6b2', 'ygy15', '123456');
INSERT INTO `user` VALUES ('8ac11caf1aa74bb2aa016be12e094330', 'ygy99', '123456');
INSERT INTO `user` VALUES ('8e361be399cc4dfebd4d5ed575b582a0', 'ygy54', '123456');
INSERT INTO `user` VALUES ('8f34f426af784f1f83e8620dcf222580', 'ygy70', '123456');
INSERT INTO `user` VALUES ('8fc0bdea33484cbc99b0acae2b1f05f4', 'ygy52', '123456');
INSERT INTO `user` VALUES ('91ced777a2a44c10927977d0cc18ccd0', 'ygy55', '123456');
INSERT INTO `user` VALUES ('92be00635c4e47d1b200399680a797e6', 'ygy25', '123456');
INSERT INTO `user` VALUES ('988d231a13dd48f49c1a1166ae9092a1', 'ygy78', '123456');
INSERT INTO `user` VALUES ('989d91a7cdf2440789ea3456890e9bef', 'ygy34', '123456');
INSERT INTO `user` VALUES ('9c87da4c9b3a4969b8c33e7d0cd00168', 'ygy59', '123456');
INSERT INTO `user` VALUES ('9d67747a3c814562aace1eb06f92e4b8', 'ygy88', '123456');
INSERT INTO `user` VALUES ('9f97fb03210b4ceaadaf3cc410880a44', 'ygy23', '123456');
INSERT INTO `user` VALUES ('a10798fb165049429b2da218f79834fb', 'ygy6', '123456');
INSERT INTO `user` VALUES ('a49142fe26744bf9b7d274dbfd55ef3d', 'ygy82', '123456');
INSERT INTO `user` VALUES ('aa3cefb77cac4f439f77613a3633186c', 'ygy68', '123456');
INSERT INTO `user` VALUES ('acf15171ab6b4eb0a16679abb3b2b607', 'ygy51', '123456');
INSERT INTO `user` VALUES ('b0c804e5709f4526abdc50a9901ab8f5', 'ygy79', '123456');
INSERT INTO `user` VALUES ('b1c7e5b00758419aa3816dab26059e0b', 'ygy66', '123456');
INSERT INTO `user` VALUES ('b2b9b65141e8450d9fc48c9837655e39', 'ygy42', '123456');
INSERT INTO `user` VALUES ('b53d99c0b7e84b62b8e5be075200192c', 'ygy13', '123456');
INSERT INTO `user` VALUES ('b97550a3587843b38d928e2fbddd525a', 'ygy35', '123456');
INSERT INTO `user` VALUES ('b9820222bc454937b5a0e5cbeb202cc5', 'ygy29', '123456');
INSERT INTO `user` VALUES ('bd6e91c0e6174c80a53d571116b2ebea', 'ygy20', '123456');
INSERT INTO `user` VALUES ('be295470173542c491b50a39d8c0ed58', 'ygy22', '123456');
INSERT INTO `user` VALUES ('c24a73bb859645a7baec4f721a59722c', 'ygy60', '123456');
INSERT INTO `user` VALUES ('c475ad1851124bcb9eaca4bbd6347320', 'ygy19', '123456');
INSERT INTO `user` VALUES ('c4f6e7476ffe4dc6924b05492f3168d3', 'ygy58', '123456');
INSERT INTO `user` VALUES ('c570f54558484380ab4113bb9a1b553b', 'ygy97', '123456');
INSERT INTO `user` VALUES ('c5ef61cc32d244ec98cb2a2490e92da3', 'ygy33', '123456');
INSERT INTO `user` VALUES ('c642b716851d4aeeab4ab058d8c4e128', 'ygy93', '123456');
INSERT INTO `user` VALUES ('c7d43abc3cad41e5951163e845b27e78', 'ygy57', '123456');
INSERT INTO `user` VALUES ('cc065bede643463f944744ad2bcdae73', 'ygy2', '123456');
INSERT INTO `user` VALUES ('d3d997d96a4946d8a9db89f05187b6d6', 'ygy16', '123456');
INSERT INTO `user` VALUES ('d6706b6a813e4f10a3b34380296255f6', 'ygy95', '123456');
INSERT INTO `user` VALUES ('d8656e32bbdd47ca9a77333f665dd5a9', 'ygy37', '123456');
INSERT INTO `user` VALUES ('dcc899ffacfd45e2ad465ca4dd9e4a9e', 'ygy62', '123456');
INSERT INTO `user` VALUES ('e1977cc9b0e24318bc8a82515ddd9fdb', 'ygy32', '123456');
INSERT INTO `user` VALUES ('e29a49f38f15461e8ba953209baf9d11', 'ygy38', '123456');
INSERT INTO `user` VALUES ('e2b6f79408d44624bda2f02a83517ccb', 'ygy86', '123456');
INSERT INTO `user` VALUES ('e814fbb0f0e34984bf2eeebbb1d0fd1f', 'ygy10', '123456');
INSERT INTO `user` VALUES ('ee9be694285a4a3a952f66b26b8944a8', 'ygy17', '123456');
INSERT INTO `user` VALUES ('eebe068087434781a7c4e0f5ec54100d', 'ygy76', '123456');
INSERT INTO `user` VALUES ('f0f413252fd44a7fb277d9b652c2ad71', 'ygy8', '123456');
INSERT INTO `user` VALUES ('f989cc8f635c4fdbb56e9d0a0a0e5a88', 'ygy36', '123456');
INSERT INTO `user` VALUES ('faaddaf643774666b1940fb634fea1af', 'ygy50', '123456');
INSERT INTO `user` VALUES ('fbb3ac43710c42bcbe5bce9c32966a7b', 'ygy71', '123456');
INSERT INTO `user` VALUES ('fc7f84cd70f94d85b1e18d52076d27a0', 'ygy24', '123456');
INSERT INTO `user` VALUES ('ff1d7917b5e84ec3a068bedaea42db90', 'ygy21', '123456');
