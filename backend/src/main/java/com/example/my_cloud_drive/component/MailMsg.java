package com.example.my_cloud_drive.component;

import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class MailMsg {
    @Resource
    private JavaMailSenderImpl javaMailSender;

    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private CodeProudct codeProudct;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public boolean SendCode(String email) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            String code = codeProudct.createCode();
            helper.setText("您的验证码为：" + code + "，5分钟内有效");
            helper.setSubject("Cloud_Drive----验证码");
            helper.setFrom(fromEmail);
            helper.setTo(email);
            javaMailSender.send(mimeMessage);
            redisTemplate.opsForValue().set(email, code, 5, TimeUnit.MINUTES);
            return true;
        } catch (MessagingException e) {
            return false;
        }
    }

}
