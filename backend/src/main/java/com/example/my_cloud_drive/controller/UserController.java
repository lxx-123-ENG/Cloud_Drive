package com.example.my_cloud_drive.controller;

import com.example.my_cloud_drive.exception.BusinessException;
import com.example.my_cloud_drive.pojo.dto.UserLoginDTO;
import com.example.my_cloud_drive.pojo.dto.UserRegisterDTO;
import com.example.my_cloud_drive.pojo.vo.UserResponseVO;
import com.example.my_cloud_drive.result.Result;
import com.example.my_cloud_drive.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/user")
@Tag(name = "用户端基本操作接口")
@Slf4j
public class UserController {
    @Resource
    private UserService userService;
    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public Result register(@RequestBody UserRegisterDTO userRegisterDTO) {
        log.info("用户注册：{}", userRegisterDTO);
        // 调用service层注册用户
        try {
            userService.register(userRegisterDTO);
            return Result.success();
        } catch (BusinessException e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public Result<UserResponseVO> login(@RequestBody UserLoginDTO userLoginDTO, HttpServletRequest request) {
        log.info("用户登录：{}", userLoginDTO);
        // 调用service层登录用户
        try {
            UserResponseVO userResponseVO = userService.login(userLoginDTO,request);
            return Result.success("登录成功",userResponseVO);
        } catch (BusinessException e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "邮箱登录")
    @PostMapping("/loginByEmail")
    public Result<UserResponseVO> loginByEmail(@RequestBody UserLoginDTO userLoginDTO, HttpServletRequest request) {
        log.info("邮箱登录：{}", userLoginDTO);
        // 调用service层登录用户
        try {
            UserResponseVO userResponseVO = userService.loginByEmail(userLoginDTO,request);
            return Result.success("登录成功",userResponseVO);
        } catch (BusinessException e) {
            return Result.error(e.getMessage());
        }
    }
}
