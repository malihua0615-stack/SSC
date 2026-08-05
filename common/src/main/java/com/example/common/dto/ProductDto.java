package com.example.common.dto;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j
@Data
public class ProductDto {

    private Long id;

    private String name;

    private Integer quantity;

    private BigDecimal price;

    private BigDecimal totalPrice;
}
