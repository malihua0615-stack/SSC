package com.example.common.web;

import com.example.common.auth.UserContext;
import com.example.common.entity.UserEntity;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class AuthInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userId = request.getHeader("SSC-User-Id");
        String username = request.getHeader("SSC-User-Name");
        UserEntity userEntity = new UserEntity();
        userEntity.setId(Long.parseLong(userId));
        userEntity.setUsername(username);
        UserContext.setUserEntity(userEntity);
        log.debug("成功设置到threadLocal 里面了。。。。ID:{}",userId);
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
        UserContext.removeUserEntity();
    }
}
