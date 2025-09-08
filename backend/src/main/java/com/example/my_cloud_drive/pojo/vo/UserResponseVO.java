package com.example.my_cloud_drive.pojo.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserResponseVO {
    private Long id; // 用户ID
    private String username; // 用户名
    private String nickname; // 昵称
    private String email; // 邮箱
    private String phone; // 手机号
    private Long totalStorage; // 总存储空间
    private Long usedStorage; // 已用存储空间
    private LocalDateTime registerTime; // 注册时间
    private LocalDateTime lastLoginTime; // 最后登录时间
}
