package com.example.common.entity;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class UserEntity {
    private String id;
    private String username;
    private String password;
    private String role;

    public static UserEntity getUser(String username) {
        Map<String,UserEntity> map = new HashMap<>();
        UserEntity  userEntity = new UserEntity();
        userEntity.setUsername("admin");
        userEntity.setPassword("PassWoRd1123+-");
        userEntity.setId("10001");
        userEntity.setRole("admin");
        map.put(userEntity.getUsername(),userEntity);
        return map.get(username);
    }
}
