package com.example.common.openfeign;


import com.example.common.entity.ProductEntity;
import com.example.common.exception.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "product", path = "/product")
public interface ProductFeignClient {

    @GetMapping("/getProductById/{id}")
    Result<ProductEntity> getProductById(@PathVariable("id") String id);

    @GetMapping("/getAllProducts")
    Result<List<ProductEntity>> getAllProducts();

    @GetMapping("/updateProduct")
    Result<Integer> updateProduct(@RequestBody ProductEntity productEntityList);
}
