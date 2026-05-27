-- =============================================
-- 二手书交易平台数据库 - book_trading_platform
-- =============================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

DROP DATABASE IF EXISTS `book_trading_platform`;
CREATE DATABASE `book_trading_platform` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `book_trading_platform`;

-- ----------------------------
-- 1. 管理员表
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `nickname` varchar(50) DEFAULT NULL COMMENT '昵称',
  `avatar_url` varchar(500) DEFAULT NULL COMMENT '头像',
  `tel` varchar(20) DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `status` varchar(20) NOT NULL DEFAULT '启用' COMMENT '状态：启用/禁用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='管理员';

INSERT INTO `admin` VALUES (1, 'admin', '123456', '系统管理员', 'https://api.dicebear.com/7.x/initials/svg?seed=Admin', '13800138000', 'admin@bookplatform.com', '启用', '2026-01-01 00:00:00');

-- ----------------------------
-- 2. 用户表（买家）
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `nickname` varchar(50) NOT NULL COMMENT '昵称',
  `avatar_url` varchar(500) NOT NULL COMMENT '头像',
  `tel` varchar(20) DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `status` varchar(20) NOT NULL DEFAULT '启用' COMMENT '状态：启用/禁用',
  `balance` decimal(10,2) NOT NULL DEFAULT 0.00 COMMENT '余额',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户（买家）';

INSERT INTO `user` VALUES
(1, 'buyer1', '123456', '书虫小李', 'https://api.dicebear.com/7.x/adventurer/svg?seed=Felix', '13900001001', 'buyer1@example.com', '启用', 5000.00, '2026-01-15 10:00:00'),
(2, 'buyer2', '123456', '爱读书的小王', 'https://api.dicebear.com/7.x/adventurer/svg?seed=Aneka', '13900001002', 'buyer2@example.com', '启用', 3000.00, '2026-02-01 14:30:00'),
(3, 'buyer3', '123456', '学霸张同学', 'https://api.dicebear.com/7.x/adventurer/svg?seed=Oliver', '13900001003', 'buyer3@example.com', '启用', 8000.00, '2026-02-20 09:15:00');

-- ----------------------------
-- 3. 卖家表（书籍卖家/店铺）
-- ----------------------------
DROP TABLE IF EXISTS `shop`;
CREATE TABLE `shop` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `nickname` varchar(50) NOT NULL COMMENT '昵称',
  `avatar_url` varchar(500) NOT NULL COMMENT '头像',
  `tel` varchar(20) DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `status` varchar(20) NOT NULL DEFAULT '禁用' COMMENT '状态：启用/禁用（需管理员审核）',
  `name` varchar(100) NOT NULL COMMENT '店铺名称',
  `fans_count` int NOT NULL DEFAULT 0 COMMENT '粉丝数量',
  `aptitude_imgs` text COMMENT '身份认证资质图片',
  `intro` text COMMENT '店铺简介',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='卖家（书籍卖家/店铺）';

INSERT INTO `shop` VALUES
(1, 'seller1', '123456', '林小鹿', 'https://api.dicebear.com/7.x/adventurer/svg?seed=Lily', '13800001001', 'seller1@example.com', '启用', '鹿鸣书屋', 128, 'https://picsum.photos/seed/cert1/400/300', '专注二手教材与文学经典，每本书都经过精心检验，品质有保障。', '2026-01-10 08:00:00'),
(2, 'seller2', '123456', '书香阁主', 'https://api.dicebear.com/7.x/adventurer/svg?seed=Robert', '13800001002', 'seller2@example.com', '启用', '书香阁', 256, 'https://picsum.photos/seed/cert2/400/300', '高校毕业生，大量优质考研教辅、专业教材低价出售。', '2026-01-20 09:00:00'),
(3, 'seller3', '123456', '小陈同学', 'https://api.dicebear.com/7.x/adventurer/svg?seed=Chen', '13800001003', 'seller3@example.com', '启用', '陈氏旧书坊', 85, 'https://picsum.photos/seed/cert3/400/300', '文学爱好者，收藏多年的好书与大家分享。', '2026-02-05 15:00:00');

