-- 插入用户数据
INSERT INTO t_user (id, username, password, email, phone, avatar, nickname, real_name, gender, birthday, status) VALUES
(1, 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE0lBfGoxXo6V5F9K', 'admin@example.com', '13800000001', 'https://example.com/avatar/admin.jpg', '管理员', '张伟', 1, '1990-01-01', 1),
(2, 'zhangsan', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE0lBfGoxXo6V5F9K', 'zhangsan@example.com', '13800000002', 'https://example.com/avatar/zhangsan.jpg', '张三', '张三', 1, '1995-05-15', 1),
(3, 'lisi', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE0lBfGoxXo6V5F9K', 'lisi@example.com', '13800000003', 'https://example.com/avatar/lisi.jpg', '李四', '李四', 2, '1992-08-20', 1),
(4, 'wangwu', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE0lBfGoxXo6V5F9K', 'wangwu@example.com', '13800000004', 'https://example.com/avatar/wangwu.jpg', '王五', '王五', 1, '1988-12-10', 1),
(5, 'zhaoliu', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE0lBfGoxXo6V5F9K', 'zhaoliu@example.com', '13800000005', 'https://example.com/avatar/zhaoliu.jpg', '赵六', '赵六', 2, '1998-03-25', 1),
(6, 'sunqi', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE0lBfGoxXo6V5F9K', 'sunqi@example.com', '13800000006', 'https://example.com/avatar/sunqi.jpg', '孙七', '孙七', 1, '1993-07-08', 1),
(7, 'zhouba', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE0lBfGoxXo6V5F9K', 'zhouba@example.com', '13800000007', 'https://example.com/avatar/zhouba.jpg', '周八', '周八', 2, '1996-11-30', 1),
(8, 'wujiu', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE0lBfGoxXo6V5F9K', 'wujiu@example.com', '13800000008', 'https://example.com/avatar/wujiu.jpg', '吴九', '吴九', 1, '1991-04-18', 0);  -- 禁用状态

-- 密码都是：123456 (BCrypt加密)

-- 用户地址
INSERT INTO t_user_address (id, user_id, receiver_name, receiver_phone, province, city, district, detail_address, zip_code, is_default) VALUES
(1, 2, '张三', '13800000002', '北京市', '北京市', '海淀区', '中关村大街1号', '100080', 1),
(2, 2, '张三', '13800000002', '上海市', '上海市', '浦东新区', '陆家嘴环路2号', '200120', 0),
(3, 3, '李四', '13800000003', '广东省', '深圳市', '南山区', '科技园南区3号', '518000', 1),
(4, 3, '李四', '13800000003', '广东省', '广州市', '天河区', '天河路4号', '510000', 0),
(5, 4, '王五', '13800000004', '浙江省', '杭州市', '西湖区', '文三路5号', '310000', 1),
(6, 5, '赵六', '13800000005', '四川省', '成都市', '高新区', '天府大道6号', '610000', 1),
(7, 6, '孙七', '13800000006', '湖北省', '武汉市', '洪山区', '光谷大道7号', '430000', 1),
(8, 7, '周八', '13800000007', '江苏省', '南京市', '鼓楼区', '中山路8号', '210000', 1);


-- 商品分类表
INSERT INTO t_category (id, parent_id, name, sort_order, icon, status) VALUES
-- 一级分类
(1, 0, '手机数码', 1, 'https://example.com/icon/digital.png', 1),
(2, 0, '电脑办公', 2, 'https://example.com/icon/computer.png', 1),
(3, 0, '家用电器', 3, 'https://example.com/icon/appliance.png', 1),
(4, 0, '图书音像', 4, 'https://example.com/icon/book.png', 1),
(5, 0, '服饰鞋包', 5, 'https://example.com/icon/clothing.png', 1),

-- 二级分类（手机数码下）
(11, 1, '手机', 1, 'https://example.com/icon/phone.png', 1),
(12, 1, '耳机', 2, 'https://example.com/icon/earphone.png', 1),
(13, 1, '智能穿戴', 3, 'https://example.com/icon/wearable.png', 1),

-- 二级分类（电脑办公下）
(21, 2, '笔记本', 1, 'https://example.com/icon/laptop.png', 1),
(22, 2, '显示器', 2, 'https://example.com/icon/monitor.png', 1),
(23, 2, '键盘鼠标', 3, 'https://example.com/icon/keyboard.png', 1),

-- 二级分类（家用电器下）
(31, 3, '电视', 1, 'https://example.com/icon/tv.png', 1),
(32, 3, '冰箱', 2, 'https://example.com/icon/fridge.png', 1),
(33, 3, '洗衣机', 3, 'https://example.com/icon/washer.png', 1);


-- 商品表
INSERT INTO t_product (id, category_id, name, sub_title, description, price, original_price, stock, sold_count, main_image, images, status, is_hot, is_new, is_recommend) VALUES
-- 手机类
(1, 11, 'Apple iPhone 15 Pro Max', 'A17 Pro芯片 钛金属边框', 'iPhone 15 Pro Max 配备6.7英寸超视网膜XDR显示屏，A17 Pro芯片，4800万像素主摄。', 8999.00, 9999.00, 100, 50, 'https://example.com/images/iphone15_main.jpg', '["https://example.com/images/iphone15_1.jpg","https://example.com/images/iphone15_2.jpg"]', 1, 1, 1, 1),

(2, 11, 'Samsung Galaxy S24 Ultra', 'AI手机 最强拍照', 'Galaxy S24 Ultra 搭载骁龙8 Gen3处理器，2亿像素主摄，支持S Pen。', 7999.00, 8999.00, 80, 30, 'https://example.com/images/s24_main.jpg', '["https://example.com/images/s24_1.jpg","https://example.com/images/s24_2.jpg"]', 1, 1, 1, 1),

(3, 11, 'Xiaomi 14 Pro', '徕卡光学 全新设计', '小米14 Pro 配备骁龙8 Gen3，徕卡Summilux镜头，120W快充。', 4999.00, 5499.00, 150, 80, 'https://example.com/images/xiaomi14_main.jpg', '["https://example.com/images/xiaomi14_1.jpg"]', 1, 1, 1, 1),

(4, 11, 'HUAWEI Mate 60 Pro', '卫星通话 昆仑玻璃', '华为Mate 60 Pro 支持卫星通话，玄武架构，昆仑玻璃。', 6999.00, 7999.00, 60, 40, 'https://example.com/images/mate60_main.jpg', '["https://example.com/images/mate60_1.jpg"]', 1, 1, 0, 1),

-- 耳机类
(5, 12, 'Sony WH-1000XM5', '旗舰降噪 头戴式耳机', '索尼1000XM5 配备双芯片降噪，30小时续航，LDAC高清传输。', 2599.00, 2999.00, 200, 120, 'https://example.com/images/sony_xm5_main.jpg', '["https://example.com/images/sony_xm5_1.jpg"]', 1, 1, 1, 1),

(6, 12, 'AirPods Pro 2', '自适应降噪 USB-C', 'AirPods Pro 2 搭载H2芯片，自适应降噪，USB-C充电盒。', 1899.00, 1999.00, 300, 200, 'https://example.com/images/airpods_main.jpg', '["https://example.com/images/airpods_1.jpg"]', 1, 1, 1, 1),

-- 笔记本类
(7, 21, 'MacBook Pro 16英寸', 'M3 Pro芯片 32GB内存', 'MacBook Pro 配备M3 Pro芯片，16英寸Liquid Retina XDR显示屏。', 19999.00, 22999.00, 30, 15, 'https://example.com/images/macbook_main.jpg', '["https://example.com/images/macbook_1.jpg"]', 1, 1, 1, 0),

(8, 21, 'ThinkPad X1 Carbon Gen 12', '超轻薄 商务旗舰', 'ThinkPad X1 Carbon 12代，英特尔Ultra 7处理器，14英寸OLED屏。', 11999.00, 13999.00, 50, 25, 'https://example.com/images/thinkpad_main.jpg', '["https://example.com/images/thinkpad_1.jpg"]', 1, 0, 1, 1),

(9, 21, 'Xiaomi Book Pro 14', 'OLED屏 轻薄本', '小米Book Pro 14配备2.8K OLED屏，12代酷睿i5处理器。', 5999.00, 6999.00, 100, 60, 'https://example.com/images/mibook_main.jpg', '["https://example.com/images/mibook_1.jpg"]', 1, 0, 0, 0),

-- 电视类
(10, 31, 'Xiaomi TV S Pro 75英寸', 'MiniLED 4K 144Hz', '小米电视S Pro 75英寸，MiniLED面板，4K 144Hz刷新率。', 7999.00, 9999.00, 20, 8, 'https://example.com/images/xiaomitv_main.jpg', '["https://example.com/images/xiaomitv_1.jpg"]', 1, 1, 0, 0),

-- 冰箱类
(11, 32, '海尔 十字对开门冰箱', '全空间保鲜 阻氧干湿分储', '海尔十字对开门冰箱，全空间保鲜科技，EPP超净系统。', 5999.00, 6999.00, 15, 5, 'https://example.com/images/haier_fridge_main.jpg', '["https://example.com/images/haier_fridge_1.jpg"]', 1, 0, 0, 0),

-- 图书
(12, 4, '深入理解Java虚拟机', 'JVM高级特性与最佳实践', '周志明著，深入讲解JVM原理，Java开发者必备书籍。', 89.00, 129.00, 500, 350, 'https://example.com/images/jvm_book_main.jpg', '["https://example.com/images/jvm_book_1.jpg"]', 1, 0, 0, 0),

-- 服饰
(13, 5, 'Nike Air Force 1', '经典百搭 运动鞋', 'Nike Air Force 1 经典款，白色皮革鞋面。', 899.00, 1099.00, 200, 100, 'https://example.com/images/af1_main.jpg', '["https://example.com/images/af1_1.jpg"]', 1, 1, 1, 0),

-- 下架商品
(14, 11, 'OnePlus 12', '性能旗舰 哈苏影像', '一加12 配备骁龙8 Gen3，哈苏影像系统。', 4299.00, 4799.00, 0, 120, 'https://example.com/images/oneplus12_main.jpg', '["https://example.com/images/oneplus12_1.jpg"]', 0, 0, 0, 0),

-- 审核中商品
(15, 21, 'Dell XPS 16', '4K OLED 高性能创作本', '戴尔XPS 16配备4K OLED屏，酷睿Ultra 9处理器。', 15999.00, 17999.00, 10, 0, 'https://example.com/images/xps16_main.jpg', '["https://example.com/images/xps16_1.jpg"]', 2, 0, 0, 0);



-- 商品 SKU 表
INSERT INTO t_product_sku (id, product_id, sku_code, specs, price, stock, images) VALUES
-- iPhone 15 Pro Max SKU
(1, 1, 'IP15PM-BLK-256', '{"color":"黑色钛金属","storage":"256GB"}', 8999.00, 50, 'https://example.com/images/iphone15_black.jpg'),
(2, 1, 'IP15PM-NAT-256', '{"color":"原色钛金属","storage":"256GB"}', 8999.00, 30, 'https://example.com/images/iphone15_natural.jpg'),
(3, 1, 'IP15PM-BLK-512', '{"color":"黑色钛金属","storage":"512GB"}', 10499.00, 20, 'https://example.com/images/iphone15_black.jpg'),

-- Galaxy S24 Ultra SKU
(4, 2, 'S24U-BLK-256', '{"color":"钛黑","storage":"256GB"}', 7999.00, 40, 'https://example.com/images/s24_black.jpg'),
(5, 2, 'S24U-GRY-512', '{"color":"钛灰","storage":"512GB"}', 8999.00, 30, 'https://example.com/images/s24_gray.jpg'),

-- Xiaomi 14 Pro SKU
(6, 3, 'MI14P-BLK-256', '{"color":"黑色","storage":"256GB"}', 4999.00, 80, 'https://example.com/images/xiaomi14_black.jpg'),
(7, 3, 'MI14P-WHT-512', '{"color":"白色","storage":"512GB"}', 5499.00, 70, 'https://example.com/images/xiaomi14_white.jpg'),

-- Sony WH-1000XM5 SKU
(8, 5, 'SONYXM5-BLK', '{"color":"黑色"}', 2599.00, 100, 'https://example.com/images/sony_xm5_black.jpg'),
(9, 5, 'SONYXM5-SIL', '{"color":"银色"}', 2599.00, 100, 'https://example.com/images/sony_xm5_silver.jpg'),

-- AirPods Pro 2 SKU
(10, 6, 'APP2-USB', '{"color":"白色"}', 1899.00, 200, 'https://example.com/images/airpods_white.jpg'),

-- MacBook Pro SKU
(11, 7, 'MBP16-M3P-32-512', '{"color":"深空灰","chip":"M3 Pro","memory":"32GB","storage":"512GB"}', 19999.00, 20, 'https://example.com/images/macbook_spacegray.jpg'),
(12, 7, 'MBP16-M3P-36-1T', '{"color":"深空黑","chip":"M3 Pro","memory":"36GB","storage":"1TB"}', 21999.00, 10, 'https://example.com/images/macbook_spaceblack.jpg'),

-- ThinkPad X1 Carbon SKU
(13, 8, 'X1C12-ULTRA7-16-512', '{"color":"黑色","chip":"Ultra 7","memory":"16GB","storage":"512GB"}', 11999.00, 30, 'https://example.com/images/thinkpad_black.jpg'),

-- Nike Air Force 1 SKU
(14, 13, 'AF1-WHT-42', '{"color":"白色","size":"42"}', 899.00, 50, 'https://example.com/images/af1_white_42.jpg'),
(15, 13, 'AF1-WHT-43', '{"color":"白色","size":"43"}', 899.00, 50, 'https://example.com/images/af1_white_43.jpg');


-- 订单表

INSERT INTO t_order (id, order_no, user_id, address_id, total_amount, pay_amount, discount_amount, freight_amount, pay_method, order_status, pay_status, pay_time, ship_time, receive_time, finish_time, remark) VALUES
-- 已完成订单
(1, 'ORD202607070001', 2, 1, 8999.00, 8999.00, 0.00, 0.00, 1, 3, 1, '2026-07-07 10:00:00', '2026-07-07 11:00:00', '2026-07-07 14:00:00', '2026-07-07 15:00:00', 'iPhone 15 Pro Max 黑色 256GB'),

(2, 'ORD202607070002', 3, 3, 2599.00, 2599.00, 0.00, 0.00, 2, 3, 1, '2026-07-07 10:30:00', '2026-07-07 12:00:00', '2026-07-07 16:00:00', '2026-07-07 17:00:00', '索尼降噪耳机'),

(3, 'ORD202607070003', 4, 5, 1899.00, 1899.00, 0.00, 0.00, 1, 3, 1, '2026-07-07 11:00:00', '2026-07-07 13:00:00', '2026-07-07 16:30:00', '2026-07-07 17:30:00', 'AirPods Pro 2'),

-- 待收货订单
(4, 'ORD202607070004', 2, 2, 19999.00, 19999.00, 0.00, 0.00, 1, 2, 1, '2026-07-07 12:00:00', '2026-07-07 14:00:00', NULL, NULL, 'MacBook Pro'),

-- 待发货订单
(5, 'ORD202607070005', 3, 4, 4999.00, 4999.00, 0.00, 0.00, 2, 1, 1, '2026-07-07 13:00:00', NULL, NULL, NULL, '小米14 Pro'),

-- 待支付订单
(6, 'ORD202607070006', 5, 6, 7999.00, 7999.00, 0.00, 0.00, 1, 0, 0, NULL, NULL, NULL, NULL, 'Samsung S24 Ultra'),

-- 已取消订单
(7, 'ORD202607070007', 6, 7, 5999.00, 5999.00, 0.00, 0.00, 1, 4, 0, NULL, NULL, NULL, NULL, '小米电视'),

-- 售后中订单
(8, 'ORD202607070008', 7, 8, 899.00, 899.00, 0.00, 0.00, 1, 5, 1, '2026-07-07 09:00:00', '2026-07-07 10:00:00', '2026-07-07 12:00:00', NULL, 'Nike AF1 尺码不合适');



-- 订单明细表
INSERT INTO t_order_item (id, order_id, product_id, sku_id, product_name, sku_specs, product_image, price, quantity, total_amount) VALUES
-- 订单1: iPhone 15 Pro Max
(1, 1, 1, 1, 'Apple iPhone 15 Pro Max', '{"color":"黑色钛金属","storage":"256GB"}', 'https://example.com/images/iphone15_black.jpg', 8999.00, 1, 8999.00),

-- 订单2: Sony耳机
(2, 2, 5, 8, 'Sony WH-1000XM5', '{"color":"黑色"}', 'https://example.com/images/sony_xm5_black.jpg', 2599.00, 1, 2599.00),

-- 订单3: AirPods Pro 2
(3, 3, 6, 10, 'AirPods Pro 2', '{"color":"白色"}', 'https://example.com/images/airpods_white.jpg', 1899.00, 1, 1899.00),

-- 订单4: MacBook Pro (2个SKU)
(4, 4, 7, 11, 'MacBook Pro 16英寸', '{"color":"深空灰","chip":"M3 Pro","memory":"32GB","storage":"512GB"}', 'https://example.com/images/macbook_spacegray.jpg', 19999.00, 1, 19999.00),

-- 订单5: 小米14 Pro
(5, 5, 3, 6, 'Xiaomi 14 Pro', '{"color":"黑色","storage":"256GB"}', 'https://example.com/images/xiaomi14_black.jpg', 4999.00, 1, 4999.00),

-- 订单6: Galaxy S24 Ultra
(6, 6, 2, 4, 'Samsung Galaxy S24 Ultra', '{"color":"钛黑","storage":"256GB"}', 'https://example.com/images/s24_black.jpg', 7999.00, 1, 7999.00),

-- 订单7: 小米电视
(7, 7, 10, NULL, 'Xiaomi TV S Pro 75英寸', NULL, 'https://example.com/images/xiaomitv_main.jpg', 5999.00, 1, 5999.00),

-- 订单8: Nike AF1
(8, 8, 13, 14, 'Nike Air Force 1', '{"color":"白色","size":"42"}', 'https://example.com/images/af1_white_42.jpg', 899.00, 1, 899.00);

