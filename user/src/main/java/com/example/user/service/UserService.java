package com.example.user.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.common.entity.UserAddressEntity;
import com.example.common.entity.UserEntity;
import com.example.common.exception.BusinessException;
import com.example.user.mapper.UserAddressMapper;
import com.example.user.mapper.UserMapper;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {


    private final UserMapper userMapper;
    private final UserAddressMapper userAddressMapper;

    private final RedisTemplate<String,String> redisTemplate;


    public UserAddressEntity getUserAddress(Long userId) {
        LambdaQueryWrapper<UserAddressEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserAddressEntity::getUserId, userId)
                .eq(UserAddressEntity::getIsDefault,"0");
        redisTemplate.opsForValue().set("test","111");
        return userAddressMapper.selectOne(queryWrapper);
    }

    public UserEntity getUserByName(String userName){
        log.info("getUserByName userName:{}",userName);
        if (!StringUtils.hasText(userName)){
            throw new BusinessException("用户名不能为空！");
        }
        LambdaQueryWrapper<UserEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserEntity::getUsername, userName);
        return userMapper.selectOne(queryWrapper);
    }

    public UserEntity getUserById(Long id){
        log.info("getUserById Id:{}",id);
        if (id == null){
            throw new BusinessException("用户ID不能为空！");
        }
        return userMapper.selectById(id);
    }

    public boolean insertUser(UserEntity userEntity){
        if (userEntity == null){
            throw new BusinessException("传入的用户为空");
        }

        if (!StringUtils.hasText(userEntity.getUsername())){
            throw new BusinessException("用户名不能为空！");
        }

        if (!StringUtils.hasText(userEntity.getPassword())){
            throw new BusinessException("用户名不能为空！");
        }

        return userMapper.insert(userEntity) > 0;
    }
}
