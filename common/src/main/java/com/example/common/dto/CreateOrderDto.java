package com.example.common.dto;

import com.example.common.entity.ProductEntity;
import com.example.common.entity.UserEntity;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateOrderDto {

    private BigDecimal totalPrice;

    private BigDecimal payPrice;

    private UserEntity user;

    private ProductEntity product;
}
