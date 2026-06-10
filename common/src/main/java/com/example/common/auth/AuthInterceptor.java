package com.example.common.auth;

import com.example.common.entity.UserEntity;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userId = request.getHeader("SSC-User-Id");
        String username = request.getHeader("SSC-User-Name");
        String role = request.getHeader("SSC-User-Role");
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);
        userEntity.setUsername(username);
        userEntity.setRole(role);
        UserContext.setUserEntity(userEntity);
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
        UserContext.removeUserEntity();
    }
}
