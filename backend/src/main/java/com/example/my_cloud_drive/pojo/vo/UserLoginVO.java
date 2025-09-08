package com.example.my_cloud_drive.pojo.vo;

import lombok.Data;

/**
 * 登录成功返回的视图对象
 */
@Data
public class UserLoginVO {
    private Long userId;
    private String username;//登录用户名
    private String nickname;//用户昵称（优先显示昵称，更友好）
    private String avatar;//用户头像URL
    private String token;// Sa-Token 生成的令牌
    private Long tokenExpireTime;// Token 过期时间单位：毫秒）
}
