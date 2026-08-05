package com.example.order.consumer;

import com.example.common.entity.OutBoxMessageEntity;
import com.example.order.config.RabbitConfig;

import com.example.order.service.OutBoxMessageService;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderConsumer {

    private final OutBoxMessageService  outBoxMessageService;

//    @RabbitListener(queues = RabbitConfig.QUEUE_NAME)
    public void handleOrder(OutBoxMessageEntity outBoxMessageEntity, Channel channel, Message message) {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        // 消费信息
        log.info("Received Order Message: {}", outBoxMessageEntity);
        try{
            outBoxMessageService.consumeMessage(outBoxMessageEntity);
            // 手动确认消息
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            log.error("订单消费失败: {}", outBoxMessageEntity, e);
            try {
                channel.basicNack(deliveryTag, false, true);
            } catch (Exception ex) {
                log.error("Error occurred while rejecting message: {}", outBoxMessageEntity, ex);
            }
        }

    }

}
