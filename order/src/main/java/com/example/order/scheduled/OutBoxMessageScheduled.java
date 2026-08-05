package com.example.order.scheduled;

import com.example.order.service.OutBoxMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OutBoxMessageScheduled {

    private final OutBoxMessageService outBoxMessageService;

    @Scheduled(cron = "0/5 * * * * ?") // 每10秒执行一次
    public void processOutBoxMessages() {
        // 调用 OutBoxMessageService 的方法来处理 OutBox 消息
        outBoxMessageService.scanOutBoxMessages();
    }
}
