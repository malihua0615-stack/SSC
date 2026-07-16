package com.example.common.openfeign;


import com.example.common.dto.UserDto;
import com.example.common.entity.UserEntity;
import com.example.common.exception.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user", path = "/product")
public interface ProductFeignClient {

    @GetMapping("/getProductById/{id}")
    Result<UserEntity> getUserByUsername(@PathVariable String id);
}
