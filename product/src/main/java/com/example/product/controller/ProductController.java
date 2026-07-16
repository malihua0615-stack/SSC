package com.example.product.controller;

import com.example.common.entity.ProductEntity;
import com.example.common.exception.Result;
import com.example.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/getProductById/{id}")
    public Result<ProductEntity> getProductById(@PathVariable String id) {
        ProductEntity productById = productService.getProductById(Long.parseLong(id));
        return Result.success(productById);
    }
}
