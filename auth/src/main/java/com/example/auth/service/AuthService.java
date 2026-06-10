package com.example.auth.service;

import com.example.common.entity.UserEntity;
import com.example.common.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthService {

    public String login(String username, String password){
        UserEntity userEntity = UserEntity.getUser(username);
        if (userEntity != null){
            if (userEntity.getPassword().equals(password)){
                String token = JwtUtil.getToken(userEntity);
                log.info("token:{}",token);
                System.out.println(token);
                return token;
            }
        }
        return "Login Failed";
    }
}
