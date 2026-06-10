package com.example.auth.controller;

import com.example.auth.service.AuthService;
import com.example.common.entity.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;


    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public String login(@RequestBody UserEntity userEntity){
        String login = authService.login(userEntity.getUsername(), userEntity.getPassword());
        log.debug("controller:login:return:{}",login);
        return login;
    }
}
