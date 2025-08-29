package com.example.my_cloud_drive.pojo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Integer version;  // 乐观锁字段

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(length = 50)
    private String nickname;

    @Column(unique = true, length = 100)
    private String email;

    @Column(length = 20)
    private String phone;

    @Column(length = 255)
    private String avatar;

    @Column(nullable = false)
    private Integer status = 1; // 1-正常，0-禁用

    @Column(nullable = false)
    private Long totalStorage = 10737418240L; // 默认10GB

    @Column(nullable = false)
    private Long usedStorage = 0L;

    @Column(nullable = false, updatable = false)
    private LocalDateTime registerTime;  // 业务相关的时间字段

    private LocalDateTime lastLoginTime;

    @PrePersist//实体对象被持久化到数据库之前需要执行的方法
    public void prePersist() {
        if (registerTime == null) {
            registerTime = LocalDateTime.now();
        }
    }
}