-- ----------------------------
-- 4. 书籍分类表
-- ----------------------------
DROP TABLE IF EXISTS `book_category`;
CREATE TABLE `book_category` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(50) NOT NULL COMMENT '分类名称',
  `remark` text COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='书籍分类';

INSERT INTO `book_category` VALUES
(1, '教材教辅', '高校教材、考研教辅、考证资料', '2026-01-01 00:00:00'),
(2, '文学小说', '中外文学名著、现当代小说、散文诗歌', '2026-01-01 00:00:00'),
(3, '计算机科技', '编程语言、算法、人工智能、数据库', '2026-01-01 00:00:00'),
(4, '经管励志', '经济管理、创业投资、职场励志', '2026-01-01 00:00:00'),
(5, '人文社科', '历史、哲学、心理学、社会学', '2026-01-01 00:00:00'),
(6, '外语考试', '英语四六级、雅思托福、日语韩语', '2026-01-01 00:00:00'),
(7, '童书绘本', '儿童读物、绘本、青少年文学', '2026-01-01 00:00:00'),
(8, '生活百科', '美食烹饪、健康养生、旅行指南', '2026-01-01 00:00:00');

-- ----------------------------
-- 5. 书籍信息表
-- ----------------------------
DROP TABLE IF EXISTS `book`;
CREATE TABLE `book` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `book_name` varchar(200) NOT NULL COMMENT '书名',
  `author` varchar(100) DEFAULT NULL COMMENT '作者',
  `publisher` varchar(100) DEFAULT NULL COMMENT '出版社',
  `isbn` varchar(20) DEFAULT NULL COMMENT 'ISBN',
  `original_price` decimal(10,2) DEFAULT NULL COMMENT '原价',
  `price` decimal(10,2) NOT NULL COMMENT '售价',
  `quality` varchar(20) NOT NULL DEFAULT '九成新' COMMENT '品相：全新/九成新/八成新/七成新',
  `category_id` int NOT NULL COMMENT '分类ID',
  `main_img` text NOT NULL COMMENT '封面图',
  `img_list` text COMMENT '详细图片（逗号分隔）',
  `intro` text COMMENT '书籍简介/描述',
  `stock` int NOT NULL DEFAULT 1 COMMENT '库存',
  `sales_volume` int NOT NULL DEFAULT 0 COMMENT '销量',
  `shop_id` int NOT NULL COMMENT '卖家ID',
  `review_status` varchar(20) NOT NULL DEFAULT '待审核' COMMENT '审核状态：待审核/已通过/已驳回',
  `status` varchar(20) NOT NULL DEFAULT '上架' COMMENT '书籍状态：上架/下架',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_category` (`category_id`),
  KEY `idx_shop` (`shop_id`),
  KEY `idx_review` (`review_status`),
  KEY `idx_book_name` (`book_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='书籍信息';

