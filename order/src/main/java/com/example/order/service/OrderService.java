package com.example.order.service;

import com.example.common.auth.UserContext;
import com.example.common.dto.CreateOrderDto;
import com.example.common.dto.ProductDto;
import com.example.common.dto.UserDto;
import com.example.common.entity.*;
import com.example.common.exception.Result;
import com.example.common.openfeign.ProductFeignClient;
import com.example.common.openfeign.UserFeignClient;
import com.example.common.util.RandomUtils;
import com.example.common.util.Util;
import com.example.order.config.RabbitConfig;
import com.example.order.mapper.OrderItemMapper;
import com.example.order.mapper.OrderMapper;
import com.example.order.mapper.OutBoxMessageMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {
    private final OrderMapper orderMapper;

    private final UserFeignClient userFeignClient;

    private final RabbitTemplate rabbitTemplate;

    private final OrderItemMapper orderItemMapper;

    private final OutBoxMessageMapper outBoxMessageMapper;

    private final ProductFeignClient productFeignClient;

    private UserAddressEntity getUserAddress() {
        UserEntity userEntity = UserContext.getUserEntity();
        UserDto userDto = new UserDto();
        userDto.setId(userEntity.getId());
        Result<UserAddressEntity> userAddress = userFeignClient.getUserAddress(userDto);
        return userAddress.getData();
    }


    @Transactional(rollbackFor = Exception.class)
    public void createOrder(CreateOrderDto createOrderDto) {
        UserEntity userEntity = UserContext.getUserEntity();

        //这里远程调用需要放到事务外面，不然会占用事务，导致资源浪费。
        UserAddressEntity userAddress = getUserAddress();

        //todo 2.查询商品表原价格，不能直接使用传过来的价格。 后面再优化


        //订单主表插入
        OrderEntity order = new OrderEntity();
        order.setOrderNo(Util.getOrderNo());
        order.setUserId(userEntity.getId());
        order.setAddressId(userAddress.getId());
        order.setTotalAmount(createOrderDto.getTotalPrice());
        order.setPayAmount(createOrderDto.getPayPrice());
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        orderMapper.insert(order);

        //订单明细插入
        List<OrderItemEntity> orderItems = new ArrayList<>();
        for (ProductDto product : createOrderDto.getProducts()) {
            OrderItemEntity orderItem = new OrderItemEntity();
            orderItem.setOrderId(order.getId());
            orderItem.setProductId(product.getId());
            orderItem.setQuantity(product.getQuantity());
            orderItem.setPrice(product.getPrice());
            orderItem.setTotalAmount(product.getTotalPrice());
            orderItems.add(orderItem);
        }
        orderItemMapper.insertBatchSomeColumn(orderItems);

        //发件箱插入
        OutBoxMessageEntity outBoxMessageEntity = new OutBoxMessageEntity();
        outBoxMessageEntity.setUserId(userEntity.getId());
        outBoxMessageEntity.setMessageId(UUID.randomUUID().toString());
        outBoxMessageEntity.setAggregatorType(OutBoxMessageEntity.AggregatorType.ORDER);
        outBoxMessageEntity.setAggregatorId(String.valueOf(order.getId()));
        outBoxMessageEntity.setEventType(OutBoxMessageEntity.EventType.ORDER_CREATED);
        outBoxMessageEntity.setEventVersion("1.0");
        outBoxMessageEntity.setPayload(createOrderDto);
        outBoxMessageEntity.setHeaders(null);
        outBoxMessageEntity.setTopic("Test");
//        outBoxMessageEntity.setTags();
        outBoxMessageEntity.setStatus(OutBoxMessageEntity.Status.PENDING.getCode());
        outBoxMessageEntity.setRetryCount(0);
        outBoxMessageEntity.setMaxRetryCount(5);
        outBoxMessageEntity.setCreatedAt(LocalDateTime.now());
        outBoxMessageEntity.setUpdatedAt(LocalDateTime.now());
        outBoxMessageEntity.setVersion(1);
        outBoxMessageMapper.insert(outBoxMessageEntity);

        //可以先测试一下。
        //写一个定时扫描的去扫这个发件箱 mq生产者
        //写一个消费者 去消费，注意需要添加死信队列。

    }


    public CreateOrderDto autoCreateOrder(){
        Result<List<ProductEntity>> allProducts = productFeignClient.getAllProducts();
        List<ProductEntity> data = allProducts.getData();
        int size = data.size();
        List<Integer> integers = RandomUtils.randomDistinctInts(0, size - 1, 3);
        List<ProductDto> getProducts = new ArrayList<>();
        BigDecimal totalPrice = new BigDecimal("0");
        //随机取商品，随机取数量
        for (Integer integer : integers) {
            ProductEntity productEntity = data.get(integer);
            ProductDto productDto = new ProductDto();
            productDto.setId(productEntity.getId());
            productDto.setPrice(productEntity.getPrice());
            if (productEntity.getStock() == 0) continue;
            int i = RandomUtils.randomInt(1, productEntity.getStock());
            productDto.setQuantity(i);
            BigDecimal multiply = productEntity.getPrice().multiply(new BigDecimal(i));
            productDto.setTotalPrice(multiply);
            totalPrice = totalPrice.add(multiply);
            getProducts.add(productDto);
        }
        CreateOrderDto createOrderDto = new CreateOrderDto();
        createOrderDto.setTotalPrice(totalPrice);
        createOrderDto.setPayPrice(totalPrice);
        createOrderDto.setProducts(getProducts);
        return createOrderDto;
    }


}
