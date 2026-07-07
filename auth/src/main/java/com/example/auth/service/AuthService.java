package com.example.auth.service;

import com.example.common.entity.UserEntity;
import com.example.common.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class AuthService {

    private final RedisTemplate<String,String> redisTemplate;
    private final String USER_TOKEN_PREFIX = "user:token:";

    public AuthService(RedisTemplate<String, String> redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    public String login(String username, String password){
        UserEntity userEntity = UserEntity.getUser(username);
        if (userEntity != null){
            if (userEntity.getPassword().equals(password)){
                String token = JwtUtil.getToken(userEntity);
                log.info("token:{}",token);
                System.out.println(token);
                redisTemplate.opsForValue().set(USER_TOKEN_PREFIX,token,7, TimeUnit.DAYS);
                return token;
            }
        }
        return "Login Failed";
    }

    public String getToken(String username){
        return  redisTemplate.opsForValue().get(USER_TOKEN_PREFIX+username);
    }

    public void deleteToken(String username){
        redisTemplate.delete(USER_TOKEN_PREFIX+username);
    }
}
