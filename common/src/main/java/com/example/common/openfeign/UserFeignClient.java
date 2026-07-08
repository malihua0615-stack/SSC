package com.example.common.openfeign;


import com.example.common.dto.UserDto;
import com.example.common.entity.UserEntity;
import com.example.common.exception.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user", path = "/user")
public interface UserFeignClient {

    @PostMapping("/getUser")
    Result<UserEntity> getUserByUsername(@RequestBody UserDto userDto);
}
