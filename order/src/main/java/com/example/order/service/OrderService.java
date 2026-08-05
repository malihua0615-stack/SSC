package com.example.order.service;

import com.example.common.auth.UserContext;
import com.example.common.dto.CreateOrderDto;
import com.example.common.dto.ProductDto;
import com.example.common.dto.UserDto;
import com.example.common.entity.*;
import com.example.common.exception.BusinessException;
import com.example.common.exception.Result;
import com.example.common.openfeign.ProductFeignClient;
import com.example.common.openfeign.UserFeignClient;
import com.example.common.util.RandomUtils;
import com.example.common.util.Util;
import com.example.order.config.RabbitConfig;
import com.example.order.mapper.OrderItemMapper;
import com.example.order.mapper.OrderMapper;
import com.example.order.mapper.OutBoxMessageMapper;
import com.example.order.util.OrderNoGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
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

    private final StringRedisTemplate redisTemplate;

    private final OutBoxMessageMapper outBoxMessageMapper;

    private final ProductFeignClient productFeignClient;

    private final OrderNoGenerator orderNoGenerator;


    private UserAddressEntity getUserAddress() {
        UserEntity userEntity = UserContext.getUserEntity();
        UserDto userDto = new UserDto();
        userDto.setId(userEntity.getId());
        Result<UserAddressEntity> userAddress = userFeignClient.getUserAddress(userDto);
        return userAddress.getData();
    }

    @Transactional(rollbackFor = Exception.class)
    public void createOrder(Long userId) throws InterruptedException {
        UserDto userDto = new UserDto();
        userDto.setId(userId);
        Result<UserAddressEntity> userAddressEntityResult = userFeignClient.getUserAddress(userDto);
        if (userAddressEntityResult.getData() == null) {
            log.info("获取用户地址失败,还没有配置用户地址，用户{}", userDto.getId());
//            throw new BusinessException("获取用户地址失败");
            return;
        }
        UserAddressEntity userAddress = userAddressEntityResult.getData();
        CreateOrderDto createOrderDto = autoCreateOrder();


        OrderEntity order = new OrderEntity();
        order.setOrderNo(orderNoGenerator.getOrderNo());
        order.setUserId(userId);
        order.setAddressId(userAddress.getId());
        order.setTotalAmount(createOrderDto.getTotalPrice());
        order.setPayAmount(createOrderDto.getPayPrice());
        order.setRemark("压力测试");
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        orderMapper.insert(order);

        List<OrderItemEntity> orderItems = new ArrayList<>();
        for (ProductDto product : createOrderDto.getProducts()) {

            //库存扣减
            Result<ProductEntity> productById = productFeignClient.getProductById(String.valueOf(product.getId()));
            if (productById.getData() == null) {
                throw new BusinessException("获取商品信息失败，商品ID：" + product.getId());
            }
            ProductEntity data = productById.getData();
            data.setStock(data.getStock() - product.getQuantity());
            if (data.getStock() < 0) {
                throw new BusinessException("库存不足，商品Name：" + product.getName());
            }

            Result<Integer> integerResult = productFeignClient.updateProduct(data);
            if (integerResult.getData() == 0) {
                throw new BusinessException("更新商品库存失败，商品ID：" + product.getId());
            }

            OrderItemEntity orderItem = new OrderItemEntity();
            orderItem.setOrderId(order.getId());
            orderItem.setProductId(product.getId());
            orderItem.setProductName(product.getName());
            orderItem.setQuantity(product.getQuantity());
            orderItem.setPrice(product.getPrice());
            orderItem.setTotalAmount(product.getTotalPrice());
            orderItem.setCreatedAt(LocalDateTime.now());
            orderItem.setUpdatedAt(LocalDateTime.now());

            orderItems.add(orderItem);
        }
        if (!orderItems.isEmpty()) {
            orderItemMapper.insertBatchSomeColumn(orderItems);
        }else {
            log.info("商品库存告罄！无法创建订单");
            orderMapper.deleteById(order);
        }


//        Thread.sleep(1000); // 模拟延迟

    }

    @Transactional(rollbackFor = Exception.class)
    public void insertOrderInfo(CreateOrderDto createOrderDto,UserAddressEntity userAddress) {
        UserEntity userEntity = UserContext.getUserEntity();


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
    }

    public boolean deductStock(long productId, int quantity) {
        String stockKey = "product:stock:" + productId;
        Object o = redisTemplate.opsForValue().get(stockKey);
        if (o == null) {
            //库存不存在，直接返回false
            return false;
        }

        Long decrement = redisTemplate.opsForValue().decrement(stockKey, quantity);
        if (decrement < 0){
            //库存不足，回滚库存
            redisTemplate.opsForValue().increment(stockKey, quantity);
            return false;
        }
        return true;
    }

    public void createOrder(CreateOrderDto createOrderDto) {
        UserAddressEntity userAddress = getUserAddress();

        //库存扣减
        for (ProductDto product : createOrderDto.getProducts()) {
            boolean deductStock = deductStock(product.getId(), product.getQuantity());
            if (!deductStock) {
                throw new BusinessException("库存不足，商品ID：" + product.getId());
            }
        }

        insertOrderInfo(createOrderDto, userAddress);
        //todo 2.查询商品表原价格，不能直接使用传过来的价格。 后面再优化

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
            productDto.setName(productEntity.getName());
            if (productEntity.getStock() == 0) continue;
            int i = RandomUtils.randomInt(1, 3);
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
