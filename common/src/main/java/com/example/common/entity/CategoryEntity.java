package com.example.common.entity;


import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 商品分类实体类
 * 对应表：t_category
 */
@Data
public class CategoryEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 分类ID */
    private Long id;

    /** 父分类ID，0表示顶级 */
    private Long parentId;

    /** 分类名称 */
    private String name;

    /** 排序 */
    private Integer sortOrder;

    /** 图标URL */
    private String icon;

    /** 状态：0禁用 1正常 */
    private Integer status;

    /** 创建时间 */
    private LocalDateTime createdAt;

    /** 更新时间 */
    private LocalDateTime updatedAt;

    /** 逻辑删除：0未删 1已删 */
    private Integer deleted;

    // ========== 关联字段（非数据库字段） ==========

    /** 父分类名称（关联查询） */
    private String parentName;

    /** 子分类列表（关联查询） */
    private List<CategoryEntity> children;

    /** 分类层级（用于展示） */
    private Integer level;

    /**
     * 判断是否为顶级分类
     */
    public boolean isRoot() {
        return parentId == null || parentId == 0;
    }

    /**
     * 判断是否启用
     */
    public boolean isEnabled() {
        return status != null && status == 1;
    }

    /**
     * 判断是否有子分类
     */
    public boolean hasChildren() {
        return children != null && !children.isEmpty();
    }
}