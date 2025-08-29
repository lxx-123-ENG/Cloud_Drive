package com.example.my_cloud_drive.controller;

import com.example.my_cloud_drive.component.MailMsg;
import com.example.my_cloud_drive.result.Result;
import com.example.my_cloud_drive.service.VerificationService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/verification")
public class VerificationController {

    @Resource
    private VerificationService verificationService;
    @Resource
    private MailMsg mailMsg;

    @PostMapping("/sendEmail")
    public Result sendCode(String email) {
        boolean send = verificationService.sendCode(email);
        if (send) {
            return Result.success("发送成功");
        }
        return Result.error("发送失败");
    }

}
