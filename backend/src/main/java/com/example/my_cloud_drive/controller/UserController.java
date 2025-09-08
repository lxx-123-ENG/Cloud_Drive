package com.example.my_cloud_drive.controller;

import com.example.my_cloud_drive.exception.BusinessException;
import com.example.my_cloud_drive.pojo.dto.UserDTO;

import com.example.my_cloud_drive.pojo.vo.UserLoginVO;
import com.example.my_cloud_drive.pojo.vo.UserResponseVO;
import com.example.my_cloud_drive.result.Result;
import com.example.my_cloud_drive.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
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
    public Result register(@RequestBody UserDTO userDTO) {
        log.info("用户注册：{}", userDTO);
        // 调用service层注册用户
        try {
            userService.register(userDTO);
            return Result.success();
        } catch (BusinessException e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public Result<UserLoginVO> login(@RequestBody UserDTO userDTO, HttpServletRequest request) {
        log.info("用户登录：{}", userDTO);
        // 调用service层登录用户
        try {
            UserLoginVO userLoginVO = userService.login(userDTO,request);
            return Result.success("登录成功",userLoginVO);
        } catch (BusinessException e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "邮箱登录")
    @PostMapping("/loginByEmail")
    public Result<UserLoginVO> loginByEmail(@RequestBody UserDTO userDTO, HttpServletRequest request) {
        log.info("邮箱登录：{}", userDTO);
        // 调用service层登录用户
        try {
            UserLoginVO userLoginVO = userService.loginByEmail(userDTO,request);
            return Result.success("登录成功",userLoginVO);
        } catch (BusinessException e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary="修改用户信息")
    @PostMapping("/update")
    public Result update(@RequestBody UserDTO userDTO) {
        log.info("修改用户信息：{}", userDTO);
        // 调用service层修改用户信息
        try {
            userService.updateInfo(userDTO);
            return Result.success();
        } catch (BusinessException e) {
            return Result.error(e.getMessage());
        }
    }
}
