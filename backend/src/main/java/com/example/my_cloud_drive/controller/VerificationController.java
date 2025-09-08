package com.example.my_cloud_drive.controller;

import com.example.my_cloud_drive.component.MailMsg;
import com.example.my_cloud_drive.exception.BusinessException;
import com.example.my_cloud_drive.pojo.entity.User;
import com.example.my_cloud_drive.repository.UserRepository;
import com.example.my_cloud_drive.result.Result;
import com.example.my_cloud_drive.service.VerificationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@Tag(name = "验证码接口")
@RequestMapping("/verification")
@Slf4j
public class VerificationController {

    @Resource
    private VerificationService verificationService;


    @Resource
    private UserRepository userRepository;

    @PostMapping("/sendEmail")
    public Result<?> sendCode(@RequestParam("email") String email) {
        boolean send = verificationService.sendCode(email);
        if (send) {
            return Result.success("发送成功");
        }
        return Result.error("发送失败");
    }

    @PostMapping("/sendLoginEmail")
    public Result<?> sendCodeLogin(@RequestParam("email") String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            throw new BusinessException("该邮箱未注册，请先注册");
        }
        boolean send = verificationService.sendCode(email);
        if (send) {
            return Result.success("发送成功");
        }
        return Result.error("发送失败");
    }
}
