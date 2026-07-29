package com.example.product.service;

import com.example.common.entity.ProductEntity;
import com.example.common.exception.BusinessException;
import com.example.product.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ProductMapper productMapper;

//    private final RedisTemplate<String,Object> redisTemplate;

    public ProductEntity getProductById(Long id) {
        if (id == null) {
            throw new BusinessException("商品Id 不能为空！！");
        }
        return productMapper.selectById(id);
    }

    public List<ProductEntity> getAllProducts() {
        List<ProductEntity> productEntities = productMapper.selectList(null);
        log.debug("获取所有商品信息: {}", productEntities);
        return  productEntities;

    }


}
