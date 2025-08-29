package com.example.my_cloud_drive.exception;

public class BusinessException extends RuntimeException{

    private int code;

    public BusinessException(String message) {
        super(message);
        this.code = 400;
    }

    // 传错误码和错误信息（如401：未登录，403：权限不足）
    public BusinessException(String message, int code) {
        super(message);
        this.code = code;
    }

    // 给前端返回错误码
    public int getCode() {
        return code;
    }
}
