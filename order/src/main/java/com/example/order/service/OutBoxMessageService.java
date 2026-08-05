package com.example.order.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.common.entity.OutBoxMessageEntity;
import com.example.order.config.RabbitConfig;
import com.example.order.mapper.OutBoxMessageMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutBoxMessageService {
    /**
     * 注意点：
     * 1.需要拿锁，不然多线程抢资源不行。
     * 2.拿锁后需要注意在删除锁的时候保证删除的是自己的锁。
     * 3.在发送mq 的时候需要注意要保证这条数据只有一个线程在操作，不然多个线程都会发mq。
     * 4.在发送或者更新失败的时候需要回滚。
     */

    private static final int BATCH_SIZE = 50;  // 每次最多处理50条
    private static final int MAX_RETRY_COUNT = 3;  // 最大重试次数

    private final OutBoxMessageMapper outBoxMessageMapper;

    private final RabbitTemplate rabbitTemplate;

    private final RedisTemplate<String, Object> redisTemplate;

    private final StringRedisTemplate stringRedisTemplate;

    public void scanOutBoxMessages() {
        String key = "outbox:schedule:lock";
        UUID uuid = UUID.randomUUID();
        //分布式锁
        Boolean b = redisTemplate.opsForValue().setIfAbsent(key, uuid.toString(), 10, TimeUnit.SECONDS);
        if (!Boolean.TRUE.equals(b)) {
            log.debug("其他实例正在运行，跳过本次");
            return;
        }
        try{
            doScanOutBoxMessages();
        }finally {
            //KEYS[1] 就是传入的key 队列的第一个参数，ARGV[1] 是入参的第一个
            //本意就是在删除的时候需要判断一下这个是不是当前的uuid 如果不是就没法删除
            String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            stringRedisTemplate.execute(new DefaultRedisScript<>(script, Long.class), Collections.singletonList(key), uuid.toString());
        }

    }

    private void doScanOutBoxMessages() {
        LambdaQueryWrapper<OutBoxMessageEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OutBoxMessageEntity::getEventType, OutBoxMessageEntity.EventType.ORDER_CREATED);
        queryWrapper.eq(OutBoxMessageEntity::getAggregatorType, OutBoxMessageEntity.AggregatorType.ORDER);
        queryWrapper.eq(OutBoxMessageEntity::getStatus, OutBoxMessageEntity.Status.PENDING.getCode());
        queryWrapper.orderByAsc(OutBoxMessageEntity::getCreatedAt);
        queryWrapper.last("limit " + BATCH_SIZE);//限制每次查询的条数
        List<OutBoxMessageEntity> outBoxMessageEntities = outBoxMessageMapper.selectList(queryWrapper);

        if (CollectionUtils.isEmpty(outBoxMessageEntities)) {
            return;
        }
        for (OutBoxMessageEntity outBoxMessageEntity : outBoxMessageEntities) {
                //这里先更新拿状态，然后再发送，如果发送失败那么就回滚，并记录发送次数
                //如果后面数据量大了，那么建议添加分布式锁，减少无效的查询，提交效率。
            try {
                // 更新状态为已处理
                outBoxMessageEntity.setStatus(OutBoxMessageEntity.Status.SENDING.getCode());
                int i = outBoxMessageMapper.updateById(outBoxMessageEntity);
                if (i <= 0) {
                    log.debug("更新消息状态失败,已经被其他线程更新了，messageId: {}", outBoxMessageEntity.getMessageId());
                    continue; // 如果更新失败，说明可能被其他线程处理了，跳过
                }
                rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_NAME, RabbitConfig.ROUTING_KEY, outBoxMessageEntity);


            }catch (Exception e){
                //失败就设置重试。
                if (outBoxMessageEntity.getRetryCount() >= MAX_RETRY_COUNT) {
                    //超过重试次数 标记发送失败。
                    outBoxMessageEntity.setStatus(OutBoxMessageEntity.Status.FAILED.getCode());
                } else {
                    //发送失败，增加重试次数
                    outBoxMessageEntity.setRetryCount(outBoxMessageEntity.getRetryCount() + 1);
                    outBoxMessageEntity.setStatus(OutBoxMessageEntity.Status.PENDING.getCode());
                }
                outBoxMessageMapper.updateById(outBoxMessageEntity);
                log.error("发送消息失败，messageId: {}, error: {}", outBoxMessageEntity.getMessageId(), e.getMessage(), e);
            }
        }
    }

    public void consumeMessage(OutBoxMessageEntity outBoxMessageEntity) {
        log.info("Received Message: {}", outBoxMessageEntity);


    }
}
