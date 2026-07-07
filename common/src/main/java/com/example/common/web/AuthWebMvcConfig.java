package com.example.common.web;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnClass(WebMvcConfigurer.class)
public class AuthWebMvcConfig implements WebMvcConfigurer {

    @PostConstruct
    public void init(){
        log.info("AuthWebMvcConfig has been initialized....");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        WebMvcConfigurer.super.addInterceptors(registry);
        registry.addInterceptor(new AuthInterceptor())
                .addPathPatterns("/**")//拦截的路径
                .excludePathPatterns("/auth/login")//排除的路径
                .order(1);
        log.info("✅ UserInfoInterceptor 已注册（WebMVC 环境）");
    }
}
