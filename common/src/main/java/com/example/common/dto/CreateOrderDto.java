package com.example.common.dto;

import com.example.common.entity.ProductEntity;
import com.example.common.entity.UserEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CreateOrderDto {

    private BigDecimal totalPrice;

    private BigDecimal payPrice;

    private List<ProductDto> products;
}
