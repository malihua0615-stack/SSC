package com.example.product.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.common.entity.ProductEntity;
import com.example.product.mapper.ProductMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductCacheService {

    private final ProductMapper productMapper;

    private final RedisTemplate<String, Object> redisTemplate;


    @PostConstruct
    public void initProductCache() {
        loadProductCache();

    }


    public void loadProductCache() {
        List<ProductEntity> productEntities = productMapper.selectList(new QueryWrapper<>());

        for (ProductEntity productEntity : productEntities) {
            String PRODUCT_CACHE = "PRODUCT_CACHE";
            redisTemplate.opsForHash().put(PRODUCT_CACHE, productEntity.getId().toString(), productEntity);
            //缓存商品库存
            String STOCK_CACHE = "STOCK_CACHE:";
            redisTemplate.opsForValue().set(STOCK_CACHE + productEntity.getId().toString(), productEntity.getStock());
        }
        log.info("成功加载商品信息，总量：{}",productEntities.size());

    }




}
