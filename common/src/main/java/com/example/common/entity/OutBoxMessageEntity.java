package com.example.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("outbox_message")
public class OutBoxMessageEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String messageId;
    private String aggregatorType;
    private String aggregatorId;
    private String eventType;
    private String eventVersion;

    @TableField(typeHandler = com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler.class)
    private Object payload;  // 可以是 Map 或 DTO

    @TableField(typeHandler = com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler.class)
    private Object headers;

    private String topic;
    private String tags;
    private Integer status;
    private Integer retryCount;
    private Integer maxRetryCount;

    private LocalDateTime createdAt;
    private LocalDateTime nextRetryAt;
    private LocalDateTime firstSendAt;
    private LocalDateTime lastSendAt;
    private LocalDateTime sentAt;
    private LocalDateTime updatedAt;

    private String traceId;
    private Long userId;
    private String remark;
    private Integer version;

    // 状态枚举（内部类）
    public enum Status {
        PENDING(0, "待发送"),
        SENDING(1, "发送中"),
        SENT(2, "已发送"),
        FAILED(3, "发送失败"),
        EXPIRED(4, "已过期");

        private final int code;
        private final String desc;

        Status(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() { return code; }
        public String getDesc() { return desc; }
    }

    // 事件类型常量
    public static class EventType {
        public static final String ORDER_CREATED = "ORDER_CREATED";
        public static final String ORDER_PAID = "ORDER_PAID";
        public static final String ORDER_SHIPPED = "ORDER_SHIPPED";
        public static final String ORDER_COMPLETED = "ORDER_COMPLETED";
        public static final String ORDER_CANCELLED = "ORDER_CANCELLED";
        public static final String ORDER_TIMEOUT = "ORDER_TIMEOUT";
    }

    // 聚合根类型
    public static class AggregatorType {
        public static final String ORDER = "ORDER";
        public static final String PAYMENT = "PAYMENT";
        public static final String REFUND = "REFUND";
    }
}