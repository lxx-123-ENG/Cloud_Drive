package com.example.my_cloud_drive.exception;

import cn.dev33.satoken.exception.NotLoginException;
import com.example.my_cloud_drive.result.Result;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

/**
 * 全局异常处理器：所有Controller层的异常都会被这里捕获处理
 */
@RestControllerAdvice // 作用于所有@RestController，捕获异常并返回JSON
public class GlobalExceptionHandler {

    // 处理 Sa-Token 未登录异常
    @ExceptionHandler(NotLoginException.class)
    public Result<Void> handleNotLoginException(NotLoginException e) {
        // 返回 401 状态码 + 未登录提示
        return Result.error(401, "请先登录");
    }

    // 2. 处理业务异常（已知错误，如“用户名已存在”）
    @ExceptionHandler(BusinessException.class)
    public Result<?> handleBusinessException(BusinessException e) {
        // 直接用Result.fail包装错误码和信息
        return Result.error(e.getCode(), e.getMessage());
    }

    // 3. 处理资源未找到异常（如“用户ID不存在”）
    @ExceptionHandler(ResourceNotFoundException.class)
    public Result<?> handleResourceNotFoundException(ResourceNotFoundException e) {
        // 复用父类的错误码（404）和信息
        return Result.error(e.getCode(), e.getMessage());
    }

    // 4. 处理其他未知异常（兜底）
    @ExceptionHandler(Exception.class)
    public Result<?> handleOtherException(Exception e) {
        // 生产环境建议记录日志，这里简化处理
        return Result.error(500, "服务器内部错误，请联系管理员");
    }
}