INSERT INTO `book` VALUES
(1, '高等数学（第七版）上册', '同济大学数学系', '高等教育出版社', '9787040396638', 46.00, 18.00, '八成新', 1, 'https://img1.doubanio.com/view/subject/l/public/s29erta542.jpg', NULL, '同济版高数经典教材，有少量笔记标注，不影响阅读。适合大一新生使用。', 3, 12, 1, '已通过', '上架', '2026-03-01 10:00:00'),
(2, '线性代数（第六版）', '同济大学数学系', '高等教育出版社', '9787040396652', 35.00, 12.00, '九成新', 1, 'https://img2.doubanio.com/view/subject/l/public/s29890012.jpg', NULL, '几乎全新，仅翻阅过几次，无笔记无折痕。', 2, 8, 1, '已通过', '上架', '2026-03-02 11:00:00'),
(3, '百年孤独', '加西亚·马尔克斯', '南海出版公司', '9787544253994', 55.00, 22.00, '九成新', 2, 'https://img2.doubanio.com/view/subject/l/public/s27237850.jpg', NULL, '范晔译本，魔幻现实主义经典之作。书页干净整洁。', 1, 5, 2, '已通过', '上架', '2026-03-05 09:00:00'),
(4, '活着', '余华', '作家出版社', '9787506365437', 29.00, 10.00, '八成新', 2, 'https://img2.doubanio.com/view/subject/l/public/s29053580.jpg', NULL, '余华经典作品，封面有轻微磨损，内页完好。', 2, 15, 2, '已通过', '上架', '2026-03-06 14:00:00'),
(5, 'Java核心技术 卷I（第11版）', '凯·S.霍斯特曼', '机械工业出版社', '9787111612728', 149.00, 55.00, '九成新', 3, 'https://img1.doubanio.com/view/subject/l/public/s33718940.jpg', NULL, 'Java入门经典教材，适合初学者系统学习。仅翻阅过前几章。', 1, 3, 1, '已通过', '上架', '2026-03-08 10:30:00'),
(6, '算法导论（第3版）', 'Thomas H.Cormen', '机械工业出版社', '9787111407010', 128.00, 48.00, '八成新', 3, 'https://img1.doubanio.com/view/subject/l/public/s25648004.jpg', NULL, '计算机专业必备参考书，部分章节有铅笔标注，可擦除。', 1, 6, 2, '已通过', '上架', '2026-03-10 08:00:00'),
(7, '经济学原理（第8版）微观经济学分册', '曼昆', '北京大学出版社', '9787301305263', 88.00, 32.00, '七成新', 4, 'https://img9.doubanio.com/view/subject/l/public/s33829875.jpg', NULL, '经管专业必修教材，有课堂笔记，内容完整。', 2, 4, 3, '已通过', '上架', '2026-03-12 16:00:00'),
(8, '人类简史：从动物到上帝', '尤瓦尔·赫拉利', '中信出版社', '9787508647357', 68.00, 25.00, '九成新', 5, 'https://img1.doubanio.com/view/subject/l/public/s27814883.jpg', NULL, '畅销人文社科读物，近乎全新，值得收藏。', 1, 9, 3, '已通过', '上架', '2026-03-15 11:00:00'),
(9, '大学英语四级真题详解', '星火英语', '上海交通大学出版社', '9787313242365', 59.80, 20.00, '八成新', 6, 'https://picsum.photos/seed/cet4/300/400', NULL, '含近10年真题，部分题目有答案标记，听力材料完整。', 3, 7, 1, '已通过', '上架', '2026-03-18 09:30:00'),
(10, '小王子', '安托万·德·圣-埃克苏佩里', '人民文学出版社', '9787020042494', 32.00, 12.00, '全新', 2, 'https://img1.doubanio.com/view/subject/l/public/s1103152.jpg', NULL, '全新未拆封，精装版，适合收藏或送人。', 2, 3, 3, '已通过', '上架', '2026-03-20 10:00:00'),
(11, '数据结构与算法分析', '马克·艾伦·维斯', '机械工业出版社', '9787111528395', 79.00, 30.00, '九成新', 3, 'https://picsum.photos/seed/datastructure/300/400', NULL, 'C语言版数据结构经典教材，考研复习好帮手。', 1, 2, 2, '已通过', '上架', '2026-03-22 14:00:00'),
(12, '考研英语词汇闪过', '刘晓艳', '国家开放大学出版社', '9787304100812', 48.00, 15.00, '八成新', 6, 'https://picsum.photos/seed/kaoyan/300/400', NULL, '考研英语必备词汇书，高频核心词有标记，方便重点复习。', 2, 11, 1, '已通过', '上架', '2026-03-25 08:00:00'),
(13, '三体', '刘慈欣', '重庆出版社', '9787229042066', 23.00, 8.00, '七成新', 2, 'https://img2.doubanio.com/view/subject/l/public/s28357056.jpg', NULL, '中国科幻巨作，书角有轻微卷曲，故事精彩不容错过。', 1, 20, 3, '已通过', '上架', '2026-04-01 15:00:00'),
(14, 'Python编程从入门到实践（第2版）', '埃里克·马瑟斯', '人民邮电出版社', '9787115546081', 89.00, 35.00, '全新', 3, 'https://picsum.photos/seed/python/300/400', NULL, '全新正版，Python零基础入门首选教材。', 1, 1, 2, '待审核', '上架', '2026-04-05 10:00:00'),
(15, '毛泽东思想和中国特色社会主义理论体系概论', '本书编写组', '高等教育出版社', '9787040494815', 26.00, 8.00, '八成新', 1, 'https://picsum.photos/seed/mao/300/400', NULL, '思政课教材，有少量课堂笔记，内容完好。', 5, 18, 1, '已通过', '上架', '2026-04-08 09:00:00');

