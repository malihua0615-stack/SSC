package com.example.common.exception;

import lombok.Getter;
import org.springframework.util.StringUtils;
@Getter
public class Result<T> {
    private Integer code;
    private String message;
    private T data;

    public Result() {}
    public Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
    public static <T> Result<T> success(T data) {
        return new Result<>(200,"success",data);
    }

    public static <T> Result<T> success(String message) {
        return new Result<>(200,message,null);
    }

    public static <T> Result<T> error(Integer code, String message) {
        return new Result<>(code,message,null);
    }

    public boolean isSuccess() {
        return !StringUtils.hasText(message);
    }


}
