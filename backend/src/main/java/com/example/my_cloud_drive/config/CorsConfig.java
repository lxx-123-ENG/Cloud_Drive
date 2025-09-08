package com.example.my_cloud_drive.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration // 标记为配置类，让Spring扫描到
public class CorsConfig {

    @Bean // 创建CorsFilter实例，注入Spring容器
    public CorsFilter corsFilter() {
        // 1. 配置跨域规则
        CorsConfiguration config = new CorsConfiguration();

        // 允许前端的具体地址（前端实际的IP+端口，不能用）
        config.addAllowedOriginPattern("*");

        // 允许携带Cookie/Token（登录后需要携带Token，必须设为true）
        config.setAllowCredentials(true);

        // 允许所有请求方法（GET/POST/PUT/DELETE等）
        config.addAllowedMethod("*");

        // 允许所有请求头（如Authorization、Content-Type等）
        config.addAllowedHeader("*");

        // 2. 配置哪些接口需要跨域（/** 表示所有接口）
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        // 3. 返回跨域过滤器
        return new CorsFilter(source);
    }
}