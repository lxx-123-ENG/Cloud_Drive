package com.example.my_cloud_drive.result;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private int code;//状态码

    private String message;//提示信息

    private T data;//业务数据

    //成功（无业务数据）
    public static <T> Result<T> success() {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("操作成功");
        result.setData(null);
        return result;
    }

    //成功（有业务数据）
    public static <T> Result<T>success(T data){
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("操作成功");
        result.setData(data);
        return result;
    }

    //成功（自定义提示信息+业务数据）
    public static <T> Result<T> success(String message, T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage(message);
        result.setData(data);
        return result;
    }
    public static <T> Result<T> error( String message) {
        Result<T> result = new Result<>();
        result.setCode(400);
        result.setMessage(message);
        result.setData(null);
        return result;
    }

    //失败（仅错误码+提示信息，无数据）
    public static <T> Result<T> error(int code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        result.setData(null);
        return result;
    }

    //失败（仅错误码+提示信息，由数据）
    public static <T> Result<T> error(int code, String message, T data) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        result.setData(data);
        return result;
    }
}
