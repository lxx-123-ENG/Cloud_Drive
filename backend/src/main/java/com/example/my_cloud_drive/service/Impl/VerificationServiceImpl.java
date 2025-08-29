package com.example.my_cloud_drive.service.Impl;

import com.example.my_cloud_drive.component.MailMsg;
import com.example.my_cloud_drive.service.VerificationService;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class VerificationServiceImpl implements VerificationService {
    @Resource
    private MailMsg mailMsg;

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public boolean sendCode(String email) {
        return mailMsg.SendCode(email);
    }

    @Override
    public boolean verifyCode(String email, String code) {
        String codeInRedis = (String) redisTemplate.opsForValue().get(email);
        return code.equals(codeInRedis);
    }
}