-- ----------------------------
-- 6. 书籍收藏表
-- ----------------------------
DROP TABLE IF EXISTS `book_collect`;
CREATE TABLE `book_collect` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `book_id` int NOT NULL COMMENT '书籍ID',
  `user_id` int NOT NULL COMMENT '用户ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_book_user` (`book_id`, `user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='书籍收藏';

INSERT INTO `book_collect` VALUES
(1, 1, 1, '2026-03-10 10:00:00'),
(2, 3, 1, '2026-03-11 14:00:00'),
(3, 5, 2, '2026-03-12 09:00:00'),
(4, 8, 2, '2026-03-15 16:00:00'),
(5, 4, 3, '2026-03-18 11:00:00');

-- ----------------------------
-- 7. 店铺收藏表
-- ----------------------------
DROP TABLE IF EXISTS `shop_collect`;
CREATE TABLE `shop_collect` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `shop_id` int NOT NULL COMMENT '店铺ID',
  `user_id` int NOT NULL COMMENT '用户ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_shop_user` (`shop_id`, `user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='店铺收藏';

INSERT INTO `shop_collect` VALUES
(1, 1, 1, '2026-03-10 10:30:00'),
(2, 2, 2, '2026-03-12 09:30:00'),
(3, 3, 3, '2026-03-20 14:00:00');

-- ----------------------------
-- 8. 购物车表
-- ----------------------------
DROP TABLE IF EXISTS `shopping_cart`;
CREATE TABLE `shopping_cart` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `book_id` int NOT NULL COMMENT '书籍ID',
  `user_id` int NOT NULL COMMENT '用户ID',
  `quantity` int NOT NULL DEFAULT 1 COMMENT '数量',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='购物车';

INSERT INTO `shopping_cart` VALUES
(1, 6, 1, 1, '2026-04-01 10:00:00'),
(2, 8, 1, 1, '2026-04-01 10:05:00'),
(3, 2, 3, 1, '2026-04-02 14:00:00');

-- ----------------------------
-- 9. 收货地址表
-- ----------------------------
DROP TABLE IF EXISTS `shipping_address`;
CREATE TABLE `shipping_address` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(50) NOT NULL COMMENT '收货人姓名',
  `tel` varchar(20) NOT NULL COMMENT '收货人电话',
  `address` text NOT NULL COMMENT '收货地址',
  `user_id` int NOT NULL COMMENT '用户ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='收货地址';

INSERT INTO `shipping_address` VALUES
(1, '李明', '13900001001', '北京市海淀区中关村大街1号清华大学紫荆公寓', 1, '2026-02-01 10:00:00'),
(2, '王芳', '13900001002', '上海市杨浦区四平路1239号同济大学菜鸟驿站', 2, '2026-02-15 14:00:00'),
(3, '张伟', '13900001003', '广州市番禺区大学城外环西路100号广东工业大学', 3, '2026-03-01 09:00:00');

