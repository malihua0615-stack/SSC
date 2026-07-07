package com.example.common.entity;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProductEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 商品ID */
    private Long id;

    /** 分类ID */
    private Long categoryId;

    /** 商品名称 */
    private String name;

    /** 副标题/卖点 */
    private String subTitle;

    /** 商品描述 */
    private String description;

    /** 商品价格 */
    private BigDecimal price;

    /** 原价 */
    private BigDecimal originalPrice;

    /** 库存数量 */
    private Integer stock;

    /** 销量 */
    private Integer soldCount;

    /** 主图URL */
    private String mainImage;

    /** 商品图片（JSON数组） */
    private String images;

    /** 状态：0下架 1上架 2审核中 */
    private Integer status;

    /** 是否热卖：0否 1是 */
    private Integer isHot;

    /** 是否新品：0否 1是 */
    private Integer isNew;

    /** 是否推荐：0否 1是 */
    private Integer isRecommend;

    /** 创建时间 */
    private LocalDateTime createdAt;

    /** 更新时间 */
    private LocalDateTime updatedAt;

    /** 逻辑删除：0未删 1已删 */
    private Integer deleted;

    // ========== 关联字段（非数据库字段） ==========

    /** 分类名称（关联查询） */
    private String categoryName;

    /** SKU 列表（关联查询） */
    private List<ProductSkuEntity> skuList;
}
