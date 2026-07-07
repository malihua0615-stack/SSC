package com.example.common.entity;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProductSkuEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /** SKU ID */
    private Long id;

    /** 商品ID */
    private Long productId;

    /** SKU编码 */
    private String skuCode;

    /** 规格描述（JSON） */
    private String specs;

    /** SKU价格 */
    private BigDecimal price;

    /** SKU库存 */
    private Integer stock;

    /** SKU图片 */
    private String images;

    /** 创建时间 */
    private LocalDateTime createdAt;

    /** 更新时间 */
    private LocalDateTime updatedAt;

    /** 逻辑删除：0未删 1已删 */
    private Integer deleted;
}
