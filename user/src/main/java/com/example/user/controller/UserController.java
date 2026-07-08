package com.example.user.controller;

import com.example.common.entity.UserEntity;
import com.example.common.exception.Result;
import com.example.common.dto.UserDto;
import com.example.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    @RequestMapping("/getUser")
    public Result<UserEntity> getUser(@RequestBody UserDto userDto){
        UserEntity userByName = userService.getUserByName(userDto.getUsername());
        return Result.success(userByName);
    }
}
