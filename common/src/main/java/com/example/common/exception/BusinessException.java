package com.example.common.exception;

import com.example.common.constant.ErrorCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException{
    private Integer code;
    private String message;

    public BusinessException(ErrorCode code, String message){
        super(message);
        this.code = code.getCode();
        this.message=message;
    }

    public BusinessException(ErrorCode code){
        super(code.getMessage());
        this.code = code.getCode();
        this.message=code.getMessage();
    }

    public BusinessException(Integer code, String message){
        super(message);
        this.code = code;
        this.message=message;
    }
}
