package com.example.common.entity;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderItemEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 订单明细ID */
    private Long id;

    /** 订单ID */
    private Long orderId;

    /** 商品ID */
    private Long productId;

    /** SKU ID */
    private Long skuId;

    /** 商品名称（快照） */
    private String productName;

    /** SKU规格（快照） */
    private String skuSpecs;

    /** 商品图片（快照） */
    private String productImage;

    /** 购买单价 */
    private BigDecimal price;

    /** 购买数量 */
    private Integer quantity;

    /** 总价 */
    private BigDecimal totalAmount;

    /** 创建时间 */
    private LocalDateTime createdAt;

    /** 更新时间 */
    private LocalDateTime updatedAt;
}