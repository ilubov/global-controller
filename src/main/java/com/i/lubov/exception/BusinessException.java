package com.i.lubov.exception;

public class BusinessException extends RuntimeException {
    private Integer code;

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return this.code;
    }
}
