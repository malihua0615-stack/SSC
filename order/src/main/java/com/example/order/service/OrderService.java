package com.example.order.service;

import com.example.common.auth.UserContext;
import com.example.common.dto.CreateOrderDto;
import com.example.common.dto.UserDto;
import com.example.common.entity.OrderEntity;
import com.example.common.entity.ProductEntity;
import com.example.common.entity.UserAddressEntity;
import com.example.common.entity.UserEntity;
import com.example.common.exception.Result;
import com.example.common.openfeign.UserFeignClient;
import com.example.common.util.Util;
import com.example.order.config.RabbitConfig;
import com.example.order.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {
    private final OrderMapper orderMapper;

    private final UserFeignClient userFeignClient;

    private final RabbitTemplate rabbitTemplate;

    @Transactional(rollbackFor = Exception.class)
    public void createOrder(CreateOrderDto createOrderDto) {
        UserEntity userEntity = UserContext.getUserEntity();
//        UserEntity user = createOrderDto.getUser();
        UserDto userDto = new UserDto();
        userDto.setId(userEntity.getId());
        Result<UserAddressEntity> userAddress = userFeignClient.getUserAddress(userDto);
        UserAddressEntity data = userAddress.getData();
        ProductEntity product = createOrderDto.getProduct();

        OrderEntity order = new OrderEntity();
        order.setOrderNo(Util.getOrderNo());
        order.setUserId(userEntity.getId());
        order.setAddressId(data.getId());
        order.setTotalAmount(createOrderDto.getTotalPrice());
        order.setPayAmount(createOrderDto.getPayPrice());
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        orderMapper.insert(order);

        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_NAME, RabbitConfig.ROUTING_KEY, order);
    }


}
