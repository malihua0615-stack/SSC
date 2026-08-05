package com.example.order.util;

import com.example.common.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class OrderNoGenerator {

    private final StringRedisTemplate redisTemplate;

    private static final String ORDER_NO_KEY = "order:no:";

    public String getOrderNo() {
        String orderNo = Util.getOrderNo();
        Long increment = redisTemplate.opsForValue().increment(ORDER_NO_KEY);
        redisTemplate.expire(ORDER_NO_KEY,2, TimeUnit.DAYS);
        //补齐15位
        String format = String.format("%09d", increment);
        return orderNo + format;
    }
}