-- ----------------------------
-- 10. 订单表（7种状态流转）
-- ----------------------------
DROP TABLE IF EXISTS `book_order`;
CREATE TABLE `book_order` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `order_no` varchar(50) NOT NULL COMMENT '订单编号',
  `book_id` int NOT NULL COMMENT '书籍ID',
  `shop_id` int NOT NULL COMMENT '卖家ID',
  `user_id` int NOT NULL COMMENT '买家ID',
  `quantity` int NOT NULL DEFAULT 1 COMMENT '数量',
  `total_money` decimal(10,2) NOT NULL COMMENT '总金额',
  `status` int NOT NULL DEFAULT 0 COMMENT '订单状态：0未支付 1已支付/待发货 2已发货/待收货 3交易完成 4已取消 5申请退款 6退款完成',
  `consignee_name` varchar(50) NOT NULL COMMENT '收货人姓名',
  `consignee_tel` varchar(20) NOT NULL COMMENT '收货人电话',
  `consignee_address` text NOT NULL COMMENT '收货地址',
  `tracking_number` varchar(50) DEFAULT NULL COMMENT '物流单号',
  `remark` text COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`),
  KEY `idx_user` (`user_id`),
  KEY `idx_shop` (`shop_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单';

INSERT INTO `book_order` VALUES
(1, 'BO20260401001', 1, 1, 1, 1, 18.00, 3, '李明', '13900001001', '北京市海淀区中关村大街1号清华大学紫荆公寓', 'SF1029384756', NULL, '2026-04-01 15:00:00', '2026-04-05 10:00:00'),
(2, 'BO20260402001', 3, 2, 2, 1, 22.00, 2, '王芳', '13900001002', '上海市杨浦区四平路1239号同济大学菜鸟驿站', 'YT9876543210', NULL, '2026-04-02 09:00:00', '2026-04-04 14:00:00'),
(3, 'BO20260403001', 4, 2, 1, 1, 10.00, 1, '李明', '13900001001', '北京市海淀区中关村大街1号清华大学紫荆公寓', NULL, '麻烦尽快发货', '2026-04-03 10:00:00', NULL),
(4, 'BO20260405001', 9, 1, 3, 1, 20.00, 0, '张伟', '13900001003', '广州市番禺区大学城外环西路100号广东工业大学', NULL, NULL, '2026-04-05 16:00:00', NULL),
(5, 'BO20260406001', 13, 3, 2, 1, 8.00, 4, '王芳', '13900001002', '上海市杨浦区四平路1239号同济大学菜鸟驿站', NULL, NULL, '2026-04-06 11:00:00', '2026-04-06 12:00:00');

