-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- 主机： 192.168.100.1:3306
-- 生成日期： 2025-01-05 08:12:30
-- 服务器版本： 8.4.2
-- PHP 版本： 8.2.24

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- 数据库： `db_712`
--
CREATE DATABASE db_712;
USE db_712;
-- --------------------------------------------------------

--
-- 表的结构 `avatar`
--

CREATE TABLE `avatar` (
  `id` bigint NOT NULL COMMENT '头像ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `file_name` varchar(255) NOT NULL COMMENT '文件名',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户头像表';

--
-- 转存表中的数据 `avatar`
--

INSERT INTO `avatar` (`id`, `user_id`, `file_name`, `create_time`) VALUES
(1, 2, '7c17f9bf-8146-400c-bc74-32bf3101a9f0.jpg', '2024-12-28 22:29:35'),
(2, 2, '97a04b08-56e1-4baf-ad6c-931e3ff62b2e.jpg', '2024-12-28 22:29:36'),
(3, 2, 'd4708b0a-41fd-44b0-8863-852f74127dda.jpg', '2024-12-28 22:34:14'),
(4, 2, 'c120ba79-bcee-4f67-96b7-00dd72491c07.jpg', '2024-12-28 22:56:13'),
(5, 12345, '77f2d0de-1798-4fc3-9bb5-3553f8e9af44.jpg', '2024-12-28 23:32:14'),
(15, 2, 'db99f631-fc23-40f7-86bb-294e68130cd1.png', '2024-12-29 15:57:04'),
(17, 1735455737075, '9b6a11f8-c2a0-4c4d-b17f-54a5a0962899.jpg', '2024-12-30 20:41:04'),
(18, 1735563453985, '9674e590-494f-4491-b538-36554294c476.jpg', '2024-12-30 20:59:51'),
(21, 1, 'ae7b9086-fcb0-4dd7-acc1-66b0edc79560.jpg', '2024-12-31 16:12:55'),
(36, 1735569702277, '5181700c-d33b-4cde-a905-94f9af81a4db.jpg', '2025-01-01 16:55:33'),
(37, 3, '4e040247-1f28-4677-9344-9789ee0c36f0.png', '2025-01-03 15:28:12');

-- --------------------------------------------------------

--
-- 表的结构 `browse_history`
--

CREATE TABLE `browse_history` (
  `id` bigint NOT NULL COMMENT '浏览记录ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `browse_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '浏览时间',
  `stay_time` int DEFAULT NULL COMMENT '停留时间(秒)'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='浏览历史表';

--
-- 转存表中的数据 `browse_history`
--

INSERT INTO `browse_history` (`id`, `user_id`, `product_id`, `browse_time`, `stay_time`) VALUES
(2, 1, 2, '2023-12-01 09:10:00', 200),
(3, 2, 3, '2023-12-01 10:00:00', 400),
(12, 1735455737075, 2, '2024-12-29 19:46:45', NULL);

-- --------------------------------------------------------

--
-- 表的结构 `cart`
--

CREATE TABLE `cart` (
  `id` bigint NOT NULL COMMENT '购物车ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `quantity` int NOT NULL DEFAULT '1' COMMENT '商品数量',
  `selected` tinyint NOT NULL DEFAULT '1' COMMENT '是否选中：0-未选中，1-选中',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='购物车表';

--
-- 转存表中的数据 `cart`
--

INSERT INTO `cart` (`id`, `user_id`, `product_id`, `quantity`, `selected`, `create_time`, `update_time`) VALUES
(2, 2, 1, 2, 1, '2024-12-29 08:42:34', '2024-12-29 08:42:34'),
(18, 1735563453985, 111, 1, 1, '2024-12-31 20:33:24', '2024-12-31 20:33:24'),
(45, 3, 114, 1, 1, '2025-01-03 20:01:55', '2025-01-03 20:01:55');

-- --------------------------------------------------------

--
-- 表的结构 `coupon`
--

CREATE TABLE `coupon` (
  `id` bigint NOT NULL COMMENT '优惠券ID',
  `name` varchar(100) NOT NULL COMMENT '优惠券名称',
  `type` tinyint NOT NULL COMMENT '类型：1-满减券，2-折扣券',
  `value` decimal(10,2) NOT NULL COMMENT '优惠券面值',
  `min_amount` decimal(10,2) NOT NULL COMMENT '最低使用金额',
  `start_time` datetime NOT NULL COMMENT '生效时间',
  `end_time` datetime NOT NULL COMMENT '失效时间',
  `total` int NOT NULL COMMENT '发行总量',
  `remain` int NOT NULL COMMENT '剩余数量',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='优惠券表';

--
-- 转存表中的数据 `coupon`
--

INSERT INTO `coupon` (`id`, `name`, `type`, `value`, `min_amount`, `start_time`, `end_time`, `total`, `remain`, `status`, `create_time`, `update_time`) VALUES
(1, '新人专享券', 1, 200.00, 2000.00, '2023-12-01 00:00:00', '2025-12-31 23:59:59', 1000, 795, 1, '2024-12-29 08:42:34', '2025-01-03 19:58:33'),
(2, '年终特惠券', 2, 0.85, 5000.00, '2023-12-20 00:00:00', '2023-12-31 23:59:59', 500, 500, 1, '2024-12-29 08:42:34', '2024-12-29 08:42:34'),
(3, '双十二', 2, 200.00, 200.00, '2024-12-31 20:03:33', '2025-06-30 20:03:36', 20, 6, 1, '2024-12-31 12:04:09', '2025-01-03 17:19:02');

-- --------------------------------------------------------

--
-- 表的结构 `orders`
--

CREATE TABLE `orders` (
  `id` bigint NOT NULL COMMENT '订单ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `total_amount` decimal(10,2) NOT NULL COMMENT '订单总金额',
  `discount_amount` decimal(10,2) DEFAULT '0.00' COMMENT '优惠金额',
  `pay_amount` decimal(10,2) DEFAULT '0.00' COMMENT '实付金额',
  `user_coupon_id` bigint DEFAULT NULL COMMENT '使用的优惠券ID',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '订单状态：0-待付款，1-待发货，2-待收货，3-已完成，4-已取消',
  `address` varchar(255) NOT NULL COMMENT '收货地址',
  `receiver_name` varchar(50) NOT NULL COMMENT '收货人姓名',
  `receiver_phone` varchar(20) NOT NULL COMMENT '收货人电话',
  `remark` varchar(255) DEFAULT NULL COMMENT '订单备注',
  `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
  `ship_time` datetime DEFAULT NULL COMMENT '发货时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单表';

--
-- 转存表中的数据 `orders`
--

INSERT INTO `orders` (`id`, `user_id`, `total_amount`, `discount_amount`, `pay_amount`, `user_coupon_id`, `status`, `address`, `receiver_name`, `receiver_phone`, `remark`, `pay_time`, `ship_time`, `create_time`, `update_time`) VALUES
(1, 3, 7999.00, 0.00, 0.00, NULL, 1, '北京市朝阳区XX路XX号', '张三', '13812345678', '尽快发货', '2024-01-01 10:00:00', '2024-12-31 20:09:16', '2024-12-31 11:41:34', '2024-12-31 12:09:28'),
(4, 1, 3000.00, 0.00, 0.00, NULL, 3, '深圳市南山区XX大厦XX室', '赵六', '13333334444', '以完成', '2024-12-31 20:09:09', '2024-12-31 20:09:11', '2024-12-31 11:41:34', '2024-12-31 12:12:03'),
(5, 1, 11499.00, 0.00, 0.00, NULL, 4, '杭州市西湖区XX弄XX号', '孙七', '13555556666', '取消订单', '2024-12-31 20:09:12', '2024-12-31 20:09:14', '2024-12-31 11:41:34', '2024-12-31 12:09:28'),
(6, 1735563453985, 7999.00, 0.00, 0.00, NULL, 3, '杭州市西湖区XX弄07号', '许飞', '13677889900', '待取货', '2025-01-01 17:26:16', '2025-01-01 17:26:21', '2025-01-01 17:26:25', '2025-01-01 18:21:58'),
(7, 1001, 7999.00, 0.00, 0.00, NULL, 3, '杭州市西湖区XX弄XX号', 'test_user1', '13800138001', NULL, NULL, NULL, '2025-01-01 09:39:41', '2025-01-01 10:05:24'),
(8, 1001, 4999.00, 0.00, 0.00, NULL, 3, '杭州市西湖区XX弄XX号', 'test_user1', '13800138001', NULL, NULL, NULL, '2025-01-01 09:39:41', '2025-01-01 10:05:37'),
(9, 1002, 15999.00, 0.00, 0.00, NULL, 3, '深圳市南山区XX大厦XX室', 'test_user2', '13800138002', NULL, NULL, NULL, '2025-01-01 09:39:41', '2025-01-01 10:05:49'),
(1007, 3, 29997.00, 0.00, 0.00, NULL, 0, '福建莆田韩江翻斗花园aa', '你好先生', '123', '', NULL, NULL, '2025-01-01 19:06:16', '2025-01-01 19:06:16'),
(1008, 3, 11499.00, 0.00, 0.00, NULL, 1, '广东省深圳市南山区科技园南区', '李四', '13900139000', '', NULL, NULL, '2025-01-01 19:06:28', '2025-01-03 19:58:19'),
(1012, 1, 20797.00, 0.00, 0.00, NULL, 0, '北京市北京市海淀区中关村科技园1号楼', '张三', '13800138000', '', NULL, NULL, '2025-01-01 20:53:39', '2025-01-01 20:53:39');

-- --------------------------------------------------------

--
-- 表的结构 `order_items`
--

CREATE TABLE `order_items` (
  `id` bigint NOT NULL COMMENT '订单项ID',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `product_name` varchar(100) NOT NULL COMMENT '商品名称',
  `price` decimal(10,2) NOT NULL COMMENT '商品单价',
  `quantity` int NOT NULL COMMENT '购买数量',
  `subtotal` decimal(10,2) NOT NULL COMMENT '小计金额'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单项表';

--
-- 转存表中的数据 `order_items`
--

INSERT INTO `order_items` (`id`, `order_id`, `product_id`, `product_name`, `price`, `quantity`, `subtotal`) VALUES
(1, 1, 1, 'ROG魔霸新锐', 7999.00, 1, 7999.00),
(2, 2, 3, '外星人台式机', 15999.00, 1, 15999.00),
(3, 3, 2, 'ThinkPad E14', 4999.00, 1, 4999.00),
(4, 1, 1, 'ROG魔霸新锐游戏本', 7899.00, 1, 7899.00),
(5, 2, 2, 'ThinkPad E14商务笔记本', 4999.00, 1, 4999.00),
(6, 2, 4, '惠普暗影精灵9 Plus', 10999.00, 1, 10999.00),
(7, 3, 3, '外星人台式机', 15999.00, 1, 15999.00),
(8, 4, 106, '机械革命蛟龙16 pro', 7999.00, 1, 7999.00),
(9, 4, 107, '苹果MacBook Air M2', 11499.00, 1, 11499.00),
(10, 4, 108, '华硕灵耀14 2024', 6299.00, 1, 6299.00),
(11, 5, 109, 'ThinkPad X1 Carbon Gen 11', 12999.00, 1, 12999.00),
(12, 6, 1, 'ROG魔霸新锐', 7999.00, 1, 7999.00),
(13, 7, 1, 'ROG魔霸新锐', 7999.00, 1, 7999.00),
(14, 8, 2, 'ThinkPad E14', 4999.00, 1, 4999.00),
(15, 9, 3, '外星人台式机', 15999.00, 1, 15999.00),
(18, 1007, 111, '联想ThinkPad P15v', 12999.00, 2, 25998.00),
(19, 1007, 113, '英特尔NUC12华尔街峡谷', 3999.00, 1, 3999.00),
(20, 1008, 114, '苹果iMac 24英寸', 11499.00, 1, 11499.00),
(21, 1009, 114, '苹果iMac 24英寸', 11499.00, 1, 11499.00),
(22, 1010, 112, '惠普Z8 G4', 25999.00, 1, 25999.00),
(23, 1010, 113, '英特尔NUC12华尔街峡谷', 3999.00, 1, 3999.00),
(24, 1010, 114, '苹果iMac 24英寸', 11499.00, 1, 11499.00),
(25, 1011, 114, '苹果iMac 24英寸', 11499.00, 1, 11499.00),
(26, 1012, 1, 'ROG魔霸新锐', 7899.00, 2, 15798.00),
(27, 1012, 2, 'ThinkPad E14', 4999.00, 1, 4999.00),
(28, 1013, 114, '苹果iMac 24英寸', 11499.00, 1, 11499.00),
(29, 1014, 113, '英特尔NUC12华尔街峡谷', 3999.00, 1, 3999.00),
(30, 1015, 114, '苹果iMac 24英寸', 11499.00, 1, 11499.00),
(31, 1016, 114, '苹果iMac 24英寸', 11499.00, 1, 11499.00),
(32, 1017, 114, '苹果iMac 24英寸', 11499.00, 1, 11499.00),
(33, 1018, 114, '苹果iMac 24英寸', 11499.00, 1, 11499.00),
(34, 1079, 107, '苹果MacBook Air M2', 11499.00, 1, 11499.00),
(35, 1097, 112, '惠普Z8 G4', 25999.00, 1, 25999.00),
(36, 1097, 114, '苹果iMac 24英寸', 11499.00, 1, 11499.00),
(37, 1098, 114, '苹果iMac 24英寸', 11499.00, 1, 11499.00),
(38, 1099, 114, '苹果iMac 24英寸', 11499.00, 1, 11499.00),
(39, 1116, 114, '苹果iMac 24英寸', 11499.00, 1, 11499.00),
(40, 1125, 113, '英特尔NUC12华尔街峡谷', 3999.00, 1, 3999.00),
(41, 1125, 114, '苹果iMac 24英寸', 11499.00, 1, 11499.00);

-- --------------------------------------------------------

--
-- 表的结构 `order_return`
--

CREATE TABLE `order_return` (
  `id` bigint NOT NULL COMMENT '退货ID',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `return_reason` varchar(255) NOT NULL COMMENT '退货原因',
  `return_amount` decimal(10,2) NOT NULL COMMENT '退款金额',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态：0-待处理，1-已同意，2-已拒绝，3-已完成',
  `images` json DEFAULT NULL COMMENT '图片凭证',
  `apply_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '申请时间',
  `handle_time` datetime DEFAULT NULL COMMENT '处理时间',
  `handle_note` varchar(255) DEFAULT NULL COMMENT '处理备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单退货表';

--
-- 转存表中的数据 `order_return`
--

INSERT INTO `order_return` (`id`, `order_id`, `user_id`, `return_reason`, `return_amount`, `status`, `images`, `apply_time`, `handle_time`, `handle_note`, `create_time`, `update_time`) VALUES
(1, 7, 1001, '笔记本电脑出现蓝屏问题，且无法正常启动', 8999.99, 3, '[{\"url\": \"http://example.com/returns/laptop_bluescreen.jpg\"}, {\"url\": \"http://example.com/returns/laptop_error.jpg\"}]', '2024-03-15 10:00:00', '2024-03-16 14:30:00', '确认产品存在硬件故障，同意退款', '2025-01-01 09:39:41', '2025-01-01 12:11:26'),
(5, 20, 3, '测试', 1999.99, 3, '[\"http://example.com/image1.jpg\", \"http://example.com/image2.jpg\"]', '2025-01-03 17:43:37', '2025-01-03 18:16:05', NULL, '2025-01-03 17:43:37', '2025-01-03 18:16:05'),
(36, 2, 3, '测试', 1999.99, 0, '[\"http://example.com/image1.jpg\", \"http://example.com/image2.jpg\"]', '2025-01-03 18:16:35', NULL, NULL, '2025-01-03 18:16:35', '2025-01-03 18:16:35');

-- --------------------------------------------------------

--
-- 表的结构 `pickup_code`
--

CREATE TABLE `pickup_code` (
  `id` bigint NOT NULL,
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `pickup_code` varchar(20) NOT NULL COMMENT '取件码',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '取件状态：0-未取件，1-已取件',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='取件码表';

--
-- 转存表中的数据 `pickup_code`
--

INSERT INTO `pickup_code` (`id`, `user_id`, `order_id`, `pickup_code`, `status`, `create_time`, `update_time`) VALUES
(1, 1735563453985, 6, '647-4-3181', 1, '2025-01-01 17:33:26', '2025-01-01 17:34:07'),
(2, 1735563453985, 6, '037-2-3162', 1, '2025-01-01 17:39:59', '2025-01-01 17:40:20'),
(3, 1735563453985, 6, '497-1-6566', 1, '2025-01-01 18:21:41', '2025-01-01 18:21:58');

-- --------------------------------------------------------

--
-- 表的结构 `product`
--

CREATE TABLE `product` (
  `id` bigint NOT NULL COMMENT '商品ID',
  `name` varchar(100) NOT NULL COMMENT '商品名称',
  `description` text COMMENT '商品描述',
  `price` decimal(10,2) NOT NULL COMMENT '商品价格',
  `stock` int NOT NULL DEFAULT '0' COMMENT '库存数量',
  `category_id` bigint NOT NULL COMMENT '分类ID',
  `brand` varchar(50) NOT NULL COMMENT '品牌',
  `model` varchar(50) NOT NULL COMMENT '型号',
  `specifications` json DEFAULT NULL COMMENT '规格参数',
  `image_url` varchar(255) DEFAULT NULL COMMENT '商品图片URL',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '商品状态：0-下架，1-上架',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品表';

--
-- 转存表中的数据 `product`
--

INSERT INTO `product` (`id`, `name`, `description`, `price`, `stock`, `category_id`, `brand`, `model`, `specifications`, `image_url`, `status`, `create_time`, `update_time`) VALUES
(1, 'ROG魔霸新锐', '华硕ROG魔霸新锐游戏本', 7899.00, 98, 1, 'ASUS', 'ROG-2023', NULL, 'https://dlcdnwebimgs.asus.com/gain/383040CA-C40F-4CE7-ACCE-C630FB29E227/w717/h525/w273', 1, '2024-12-29 08:42:34', '2025-01-03 11:12:17'),
(2, 'ThinkPad E14', 'ThinkPad E14商务笔记本', 4999.00, 199, 2, 'Lenovo', 'E14-2023', '{\"cpu\": \"i5-1235U\", \"gpu\": \"Intel Iris Xe\", \"memory\": \"16GB\", \"storage\": \"512GB\"}', 'https://dlcdnwebimgs.asus.com/gain/383040CA-C40F-4CE7-ACCE-C630FB29E227/w717/h525/w273', 1, '2024-12-29 08:42:34', '2025-01-03 11:12:17'),
(3, '外星人台式机', '外星人Aurora R15游戏台式机', 15999.00, 50, 5, 'Alienware', 'Aurora-R15', '{\"cpu\": \"i9-13900K\", \"gpu\": \"RTX4090\", \"memory\": \"32GB\", \"storage\": \"2TB\"}', 'https://dlcdnwebimgs.asus.com/gain/383040CA-C40F-4CE7-ACCE-C630FB29E227/w717/h525/w273', 1, '2024-12-29 08:42:34', '2025-01-03 11:12:17'),
(105, '惠普暗影精灵9 Plus', '高性能游戏本，搭载强劲处理器与高性能显卡，能流畅运行各类大型游戏，屏幕显示效果出色，散热性能良好，专为游戏玩家打造。', 10999.00, 30, 1, 'HP', '暗影精灵9 Plus', '{\"cpu\": \"Intel i9-13980HX\", \"gpu\": \"NVIDIA RTX 4080\", \"memory\": \"32GB\", \"storage\": \"1TB SSD\", \"screen_size\": \"17.3英寸\", \"refresh_rate\": \"240Hz\"}', 'https://dlcdnwebimgs.asus.com/gain/383040CA-C40F-4CE7-ACCE-C630FB29E227/w717/h525/w273', 1, '2024-12-31 07:08:34', '2025-01-03 11:12:17'),
(106, '机械革命蛟龙16 Pro', '性价比很高的游戏本，配置均衡，AMD锐龙处理器配合高性能显卡，可提供强大的游戏性能，外观设计酷炫，具备不错的拓展性。', 7999.00, 40, 1, 'MECHREVO', '蛟龙16 Pro', '{\"cpu\": \"AMD Ryzen 7 7735H\", \"gpu\": \"NVIDIA RTX 4060\", \"memory\": \"16GB\", \"storage\": \"512GB SSD\", \"keyboard\": \"全尺寸机械键盘\", \"screen_size\": \"16英寸\"}', 'https://dlcdnwebimgs.asus.com/gain/383040CA-C40F-4CE7-ACCE-C630FB29E227/w717/h525/w273', 1, '2024-12-31 07:08:34', '2025-01-03 11:12:17'),
(107, '苹果MacBook Air M2', '苹果公司推出的轻薄便携笔记本电脑，采用自家M2芯片，性能强劲且功耗低，外观简约时尚，系统流畅稳定，适合日常办公及创意工作者使用。', 11499.00, 49, 3, 'Apple', 'MacBook Air M2', '{\"chip\": \"Apple M2\", \"memory\": \"8GB\", \"weight\": \"1.24千克\", \"storage\": \"256GB\", \"screen_size\": \"13.6英寸\"}', 'https://dlcdnwebimgs.asus.com/gain/383040CA-C40F-4CE7-ACCE-C630FB29E227/w717/h525/w273', 1, '2024-12-31 07:08:34', '2025-01-03 11:12:17'),
(108, '华硕灵耀14 2024', '轻薄本中的优秀代表，具备长续航能力，屏幕素质优秀，搭载英特尔酷睿处理器，整体性能可以满足日常办公、学习以及轻度娱乐需求，外观精致。', 6299.00, 60, 3, 'ASUS', '灵耀14 2024', '{\"cpu\": \"Intel Core i5-1440P\", \"memory\": \"16GB\", \"storage\": \"512GB SSD\", \"screen_size\": \"14英寸\", \"battery_life\": \"10小时左右\"}', 'https://dlcdnwebimgs.asus.com/gain/383040CA-C40F-4CE7-ACCE-C630FB29E227/w717/h525/w273', 1, '2024-12-31 07:08:34', '2025-01-03 11:12:44'),
(109, 'ThinkPad X1 Carbon Gen 11', '联想旗下经典商务笔记本，坚固耐用，键盘手感舒适，具备出色的安全性能和稳定性，适合商务办公人士，接口丰富，方便外接各种设备。', 12999.00, 20, 3, 'Lenovo', 'X1 Carbon Gen 11', '{\"cpu\": \"Intel Core i7-1360P\", \"memory\": \"16GB\", \"storage\": \"1TB SSD\", \"screen_size\": \"14英寸\", \"security_features\": \"指纹识别、面部识别、TPM芯片等\"}', 'https://dlcdnwebimgs.asus.com/gain/383040CA-C40F-4CE7-ACCE-C630FB29E227/w717/h525/w273', 1, '2024-12-31 07:08:34', '2025-01-03 11:12:44'),
(110, '戴尔Latitude 9440', '高端商务笔记本，拥有优秀的做工和品质，配备高分辨率屏幕，支持智能隐私功能，在保障性能的同时，注重数据安全与用户隐私，适合对办公环境要求较高的商务人士。', 14999.00, 15, 2, 'Dell', 'Latitude 9440', '{\"cpu\": \"Intel Core i9-13900H\", \"memory\": \"32GB\", \"storage\": \"2TB SSD\", \"screen_size\": \"14英寸\", \"privacy_screen\": \"智能防窥屏\"}', 'https://dlcdnwebimgs.asus.com/gain/383040CA-C40F-4CE7-ACCE-C630FB29E227/w717/h525/w273', 1, '2024-12-31 07:08:34', '2025-01-03 11:12:44'),
(111, '联想ThinkPad P15v', '联想的工作站笔记本，具备强大的专业性能，适合专业设计和工程人员。', 12999.00, 8, 4, 'Lenovo', 'ThinkPad P15v', '{\"cpu\": \"Intel Xeon W - 10885M\", \"gpu\": \"NVIDIA Quadro T1000\", \"memory\": \"32GB\", \"storage\": \"1TB SSD\", \"screen_size\": \"15.6英寸\"}', 'https://dlcdnwebimgs.asus.com/gain/383040CA-C40F-4CE7-ACCE-C630FB29E227/w717/h525/w273', 1, '2024-12-31 07:11:42', '2025-01-03 11:12:44'),
(112, '惠普Z8 G4', '惠普的工作站台式机，专为专业工作设计，性能稳定可靠。', 25999.00, 6, 6, 'HP', 'Z8 G4', '{\"cpu\": \"Intel Xeon W - 3265M\", \"gpu\": \"NVIDIA Quadro RTX 8000\", \"memory\": \"64GB\", \"storage\": \"2TB SSD + 4TB HDD\", \"case_type\": \"塔式\"}', 'https://dlcdnwebimgs.asus.com/gain/383040CA-C40F-4CE7-ACCE-C630FB29E227/w717/h525/w273', 1, '2024-12-31 07:12:32', '2025-01-03 11:12:44'),
(113, '英特尔NUC12华尔街峡谷', '英特尔的迷你台式机，体积小巧，适合对空间有要求的场景。', 3999.00, 16, 7, 'Intel', 'NUC12华尔街峡谷', '{\"cpu\": \"Intel Core i7 - 1260P\", \"memory\": \"16GB\", \"storage\": \"512GB SSD\", \"case_type\": \"迷你\"}', 'https://dlcdnwebimgs.asus.com/gain/383040CA-C40F-4CE7-ACCE-C630FB29E227/w717/h525/w273', 1, '2024-12-31 07:12:48', '2025-01-03 11:12:44'),
(114, '苹果iMac 24英寸', '苹果的一体机，设计简约，系统流畅，适合家庭和办公使用。', 11499.00, 16, 8, 'Apple', 'iMac 24英寸', '{\"chip\": \"Apple M1\", \"memory\": \"8GB\", \"storage\": \"256GB\", \"screen_size\": \"24英寸\"}', 'https://store.storeimages.cdn-apple.com/8756/as-images.apple.com/is/mac-card-40-imac-202410?wid=680&hei=528&fmt=p-jpg&qlt=95&.v=1731974953703', 1, '2024-12-31 07:13:05', '2025-01-03 11:17:38');

-- --------------------------------------------------------

--
-- 表的结构 `product_category`
--

CREATE TABLE `product_category` (
  `id` bigint NOT NULL COMMENT '分类ID',
  `name` varchar(50) NOT NULL COMMENT '分类名称',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品分类表';

--
-- 转存表中的数据 `product_category`
--

INSERT INTO `product_category` (`id`, `name`, `create_time`, `update_time`) VALUES
(1, '游戏本', '2024-12-29 08:42:34', '2024-12-31 07:02:38'),
(2, '商务本', '2024-12-29 08:42:34', '2024-12-31 07:02:38'),
(3, '轻薄本', '2024-12-29 08:42:34', '2024-12-31 07:02:44'),
(4, '工作站笔记本', '2024-12-31 07:03:17', '2024-12-31 07:03:17'),
(5, '游戏台式机', '2024-12-31 07:04:22', '2024-12-31 07:04:22'),
(6, '工作站台式机', '2024-12-31 07:04:22', '2024-12-31 07:04:22'),
(7, '迷你台式机', '2024-12-31 07:04:22', '2024-12-31 07:04:22'),
(8, '一体机', '2024-12-31 07:04:22', '2024-12-31 07:04:22');

-- --------------------------------------------------------

--
-- 表的结构 `product_favorite`
--

CREATE TABLE `product_favorite` (
  `id` bigint NOT NULL COMMENT '收藏ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品收藏表';

--
-- 转存表中的数据 `product_favorite`
--

INSERT INTO `product_favorite` (`id`, `user_id`, `product_id`, `create_time`) VALUES
(1, 1, 1, '2024-12-29 08:42:34'),
(2, 1, 3, '2024-12-29 08:42:34'),
(3, 2, 2, '2024-12-29 08:42:34'),
(23, 3, 106, '2024-12-31 20:14:05'),
(28, 3, 1, '2025-01-02 14:39:46'),
(79, 3, 114, '2025-01-02 19:53:40'),
(92, 3, 112, '2025-01-02 20:38:37'),
(93, 3, 111, '2025-01-03 15:24:52'),
(118, 3, 108, '2025-01-03 19:58:39');

-- --------------------------------------------------------

--
-- 表的结构 `product_recommendation`
--

CREATE TABLE `product_recommendation` (
  `id` bigint NOT NULL,
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `user_id` bigint DEFAULT NULL COMMENT '用户ID',
  `score` int NOT NULL DEFAULT '0' COMMENT '推荐分数',
  `recommendation_type` varchar(20) NOT NULL COMMENT '推荐类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品推荐表';

-- --------------------------------------------------------

--
-- 表的结构 `product_review`
--

CREATE TABLE `product_review` (
  `id` bigint NOT NULL COMMENT '评价ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `rating` int NOT NULL COMMENT '评分(1-5星)',
  `content` text COMMENT '评价内容',
  `images` json DEFAULT NULL COMMENT '评价图片URLs',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0-隐藏，1-显示',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品评价表';

--
-- 转存表中的数据 `product_review`
--

INSERT INTO `product_review` (`id`, `user_id`, `product_id`, `order_id`, `rating`, `content`, `images`, `status`, `create_time`, `update_time`) VALUES
(1, 1, 1, 1, 5, '游戏本性能很强，散热也不错', '[\"images/reviews/review1_1.jpg\", \"images/reviews/review1_2.jpg\"]', 1, '2024-12-29 08:42:34', '2024-12-31 15:31:09'),
(3, 0, 3, 2, 5, '1111111111111', '[]', 1, '2024-12-30 22:34:38', '2024-12-30 22:34:38'),
(4, 1, 3, 3, 4, '55555555', NULL, 1, '2024-12-31 07:34:06', '2024-12-31 07:34:06'),
(5, 1, 4, 3, 5, '哈哈哈哈哈哈哈哈哈', '[]', 1, '2024-12-31 15:37:59', '2024-12-31 15:37:59'),
(6, 0, 3, 2, 5, '啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊', '[]', 1, '2024-12-31 15:48:59', '2024-12-31 15:48:59'),
(7, 1, 3, 2, 4, '这个东西真高级', '[]', 1, '2024-12-31 15:55:17', '2024-12-31 15:55:17'),
(8, 1, 3, 2, 5, '666666', '[\"/file/avatar/1/ae7b9086-fcb0-4dd7-acc1-66b0edc79560.jpg\"]', 1, '2024-12-31 16:12:55', '2024-12-31 16:12:55'),
(9, 1, 3, 2, 5, '666666', '[]', 1, '2024-12-31 16:13:00', '2024-12-31 16:13:00'),
(10, 3, 114, 1116, 5, '没问题很好', '[]', 1, '2025-01-03 18:55:29', '2025-01-03 18:55:29'),
(11, 3, 114, 1116, 5, '的方式发送', '[]', 1, '2025-01-03 18:55:51', '2025-01-03 18:55:51');

-- --------------------------------------------------------

--
-- 表的结构 `promotion`
--

CREATE TABLE `promotion` (
  `id` bigint NOT NULL COMMENT '活动ID',
  `name` varchar(100) NOT NULL COMMENT '活动名称',
  `description` text COMMENT '活动描述',
  `type` tinyint NOT NULL COMMENT '活动类型：1-折扣，2-满减',
  `discount` decimal(10,2) DEFAULT NULL COMMENT '折扣率',
  `threshold` decimal(10,2) DEFAULT NULL COMMENT '满减阈值',
  `reduction` decimal(10,2) DEFAULT NULL COMMENT '满减金额',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态：0-未开始，1-进行中，2-已结束',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='促销活动表';

--
-- 转存表中的数据 `promotion`
--

INSERT INTO `promotion` (`id`, `name`, `description`, `type`, `discount`, `threshold`, `reduction`, `start_time`, `end_time`, `status`, `create_time`, `update_time`) VALUES
(1, '双12大促', '双12全场商品特惠', 1, 0.80, NULL, NULL, '2023-12-12 00:00:00', '2023-12-12 23:59:59', 0, '2024-12-29 08:42:34', '2024-12-29 08:42:34'),
(2, '新年满减', '满10000减1000', 2, NULL, 10000.00, 1000.00, '2023-12-25 00:00:00', '2024-01-05 23:59:59', 0, '2024-12-29 08:42:34', '2024-12-29 08:42:34');

-- --------------------------------------------------------

--
-- 表的结构 `promotion_product`
--

CREATE TABLE `promotion_product` (
  `id` bigint NOT NULL COMMENT '关联ID',
  `promotion_id` bigint NOT NULL COMMENT '活动ID',
  `product_id` bigint NOT NULL COMMENT '商品ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='活动商品关联表';

--
-- 转存表中的数据 `promotion_product`
--

INSERT INTO `promotion_product` (`id`, `promotion_id`, `product_id`) VALUES
(1, 1, 1),
(2, 1, 2),
(3, 2, 3);

-- --------------------------------------------------------

--
-- 表的结构 `promotion_rule`
--

CREATE TABLE `promotion_rule` (
  `id` bigint NOT NULL COMMENT '规则ID',
  `promotion_id` bigint NOT NULL COMMENT '活动ID',
  `type` tinyint NOT NULL COMMENT '规则类型：1-满减，2-折扣，3-赠品',
  `threshold` decimal(10,2) DEFAULT NULL COMMENT '满足条件金额',
  `discount` decimal(10,2) DEFAULT NULL COMMENT '优惠金额/折扣率',
  `gift_product_id` bigint DEFAULT NULL COMMENT '赠品商品ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='促销规则表';

-- --------------------------------------------------------

--
-- 表的结构 `sales_statistics`
--

CREATE TABLE `sales_statistics` (
  `id` bigint NOT NULL,
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `product_name` varchar(100) NOT NULL COMMENT '商品名称',
  `sales_count` int NOT NULL DEFAULT '0' COMMENT '销售数量',
  `sales_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '销售金额',
  `statistics_time` datetime NOT NULL COMMENT '统计时间',
  `time_range` varchar(10) NOT NULL COMMENT '统计时间范围'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品销售统计表';

-- --------------------------------------------------------

--
-- 表的结构 `supplier`
--

CREATE TABLE `supplier` (
  `id` bigint NOT NULL,
  `name` varchar(100) NOT NULL COMMENT '供应商名称',
  `contact` varchar(50) DEFAULT NULL COMMENT '联系人',
  `phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `address` varchar(200) DEFAULT NULL COMMENT '地址',
  `description` text COMMENT '供应商描述',
  `status` int DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='供应商表';

--
-- 转存表中的数据 `supplier`
--

INSERT INTO `supplier` (`id`, `name`, `contact`, `phone`, `email`, `address`, `description`, `status`, `create_time`, `update_time`) VALUES
(1, '苹果中ss国26', '张三', '13800138000', 'contact@apple.com.cn', '北京市朝阳区xx路xx号', '苹果产品官方供应商', 1, '2024-12-31 10:52:32', '2024-12-31 13:00:42'),
(2, '华为技术', '李四', '13800138002', 'contact@huawei.com', '深圳市龙岗区坂田华为基地', '华为产品官方供应商', 1, '2024-12-31 10:52:32', '2024-12-31 10:52:32'),
(3, '小米科技', '王五', '13800138003', 'contact@xiaomi.com', '北京市海淀区清河中街68号', '小米产品官方供应商', 1, '2024-12-31 10:52:32', '2024-12-31 10:52:32'),
(4, '戴尔中国', '赵六', '13800138004', 'contact@dell.com.cn', '厦门市思明区观日路10号', '戴尔电脑官方供应商', 1, '2024-12-31 10:52:32', '2024-12-31 10:52:32'),
(5, '联想集团', '钱七', '13800138005', 'contact@lenovo.com.cn', '北京市海淀区创业路6号', '联想产品官方供应商', 1, '2024-12-31 10:52:32', '2024-12-31 10:52:32'),
(1735643060091, '苹果中国', '张三', '13800138000', 'contact@apple.com.cn', '北京市朝阳区xx路xx号', '', 1, '2024-12-31 11:04:20', '2024-12-31 11:04:20');

-- --------------------------------------------------------

--
-- 表的结构 `supplier_product`
--

CREATE TABLE `supplier_product` (
  `id` bigint NOT NULL,
  `supplier_id` bigint NOT NULL COMMENT '供应商ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `stock` int NOT NULL DEFAULT '0' COMMENT '库存数量',
  `price` decimal(10,2) NOT NULL COMMENT '供应价格',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='供应商商品库存关联表';

--
-- 转存表中的数据 `supplier_product`
--

INSERT INTO `supplier_product` (`id`, `supplier_id`, `product_id`, `stock`, `price`, `create_time`, `update_time`) VALUES
(1, 1, 1, 100, 6999.00, '2024-12-31 10:52:32', '2024-12-31 10:52:32'),
(2, 1, 2, 200, 7999.00, '2024-12-31 10:52:32', '2024-12-31 10:52:32'),
(3, 2, 3, 150, 4999.00, '2024-12-31 10:52:32', '2024-12-31 10:52:32'),
(4, 2, 4, 180, 5999.00, '2024-12-31 10:52:32', '2024-12-31 10:52:32'),
(5, 3, 5, 300, 2999.00, '2024-12-31 10:52:32', '2024-12-31 10:52:32'),
(6, 3, 6, 250, 3999.00, '2024-12-31 10:52:32', '2024-12-31 10:52:32'),
(7, 4, 7, 120, 5499.00, '2024-12-31 10:52:32', '2024-12-31 10:52:32'),
(8, 4, 8, 150, 6499.00, '2024-12-31 10:52:32', '2024-12-31 10:52:32'),
(9, 5, 9, 200, 4499.00, '2024-12-31 10:52:32', '2024-12-31 10:52:32'),
(10, 5, 10, 180, 5499.00, '2024-12-31 10:52:32', '2024-12-31 10:52:32'),
(1735642977872, 1, 12, 100, 1000.00, '2024-12-31 11:02:58', '2024-12-31 11:02:58'),
(1735643020492, 1, 22, 100, 1000.00, '2024-12-31 11:03:41', '2024-12-31 11:03:41'),
(1735650031737, 1, 322, 100, 1000.00, '2024-12-31 13:00:32', '2024-12-31 13:00:32');

-- --------------------------------------------------------

--
-- 表的结构 `tb_activity`
--

CREATE TABLE `tb_activity` (
  `id` bigint NOT NULL,
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '活动名称',
  `description` text COLLATE utf8mb4_unicode_ci COMMENT '活动描述',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '活动状态：0-未开始，1-进行中，2-已结束',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- 转存表中的数据 `tb_activity`
--

INSERT INTO `tb_activity` (`id`, `name`, `description`, `start_time`, `end_time`, `status`, `create_time`, `update_time`) VALUES
(1, '新年特惠活动', '2024新年电脑特惠活动', '2024-01-01 00:00:00', '2024-01-07 23:59:59', 1, '2025-01-01 09:35:36', '2025-01-01 09:35:36'),
(2, '情人节促销', '情人节笔记本电脑优惠', '2024-02-14 00:00:00', '2024-02-15 23:59:59', 0, '2025-01-01 09:35:36', '2025-01-01 09:35:36'),
(3, '开学季大促', '开学季学生优惠活动', '2024-03-01 00:00:00', '2024-03-15 23:59:59', 0, '2025-01-01 09:35:36', '2025-01-01 09:35:36'),
(4, '新年特惠活动', '2024新年电脑特惠活动', '2024-01-01 00:00:00', '2024-01-07 23:59:59', 1, '2025-01-01 09:38:05', '2025-01-01 09:38:05'),
(5, '情人节促销', '情人节笔记本电脑优惠', '2024-02-14 00:00:00', '2024-02-15 23:59:59', 0, '2025-01-01 09:38:05', '2025-01-01 09:38:05'),
(6, '开学季大促', '开学季学生优惠活动', '2024-03-01 00:00:00', '2024-03-15 23:59:59', 0, '2025-01-01 09:38:05', '2025-01-01 09:38:05');

-- --------------------------------------------------------

--
-- 表的结构 `tb_activity_product`
--

CREATE TABLE `tb_activity_product` (
  `id` bigint NOT NULL,
  `activity_id` bigint NOT NULL COMMENT '活动ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `activity_price` decimal(10,2) NOT NULL COMMENT '活动价格',
  `stock` int NOT NULL COMMENT '活动库存',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- 转存表中的数据 `tb_activity_product`
--

INSERT INTO `tb_activity_product` (`id`, `activity_id`, `product_id`, `activity_price`, `stock`, `create_time`, `update_time`) VALUES
(1, 1, 1, 6999.00, 50, '2025-01-01 09:35:36', '2025-01-01 09:35:36'),
(2, 1, 2, 3999.00, 30, '2025-01-01 09:35:36', '2025-01-01 09:35:36'),
(3, 2, 3, 13999.00, 20, '2025-01-01 09:35:36', '2025-01-01 09:35:36'),
(4, 2, 4, 9999.00, 25, '2025-01-01 09:35:36', '2025-01-01 09:35:36'),
(5, 3, 106, 6999.00, 40, '2025-01-01 09:35:36', '2025-01-01 09:35:36'),
(6, 3, 107, 9999.00, 35, '2025-01-01 09:35:36', '2025-01-01 09:35:36'),
(7, 1, 1, 6999.00, 50, '2025-01-01 09:38:05', '2025-01-01 09:38:05'),
(8, 1, 2, 3999.00, 30, '2025-01-01 09:38:05', '2025-01-01 09:38:05'),
(9, 2, 3, 13999.00, 20, '2025-01-01 09:38:05', '2025-01-01 09:38:05'),
(10, 2, 4, 9999.00, 25, '2025-01-01 09:38:05', '2025-01-01 09:38:05');

-- --------------------------------------------------------

--
-- 表的结构 `tb_product`
--

CREATE TABLE `tb_product` (
  `id` bigint NOT NULL,
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商品名称',
  `description` text COLLATE utf8mb4_unicode_ci COMMENT '商品描述',
  `price` decimal(10,2) NOT NULL COMMENT '商品价格',
  `stock` int NOT NULL COMMENT '库存数量',
  `image_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商品图片URL',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0-下架，1-上架',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- 转存表中的数据 `tb_product`
--

INSERT INTO `tb_product` (`id`, `name`, `description`, `price`, `stock`, `image_url`, `status`, `create_time`, `update_time`) VALUES
(1, 'ROG魔霸新锐', 'ROG魔霸新锐 AMD锐龙R7 RTX4060', 7999.00, 100, '/images/products/rog1.jpg', 1, '2025-01-01 09:38:05', '2025-01-01 09:38:05'),
(2, 'ThinkPad E14', 'ThinkPad E14 锐龙版 商务本', 4999.00, 50, '/images/products/thinkpad1.jpg', 1, '2025-01-01 09:38:05', '2025-01-01 09:38:05'),
(3, '外星人台式机', 'Alienware Aurora R15台式机', 15999.00, 30, '/images/products/alienware1.jpg', 1, '2025-01-01 09:38:05', '2025-01-01 09:38:05'),
(4, '惠普暗影精灵9', '惠普暗影精灵9 Plus', 11999.00, 40, '/images/products/hp1.jpg', 1, '2025-01-01 09:38:05', '2025-01-01 09:38:05');

-- --------------------------------------------------------

--
-- 表的结构 `user`
--

CREATE TABLE `user` (
  `id` bigint NOT NULL COMMENT '用户ID',
  `name` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';

--
-- 转存表中的数据 `user`
--

INSERT INTO `user` (`id`, `name`, `password`, `email`) VALUES
(1, 'admin', '12345678', 'wenshuntao@qq.com'),
(2, 'aa', '123456', 's22s@example.com'),
(3, 'lisi', '123456', 'wenshuntao@qq.com'),
(4, 'wangwu', '123456', 'wangwu@example.com'),
(5, 'zhaoliu', '123456', 'zhaoliu@example.com'),
(1001, 'test_user1', '123456', 'test1@example.com'),
(1002, 'test_user2', '123456', 'test2@example.com'),
(1735454816692, 'aaww', '12345678w1', '565900538@qq.com'),
(1735455170795, 'qawaw', 'ws12345678', 'kzzavxzba@tempmail.cn'),
(1735455228202, 'wwwsw', 'ws12345678', 'kzzavxzba@tempmail.cn'),
(1735455270728, 'awww', 'adminwwwwww12', 'kzzavxzba@tempmail.cn'),
(1735455737075, 'xfff', '1234xfff', 'xfff@qq.com'),
(1735458976689, 'xzff', 'x23333322', 'uf6cn1+emxgz46llb4sg@guerrillamail.com'),
(1735459043943, 'xzff2', 'x23333322', 'uf6cn1+emxgz46llb4sg@guerrillamail.com'),
(1735459818895, 'xzff25', '222', 'uf6cn1+emxgz46llb4sg@guerrillamail.com'),
(1735461179200, 'xzff252', 'x23333322', 'uf6cn1+emxgz46llb4sg@guerrillamail.com'),
(1735462649481, 'xzff2522', 'x23333322', 'uf6cn1+emxgz46llb4sg@guerrillamail.com'),
(1735462764359, 'xz12345', 'x23333322', 'uf6cn1+emxgz46llb4sg@guerrillamail.com'),
(1735462936482, '23333a', 'x23333322', 'uf6cn1+emxgz46llb4sg@guerrillamail.com'),
(1735463077368, '23333a2', 'x23333322', 'uf6cn1+emxgz46llb4sg@guerrillamail.com'),
(1735463184057, '223aa', 'x23333322', 'uf6cn1+emxgz46llb4sg@guerrillamail.com'),
(1735563453985, 'xffff', 'xf123456', 'xffff@qq.com'),
(1735569702277, 'xzffff', 'xzffff11', 'uf6cn1+emxgz46llb4sg@guerrillamail.com'),
(1735740384366, 'test022', '1234562A', '123@qq'),
(1735895029768, 'lisi2', 'wS12345678', 'wenshuntao@qq.com');

-- --------------------------------------------------------

--
-- 表的结构 `user_address`
--

CREATE TABLE `user_address` (
  `id` bigint NOT NULL COMMENT '地址ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `receiver_name` varchar(50) NOT NULL COMMENT '收货人姓名',
  `receiver_phone` varchar(20) NOT NULL COMMENT '收货人电话',
  `province` varchar(20) NOT NULL COMMENT '省份',
  `city` varchar(20) NOT NULL COMMENT '城市',
  `district` varchar(20) NOT NULL COMMENT '区/县',
  `detail_address` varchar(255) NOT NULL COMMENT '详细地址',
  `is_default` tinyint NOT NULL DEFAULT '0' COMMENT '是否默认地址：0-否，1-是',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户收货地址表';

--
-- 转存表中的数据 `user_address`
--

INSERT INTO `user_address` (`id`, `user_id`, `receiver_name`, `receiver_phone`, `province`, `city`, `district`, `detail_address`, `is_default`, `create_time`, `update_time`) VALUES
(1, 1, '张三', '13800138000', '北京市', '北京市', '海淀区', '中关村科技园1号楼', 1, '2024-12-29 08:42:34', '2024-12-29 08:42:34'),
(2, 1, '张三', '13800138001', '上海市', '上海市', '浦东新区', '陆家嘴金融中心', 0, '2024-12-29 08:42:34', '2024-12-29 08:42:34'),
(3, 3, '李四', '13900139000', '广东省', '深圳市', '南山区', '科技园南区', 0, '2024-12-29 08:42:34', '2025-01-01 10:40:05'),
(6, 3, '你好先生', '123', '福建', '莆田', '韩江', '翻斗花园aa', 0, '2024-12-30 20:58:46', '2025-01-01 10:41:11'),
(8, 2, '张三', '13800138000', '广东省', '深圳市', '南山区', '科技园南区1号楼', 0, '2024-12-30 21:25:36', '2024-12-30 13:26:41'),
(9, 2, '张三', '13800138000', '广东省', '深圳市', '南山区', '科技园南区1号楼', 0, '2024-12-30 21:26:41', '2024-12-30 13:26:41'),
(11, 2, '张三', '13800138000', '广东省', '深圳市', '南山区', '科技园南区1号楼', 0, '2024-12-30 21:26:42', '2024-12-30 14:10:12'),
(12, 2, '张三', '13800138000', '广东省', '深圳市', '南山区', '科技园南区1号楼', 0, '2024-12-30 22:10:13', '2024-12-30 14:22:47'),
(17, 2, '张三', '13800138000', '广东省', '深圳市', '南山区', '科技园南区1号楼', 0, '2024-12-31 16:30:59', '2024-12-31 08:31:11');

-- --------------------------------------------------------

--
-- 表的结构 `user_behavior`
--

CREATE TABLE `user_behavior` (
  `id` bigint NOT NULL,
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `behavior_type` varchar(20) NOT NULL COMMENT '行为类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户行为表';

-- --------------------------------------------------------

--
-- 表的结构 `user_coupon`
--

CREATE TABLE `user_coupon` (
  `id` bigint NOT NULL COMMENT '用户优惠券ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `coupon_id` bigint NOT NULL COMMENT '优惠券ID',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态：0-未使用，1-已使用，2-已过期',
  `use_time` datetime DEFAULT NULL COMMENT '使用时间',
  `order_id` bigint DEFAULT NULL COMMENT '使用的订单ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '领取时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户优惠券表';

--
-- 转存表中的数据 `user_coupon`
--

INSERT INTO `user_coupon` (`id`, `user_id`, `coupon_id`, `status`, `use_time`, `order_id`, `create_time`) VALUES
(1, 1, 1, 1, '2023-12-01 10:00:00', 1, '2023-12-01 09:00:00'),
(2, 1, 2, 0, NULL, NULL, '2023-12-01 09:00:00'),
(3, 3, 1, 1, '2025-01-02 13:45:47', NULL, '2023-12-01 09:30:00'),
(4, 3, 1, 1, NULL, NULL, '2024-12-31 10:16:55'),
(5, 3, 3, 1, '2025-01-01 22:52:32', NULL, '2024-12-31 20:04:15'),
(6, 3, 3, 1, '2025-01-02 13:59:09', NULL, '2024-12-31 20:42:23'),
(165, 3, 1, 1, '2025-01-03 16:50:46', NULL, '2025-01-03 16:50:39'),
(166, 3, 1, 1, '2025-01-03 17:13:22', NULL, '2025-01-03 16:50:52'),
(167, 3, 3, 1, '2025-01-03 16:51:06', NULL, '2025-01-03 16:50:55'),
(192, 3, 1, 1, '2025-01-03 19:45:15', NULL, '2025-01-03 17:19:00'),
(193, 3, 3, 0, NULL, NULL, '2025-01-03 17:19:02'),
(194, 3, 1, 0, NULL, NULL, '2025-01-03 19:58:31'),
(195, 3, 1, 0, NULL, NULL, '2025-01-03 19:58:33');

-- --------------------------------------------------------

--
-- 表的结构 `user_feedback`
--

CREATE TABLE `user_feedback` (
  `id` bigint NOT NULL COMMENT '反馈ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `type` varchar(20) NOT NULL COMMENT '反馈类型',
  `title` varchar(100) NOT NULL COMMENT '反馈标题',
  `content` text NOT NULL COMMENT '反馈内容',
  `images` json DEFAULT NULL COMMENT '反馈图片URLs',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态：0-未处理，1-处理中，2-已处理',
  `reply` text COMMENT '回复内容',
  `reply_time` datetime DEFAULT NULL COMMENT '回复时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户反馈表';

--
-- 转存表中的数据 `user_feedback`
--

INSERT INTO `user_feedback` (`id`, `user_id`, `type`, `title`, `content`, `images`, `status`, `reply`, `reply_time`, `create_time`, `update_time`) VALUES
(1, 1, '商品建议', '希望增加更多显卡选择', '建议增加AMD显卡的选择，比如RX7900系列', NULL, 1, '感谢您的建议，我们会考虑引入更多显卡型号', '2023-12-01 15:00:00', '2024-12-29 08:42:34', '2024-12-29 08:42:34'),
(2, 2, '售后问题', '电脑蓝屏', '新买的电脑出现蓝屏现象', '[\"images/feedback/blue_screen.jpg\"]', 2, '已经为您处理，请更新最新驱动', '2023-12-01 16:00:00', '2024-12-29 08:42:34', '2024-12-29 08:42:34');

--
-- 转储表的索引
--

--
-- 表的索引 `avatar`
--
ALTER TABLE `avatar`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_user_id` (`user_id`);

--
-- 表的索引 `browse_history`
--
ALTER TABLE `browse_history`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_user_id` (`user_id`),
  ADD KEY `idx_product_id` (`product_id`);

--
-- 表的索引 `cart`
--
ALTER TABLE `cart`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uk_user_product` (`user_id`,`product_id`),
  ADD KEY `idx_user_id` (`user_id`),
  ADD KEY `idx_product_id` (`product_id`);

--
-- 表的索引 `coupon`
--
ALTER TABLE `coupon`
  ADD PRIMARY KEY (`id`);

--
-- 表的索引 `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_user_id` (`user_id`);

--
-- 表的索引 `order_items`
--
ALTER TABLE `order_items`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_order_id` (`order_id`),
  ADD KEY `idx_product_id` (`product_id`);

--
-- 表的索引 `order_return`
--
ALTER TABLE `order_return`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uk_order_id` (`order_id`),
  ADD KEY `idx_user_id` (`user_id`);

--
-- 表的索引 `pickup_code`
--
ALTER TABLE `pickup_code`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_user_id` (`user_id`),
  ADD KEY `idx_order_id` (`order_id`),
  ADD KEY `idx_pickup_code` (`pickup_code`);

--
-- 表的索引 `product`
--
ALTER TABLE `product`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_category` (`category_id`),
  ADD KEY `idx_brand` (`brand`);

--
-- 表的索引 `product_category`
--
ALTER TABLE `product_category`
  ADD PRIMARY KEY (`id`);

--
-- 表的索引 `product_favorite`
--
ALTER TABLE `product_favorite`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uk_user_product` (`user_id`,`product_id`),
  ADD KEY `idx_user_id` (`user_id`),
  ADD KEY `idx_product_id` (`product_id`);

--
-- 表的索引 `product_recommendation`
--
ALTER TABLE `product_recommendation`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_user_product` (`user_id`,`product_id`),
  ADD KEY `idx_score` (`score`);

--
-- 表的索引 `product_review`
--
ALTER TABLE `product_review`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_user_id` (`user_id`),
  ADD KEY `idx_product_id` (`product_id`),
  ADD KEY `idx_order_id` (`order_id`);

--
-- 表的索引 `promotion`
--
ALTER TABLE `promotion`
  ADD PRIMARY KEY (`id`);

--
-- 表的索引 `promotion_product`
--
ALTER TABLE `promotion_product`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uk_promotion_product` (`promotion_id`,`product_id`),
  ADD KEY `idx_promotion_id` (`promotion_id`),
  ADD KEY `idx_product_id` (`product_id`);

--
-- 表的索引 `promotion_rule`
--
ALTER TABLE `promotion_rule`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_promotion_id` (`promotion_id`);

--
-- 表的索引 `sales_statistics`
--
ALTER TABLE `sales_statistics`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_product_id` (`product_id`),
  ADD KEY `idx_statistics_time` (`statistics_time`);

--
-- 表的索引 `supplier`
--
ALTER TABLE `supplier`
  ADD PRIMARY KEY (`id`);

--
-- 表的索引 `supplier_product`
--
ALTER TABLE `supplier_product`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uk_supplier_product` (`supplier_id`,`product_id`) COMMENT '同一供应商的同一商品只能有一条记录',
  ADD KEY `idx_supplier_product_supplier` (`supplier_id`),
  ADD KEY `idx_supplier_product_product` (`product_id`);

--
-- 表的索引 `tb_activity`
--
ALTER TABLE `tb_activity`
  ADD PRIMARY KEY (`id`);

--
-- 表的索引 `tb_activity_product`
--
ALTER TABLE `tb_activity_product`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_activity_id` (`activity_id`),
  ADD KEY `idx_product_id` (`product_id`);

--
-- 表的索引 `tb_product`
--
ALTER TABLE `tb_product`
  ADD PRIMARY KEY (`id`);

--
-- 表的索引 `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`);

--
-- 表的索引 `user_address`
--
ALTER TABLE `user_address`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_user_id` (`user_id`);

--
-- 表的索引 `user_behavior`
--
ALTER TABLE `user_behavior`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_user_id` (`user_id`),
  ADD KEY `idx_product_id` (`product_id`),
  ADD KEY `idx_behavior_type` (`behavior_type`),
  ADD KEY `idx_create_time` (`create_time`);

--
-- 表的索引 `user_coupon`
--
ALTER TABLE `user_coupon`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_user_id` (`user_id`),
  ADD KEY `idx_coupon_id` (`coupon_id`),
  ADD KEY `idx_order_id` (`order_id`);

--
-- 表的索引 `user_feedback`
--
ALTER TABLE `user_feedback`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_user_id` (`user_id`);

--
-- 在导出的表使用AUTO_INCREMENT
--

--
-- 使用表AUTO_INCREMENT `avatar`
--
ALTER TABLE `avatar`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT COMMENT '头像ID', AUTO_INCREMENT=38;

--
-- 使用表AUTO_INCREMENT `browse_history`
--
ALTER TABLE `browse_history`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT COMMENT '浏览记录ID', AUTO_INCREMENT=13;

--
-- 使用表AUTO_INCREMENT `cart`
--
ALTER TABLE `cart`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT COMMENT '购物车ID', AUTO_INCREMENT=46;

--
-- 使用表AUTO_INCREMENT `coupon`
--
ALTER TABLE `coupon`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT COMMENT '优惠券ID', AUTO_INCREMENT=625;

--
-- 使用表AUTO_INCREMENT `orders`
--
ALTER TABLE `orders`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT COMMENT '订单ID', AUTO_INCREMENT=1130;

--
-- 使用表AUTO_INCREMENT `order_items`
--
ALTER TABLE `order_items`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT COMMENT '订单项ID', AUTO_INCREMENT=42;

--
-- 使用表AUTO_INCREMENT `order_return`
--
ALTER TABLE `order_return`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT COMMENT '退货ID', AUTO_INCREMENT=80;

--
-- 使用表AUTO_INCREMENT `pickup_code`
--
ALTER TABLE `pickup_code`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- 使用表AUTO_INCREMENT `product`
--
ALTER TABLE `product`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT COMMENT '商品ID', AUTO_INCREMENT=1003;

--
-- 使用表AUTO_INCREMENT `product_category`
--
ALTER TABLE `product_category`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT COMMENT '分类ID', AUTO_INCREMENT=10;

--
-- 使用表AUTO_INCREMENT `product_favorite`
--
ALTER TABLE `product_favorite`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT COMMENT '收藏ID', AUTO_INCREMENT=123;

--
-- 使用表AUTO_INCREMENT `product_recommendation`
--
ALTER TABLE `product_recommendation`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- 使用表AUTO_INCREMENT `product_review`
--
ALTER TABLE `product_review`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT COMMENT '评价ID', AUTO_INCREMENT=12;

--
-- 使用表AUTO_INCREMENT `promotion`
--
ALTER TABLE `promotion`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT COMMENT '活动ID', AUTO_INCREMENT=3;

--
-- 使用表AUTO_INCREMENT `promotion_product`
--
ALTER TABLE `promotion_product`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT COMMENT '关联ID', AUTO_INCREMENT=4;

--
-- 使用表AUTO_INCREMENT `promotion_rule`
--
ALTER TABLE `promotion_rule`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT COMMENT '规则ID';

--
-- 使用表AUTO_INCREMENT `sales_statistics`
--
ALTER TABLE `sales_statistics`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- 使用表AUTO_INCREMENT `tb_activity`
--
ALTER TABLE `tb_activity`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- 使用表AUTO_INCREMENT `tb_activity_product`
--
ALTER TABLE `tb_activity_product`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- 使用表AUTO_INCREMENT `tb_product`
--
ALTER TABLE `tb_product`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- 使用表AUTO_INCREMENT `user_address`
--
ALTER TABLE `user_address`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT COMMENT '地址ID', AUTO_INCREMENT=18;

--
-- 使用表AUTO_INCREMENT `user_behavior`
--
ALTER TABLE `user_behavior`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- 使用表AUTO_INCREMENT `user_coupon`
--
ALTER TABLE `user_coupon`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户优惠券ID', AUTO_INCREMENT=199;

--
-- 使用表AUTO_INCREMENT `user_feedback`
--
ALTER TABLE `user_feedback`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT COMMENT '反馈ID', AUTO_INCREMENT=3;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
