package com.example.common.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        log.error("系统出现错误:{}",e.getMessage());
        return new Result<>(500,e.getMessage(),null);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(BusinessException.class)
    public Result<Void> handleException(BusinessException e) {
        log.error("业务错误：{}",e.getMessage());
        return new Result<>(e.getCode(),e.getMessage(),null);
    }


}