-- ----------------------------
-- 11. 订单评价表
-- ----------------------------
DROP TABLE IF EXISTS `order_evaluate`;
CREATE TABLE `order_evaluate` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` int NOT NULL COMMENT '用户ID',
  `book_id` int NOT NULL COMMENT '书籍ID',
  `order_id` int NOT NULL COMMENT '订单ID',
  `content` text NOT NULL COMMENT '评价内容',
  `rate` int NOT NULL DEFAULT 5 COMMENT '评分1-5',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单评价';

INSERT INTO `order_evaluate` VALUES
(1, 1, 1, 1, '书的品相和描述一致，八成新确实有些笔记标注，但完全不影响使用。卖家发货也很快，好评！', 5, '2026-04-06 10:00:00');

-- ----------------------------
-- 12. 浏览记录表
-- ----------------------------
DROP TABLE IF EXISTS `browsing_history`;
CREATE TABLE `browsing_history` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `book_id` int NOT NULL COMMENT '书籍ID',
  `user_id` int NOT NULL COMMENT '用户ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='浏览记录';

INSERT INTO `browsing_history` VALUES
(1, 1, 1, '2026-03-28 10:00:00'),
(2, 3, 1, '2026-03-28 10:05:00'),
(3, 5, 1, '2026-03-29 14:00:00'),
(4, 8, 2, '2026-03-30 09:00:00'),
(5, 4, 2, '2026-03-30 09:10:00'),
(6, 13, 3, '2026-04-01 15:00:00'),
(7, 7, 3, '2026-04-01 15:15:00'),
(8, 6, 1, '2026-04-02 08:00:00'),
(9, 10, 2, '2026-04-02 16:00:00'),
(10, 2, 3, '2026-04-03 11:00:00');

-- ----------------------------
-- 13. 消息表（在线沟通）
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `from_user_id` int NOT NULL COMMENT '发送者ID',
  `from_user_type` varchar(10) NOT NULL COMMENT '发送者类型：USER/SHOP',
  `to_user_id` int NOT NULL COMMENT '接收者ID',
  `to_user_type` varchar(10) NOT NULL COMMENT '接收者类型：USER/SHOP',
  `content` text NOT NULL COMMENT '消息内容',
  `is_read` tinyint NOT NULL DEFAULT 0 COMMENT '是否已读：0未读 1已读',
  `book_id` int DEFAULT NULL COMMENT '关联书籍ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_to_user` (`to_user_id`, `to_user_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='消息';

INSERT INTO `message` VALUES
(1, 1, 'USER', 1, 'SHOP', '你好，请问这本高等数学还有货吗？', 1, 1, '2026-04-01 10:00:00'),
(2, 1, 'SHOP', 1, 'USER', '有的，还有3本库存，品相都是八成新。', 1, 1, '2026-04-01 10:02:00'),
(3, 1, 'USER', 1, 'SHOP', '好的，我下单了，麻烦尽快发货哦！', 1, 1, '2026-04-01 10:05:00'),
(4, 2, 'USER', 2, 'SHOP', '百年孤独这本书封面有折痕吗？', 1, 3, '2026-04-02 08:30:00'),
(5, 2, 'SHOP', 2, 'USER', '没有的，书页非常干净整洁，九成新品相。', 1, 3, '2026-04-02 08:35:00');

-- ----------------------------
-- 14. 轮播图表
-- ----------------------------
DROP TABLE IF EXISTS `slideshow`;
CREATE TABLE `slideshow` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `title` varchar(100) NOT NULL COMMENT '标题',
  `main_img` text NOT NULL COMMENT '图片',
  `link` varchar(255) NOT NULL COMMENT '链接',
  `sort` int NOT NULL DEFAULT 0 COMMENT '排序',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='轮播图';

INSERT INTO `slideshow` VALUES
(1, '开学季教材特惠', 'https://picsum.photos/seed/banner1/1200/400', '/bookList?categoryId=1', 1, '2026-03-01 00:00:00'),
(2, '文学经典专区', 'https://picsum.photos/seed/banner2/1200/400', '/bookList?categoryId=2', 2, '2026-03-01 00:00:00'),
(3, '考研备战书单', 'https://picsum.photos/seed/banner3/1200/400', '/bookList?categoryId=6', 3, '2026-03-01 00:00:00');

-- ----------------------------
-- 15. 公告表
-- ----------------------------
DROP TABLE IF EXISTS `announcement`;
CREATE TABLE `announcement` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `title` varchar(200) NOT NULL COMMENT '标题',
  `content` text NOT NULL COMMENT '内容',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='公告';

INSERT INTO `announcement` VALUES
(1, '平台上线公告', '欢迎使用二手书交易平台！本平台致力于为广大读者提供优质、便捷的二手书交易服务，让每一本好书都能找到新主人。', '2026-03-01 00:00:00'),
(2, '关于书籍品相说明', '为保障买家权益，请卖家如实描述书籍品相。品相分为：全新（未拆封或未使用）、九成新（几乎无使用痕迹）、八成新（有轻微使用痕迹）、七成新（有明显使用痕迹但不影响阅读）。', '2026-03-15 00:00:00');

SET FOREIGN_KEY_CHECKS = 1;
