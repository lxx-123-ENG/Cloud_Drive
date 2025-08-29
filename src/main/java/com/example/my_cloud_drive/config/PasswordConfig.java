package com.example.my_cloud_drive.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


/**
 * 注册BCrypt密码加密器
 * BCrypt算法特点：
 * 1. 自动生成随机盐值（每次加密结果不同）
 * 2. 加密过程不可逆（只能通过matches方法校验）
 */
@Configuration
public class PasswordConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
