CREATE TABLE t_user (
                        id              BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
                        username        VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
                        password        VARCHAR(255) NOT NULL COMMENT '密码（BCrypt加密）',
                        email           VARCHAR(100) COMMENT '邮箱',
                        phone           VARCHAR(20) COMMENT '手机号',
                        avatar          VARCHAR(255) COMMENT '头像URL',
                        nickname        VARCHAR(50) COMMENT '昵称',
                        real_name       VARCHAR(50) COMMENT '真实姓名',
                        gender          TINYINT DEFAULT 0 COMMENT '性别：0未知 1男 2女',
                        birthday        DATE COMMENT '生日',
                        status          TINYINT DEFAULT 1 COMMENT '状态：0禁用 1正常',
                        created_at      DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                        updated_at      DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                        deleted         TINYINT DEFAULT 0 COMMENT '逻辑删除：0未删 1已删',
                        INDEX idx_username (username),
                        INDEX idx_phone (phone),
                        INDEX idx_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

CREATE TABLE t_user_address (
                                id              BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '地址ID',
                                user_id         BIGINT NOT NULL COMMENT '用户ID',
                                receiver_name   VARCHAR(50) NOT NULL COMMENT '收货人姓名',
                                receiver_phone  VARCHAR(20) NOT NULL COMMENT '收货人电话',
                                province        VARCHAR(50) NOT NULL COMMENT '省',
                                city            VARCHAR(50) NOT NULL COMMENT '市',
                                district        VARCHAR(50) NOT NULL COMMENT '区/县',
                                detail_address  VARCHAR(255) NOT NULL COMMENT '详细地址',
                                zip_code        VARCHAR(10) COMMENT '邮政编码',
                                is_default      TINYINT DEFAULT 0 COMMENT '是否默认：0否 1是',
                                created_at      DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                updated_at      DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                deleted         TINYINT DEFAULT 0 COMMENT '逻辑删除：0未删 1已删',
                                INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户收货地址表';

CREATE TABLE t_category (
                            id              BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '分类ID',
                            parent_id       BIGINT DEFAULT 0 COMMENT '父分类ID，0表示顶级',
                            name            VARCHAR(50) NOT NULL COMMENT '分类名称',
                            sort_order      INT DEFAULT 0 COMMENT '排序',
                            icon            VARCHAR(255) COMMENT '图标URL',
                            status          TINYINT DEFAULT 1 COMMENT '状态：0禁用 1正常',
                            created_at      DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            updated_at      DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                            deleted         TINYINT DEFAULT 0 COMMENT '逻辑删除：0未删 1已删',
                            INDEX idx_parent_id (parent_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品分类表';


CREATE TABLE t_product (
                           id              BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '商品ID',
                           category_id     BIGINT NOT NULL COMMENT '分类ID',
                           name            VARCHAR(200) NOT NULL COMMENT '商品名称',
                           sub_title       VARCHAR(500) COMMENT '副标题/卖点',
                           description     TEXT COMMENT '商品描述',
                           price           DECIMAL(10,2) NOT NULL COMMENT '商品价格',
                           original_price  DECIMAL(10,2) COMMENT '原价',
                           stock           INT DEFAULT 0 COMMENT '库存数量',
                           sold_count      INT DEFAULT 0 COMMENT '销量',
                           main_image      VARCHAR(255) COMMENT '主图URL',
                           images          TEXT COMMENT '商品图片（JSON数组）',
                           status          TINYINT DEFAULT 1 COMMENT '状态：0下架 1上架 2审核中',
                           is_hot          TINYINT DEFAULT 0 COMMENT '是否热卖：0否 1是',
                           is_new          TINYINT DEFAULT 0 COMMENT '是否新品：0否 1是',
                           is_recommend    TINYINT DEFAULT 0 COMMENT '是否推荐：0否 1是',
                           created_at      DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                           updated_at      DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                           deleted         TINYINT DEFAULT 0 COMMENT '逻辑删除：0未删 1已删',
                           INDEX idx_category_id (category_id),
                           INDEX idx_name (name),
                           INDEX idx_status (status),
                           INDEX idx_is_hot (is_hot),
                           INDEX idx_is_new (is_new),
                           INDEX idx_is_recommend (is_recommend)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';


CREATE TABLE t_product_sku (
                               id              BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'SKU ID',
                               product_id      BIGINT NOT NULL COMMENT '商品ID',
                               sku_code        VARCHAR(100) NOT NULL UNIQUE COMMENT 'SKU编码',
                               specs           VARCHAR(500) NOT NULL COMMENT '规格描述（JSON）',
                               price           DECIMAL(10,2) NOT NULL COMMENT 'SKU价格',
                               stock           INT DEFAULT 0 COMMENT 'SKU库存',
                               images          VARCHAR(255) COMMENT 'SKU图片',
                               created_at      DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                               updated_at      DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                               deleted         TINYINT DEFAULT 0 COMMENT '逻辑删除：0未删 1已删',
                               INDEX idx_product_id (product_id),
                               INDEX idx_sku_code (sku_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品SKU表';


CREATE TABLE t_product_spec (
                                id              BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '规格ID',
                                product_id      BIGINT NOT NULL COMMENT '商品ID',
                                spec_name       VARCHAR(100) NOT NULL COMMENT '规格名称（如：颜色、尺寸）',
                                spec_values     TEXT NOT NULL COMMENT '规格值（JSON数组）',
                                created_at      DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                updated_at      DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                INDEX idx_product_id (product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品规格表';


CREATE TABLE t_order (
                         id              BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '订单ID',
                         order_no        VARCHAR(32) NOT NULL UNIQUE COMMENT '订单编号',
                         user_id         BIGINT NOT NULL COMMENT '用户ID',
                         address_id      BIGINT NOT NULL COMMENT '收货地址ID',
                         total_amount    DECIMAL(10,2) NOT NULL COMMENT '订单总金额',
                         pay_amount      DECIMAL(10,2) NOT NULL COMMENT '实付金额',
                         discount_amount DECIMAL(10,2) DEFAULT 0 COMMENT '优惠金额',
                         freight_amount  DECIMAL(10,2) DEFAULT 0 COMMENT '运费',
                         pay_method      TINYINT DEFAULT 1 COMMENT '支付方式：1微信 2支付宝 3余额',
                         order_status    TINYINT DEFAULT 0 COMMENT '订单状态：0待支付 1待发货 2待收货 3已完成 4已取消 5售后中',
                         pay_status      TINYINT DEFAULT 0 COMMENT '支付状态：0未支付 1已支付 2支付失败',
                         pay_time        DATETIME COMMENT '支付时间',
                         ship_time       DATETIME COMMENT '发货时间',
                         receive_time    DATETIME COMMENT '收货时间',
                         cancel_time     DATETIME COMMENT '取消时间',
                         finish_time     DATETIME COMMENT '完成时间',
                         remark          VARCHAR(500) COMMENT '订单备注',
                         created_at      DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                         updated_at      DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                         deleted         TINYINT DEFAULT 0 COMMENT '逻辑删除：0未删 1已删',
                         INDEX idx_order_no (order_no),
                         INDEX idx_user_id (user_id),
                         INDEX idx_order_status (order_status),
                         INDEX idx_pay_status (pay_status),
                         INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';


CREATE TABLE t_order_item (
                              id              BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '订单明细ID',
                              order_id        BIGINT NOT NULL COMMENT '订单ID',
                              product_id      BIGINT NOT NULL COMMENT '商品ID',
                              sku_id          BIGINT COMMENT 'SKU ID',
                              product_name    VARCHAR(200) NOT NULL COMMENT '商品名称（快照）',
                              sku_specs       VARCHAR(500) COMMENT 'SKU规格（快照）',
                              product_image   VARCHAR(255) COMMENT '商品图片（快照）',
                              price           DECIMAL(10,2) NOT NULL COMMENT '购买单价',
                              quantity        INT NOT NULL COMMENT '购买数量',
                              total_amount    DECIMAL(10,2) NOT NULL COMMENT '总价',
                              created_at      DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                              updated_at      DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                              INDEX idx_order_id (order_id),
                              INDEX idx_product_id (product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单明细表';


CREATE TABLE t_seckill_product (
                                   id              BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '秒杀商品ID',
                                   product_id      BIGINT NOT NULL COMMENT '商品ID',
                                   sku_id          BIGINT COMMENT 'SKU ID',
                                   seckill_price   DECIMAL(10,2) NOT NULL COMMENT '秒杀价格',
                                   seckill_stock   INT NOT NULL COMMENT '秒杀库存',
                                   seckill_limit   INT DEFAULT 1 COMMENT '每人限购数量',
                                   start_time      DATETIME NOT NULL COMMENT '秒杀开始时间',
                                   end_time        DATETIME NOT NULL COMMENT '秒杀结束时间',
                                   status          TINYINT DEFAULT 1 COMMENT '状态：0未开始 1进行中 2已结束 3已取消',
                                   created_at      DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                   updated_at      DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                   deleted         TINYINT DEFAULT 0 COMMENT '逻辑删除：0未删 1已删',
                                   INDEX idx_product_id (product_id),
                                   INDEX idx_start_end_time (start_time, end_time),
                                   INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='秒杀商品表';

CREATE TABLE t_seckill_order (
                                 id              BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '秒杀订单ID',
                                 order_id        BIGINT NOT NULL COMMENT '订单ID',
                                 seckill_id      BIGINT NOT NULL COMMENT '秒杀商品ID',
                                 user_id         BIGINT NOT NULL COMMENT '用户ID',
                                 seckill_price   DECIMAL(10,2) NOT NULL COMMENT '秒杀价格',
                                 quantity        INT NOT NULL COMMENT '秒杀数量',
                                 created_at      DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 INDEX idx_order_id (order_id),
                                 INDEX idx_seckill_id (seckill_id),
                                 INDEX idx_user_id (user_id),
                                 UNIQUE KEY uk_user_seckill (user_id, seckill_id) COMMENT '防止重复秒杀'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='秒杀订单表';
