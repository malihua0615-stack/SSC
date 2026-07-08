package com.example.auth.service;

import com.example.common.entity.UserEntity;
import com.example.common.exception.BusinessException;
import com.example.common.exception.Result;
import com.example.common.openfeign.UserFeignClient;
import com.example.common.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final RedisTemplate<String,String> redisTemplate;
    private final String USER_TOKEN_PREFIX = "user:token:";
    private final UserFeignClient userFeignClient;


    public String login(String username, String password){
        com.example.common.dto.UserDto userDto = new com.example.common.dto.UserDto();
        userDto.setUsername(username);
        Result<UserEntity> userByUsername = userFeignClient.getUserByUsername(userDto);
        if(userByUsername.isSuccess()){
            throw new BusinessException(userByUsername.getMessage());
        }
        UserEntity userEntity = userByUsername.getData();
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
