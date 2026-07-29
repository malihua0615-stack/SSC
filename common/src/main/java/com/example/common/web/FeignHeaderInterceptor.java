package com.example.common.web;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

//这个非常重要.
//这个是在openfeign请求的时候不走网关导致的网关写的功能失效.
//目前就是为了解决网关过滤器解析jwt 生成消息头的问题.
@Configuration
public class FeignHeaderInterceptor {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return (RequestTemplate template) -> {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                String userId = request.getHeader("SSC-User-Id");
                String username = request.getHeader("SSC-User-Name");
                if (userId != null) {
                    template.header("SSC-User-Id", userId);
                }
                if (username != null) {
                    template.header("SSC-User-Name", username);
                }
            }
        };
    }
}