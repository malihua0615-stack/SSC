package com.example.order.controller;

import com.example.common.dto.CreateOrderDto;
import com.example.common.exception.Result;
import com.example.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/order")
@Slf4j
@RequiredArgsConstructor
public class OrderController {


    private final OrderService orderService;

    @PostMapping("/createOrder")
    public Result createOrder(@RequestBody CreateOrderDto createOrderDto) {
        createOrderDto.setPayPrice(new BigDecimal(50));
        createOrderDto.setTotalPrice(new BigDecimal(500));
        orderService.createOrder(createOrderDto);
        return Result.success("OK");
    }
}
