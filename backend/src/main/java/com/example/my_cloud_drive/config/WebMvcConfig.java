package com.example.my_cloud_drive.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(new SaInterceptor(handle -> {
                    // 校验是否已登录（未登录会自动抛出 401 异常）
                    StpUtil.checkLogin();
                }))
                .addPathPatterns("/**") // 拦截所有接口
                .excludePathPatterns("/user/user/login") // 放行登录接口
                .excludePathPatterns("/user/user/register") // 放行注册接口
                .excludePathPatterns("/user/user/loginByEmail") // 放行邮箱登录接口
                .excludePathPatterns("/verification/sendEmail") // 放行发送验证码接口
                .excludePathPatterns("/verification/sendLoginEmail") // 放行发送登录验证码接口
                .excludePathPatterns("/swagger-ui.html")
                .excludePathPatterns("/swagger-ui/**")// 放行swagger测试
                .excludePathPatterns("/v3/api-docs/**")// 放行API文档数据路径
                .excludePathPatterns("/doc.html");// 放行knife4j文档
    }
}
