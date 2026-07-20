package com.example.common.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

//这个类的作用就是让字符串传入redis 的时候不会乱码。
//需要注意数据内容还是乱码，因为redis-cli 是不会解析中文的。
@Configuration
public class RedisConfig {

    @PostConstruct
    public void init(){
        System.out.println("loading RedisConfig......");
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        //这里是为了让时间能存进reids
        // 创建自定义 ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        // 注册 Java 8 时间类型支持
        objectMapper.registerModule(new JavaTimeModule());
        // 禁用将日期序列化为时间戳（使用 ISO-8601 格式）
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);


        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(objectMapper);

        // 设置 Key 和 HashKey 使用 String 序列化
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        // 设置 Value 和 HashValue 使用 JSON 序列化
        template.setValueSerializer(serializer);
        template.setHashValueSerializer(serializer);

        template.afterPropertiesSet();
        return template;
    }


}
