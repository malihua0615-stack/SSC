package com.example.common.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 订单ID */
    private Long id;

    /** 订单编号 */
    private String orderNo;

    /** 用户ID */
    private Long userId;

    /** 收货地址ID */
    private Long addressId;

    /** 订单总金额 */
    private BigDecimal totalAmount;

    /** 实付金额 */
    private BigDecimal payAmount;

    /** 优惠金额 */
    private BigDecimal discountAmount;

    /** 运费 */
    private BigDecimal freightAmount;

    /** 支付方式：1微信 2支付宝 3余额 */
    private Integer payMethod;

    /** 订单状态：0待支付 1待发货 2待收货 3已完成 4已取消 5售后中 */
    private Integer orderStatus;

    /** 支付状态：0未支付 1已支付 2支付失败 */
    private Integer payStatus;

    /** 支付时间 */
    private LocalDateTime payTime;

    /** 发货时间 */
    private LocalDateTime shipTime;

    /** 收货时间 */
    private LocalDateTime receiveTime;

    /** 取消时间 */
    private LocalDateTime cancelTime;

    /** 完成时间 */
    private LocalDateTime finishTime;

    /** 订单备注 */
    private String remark;

    /** 创建时间 */
    private LocalDateTime createdAt;

    /** 更新时间 */
    private LocalDateTime updatedAt;

    /** 逻辑删除：0未删 1已删 */
    private Integer deleted;

    // ========== 关联字段（非数据库字段） ==========

    /** 收货地址信息（关联查询） */
    private UserAddressEntity address;

    /** 订单明细列表（关联查询） */
    private List<OrderItemEntity> orderItems;

    /** 用户名（关联查询） */
    private String username;
}
