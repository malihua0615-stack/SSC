package com.example.common.entity;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class SeckillProductEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 秒杀商品ID */
    private Long id;

    /** 商品ID */
    private Long productId;

    /** SKU ID */
    private Long skuId;

    /** 秒杀价格 */
    private BigDecimal seckillPrice;

    /** 秒杀库存 */
    private Integer seckillStock;

    /** 每人限购数量 */
    private Integer seckillLimit;

    /** 秒杀开始时间 */
    private LocalDateTime startTime;

    /** 秒杀结束时间 */
    private LocalDateTime endTime;

    /** 状态：0未开始 1进行中 2已结束 3已取消 */
    private Integer status;

    /** 创建时间 */
    private LocalDateTime createdAt;

    /** 更新时间 */
    private LocalDateTime updatedAt;

    /** 逻辑删除：0未删 1已删 */
    private Integer deleted;

    // ========== 关联字段（非数据库字段） ==========

    /** 商品信息（关联查询） */
    private ProductEntity product;

    /** 秒杀进度（实时计算） */
    private Integer progress;
}