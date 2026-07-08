package com.example.common.entity;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 秒杀订单实体类
 * 对应表：t_seckill_order
 */
@Data
public class SeckillOrderEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 秒杀订单ID */
    private Long id;

    /** 订单ID（关联 t_order） */
    private Long orderId;

    /** 秒杀商品ID（关联 t_seckill_product） */
    private Long seckillId;

    /** 用户ID */
    private Long userId;

    /** 秒杀价格 */
    private BigDecimal seckillPrice;

    /** 秒杀数量 */
    private Integer quantity;

    /** 创建时间 */
    private LocalDateTime createdAt;

    // ========== 关联字段（非数据库字段） ==========

    /** 订单信息（关联查询） */
    private OrderEntity order;

    /** 秒杀商品信息（关联查询） */
    private SeckillProductEntity seckillProduct;

    /** 商品信息（关联查询） */
    private ProductEntity product;

    /** 用户名（关联查询） */
    private String username;
}