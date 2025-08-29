package com.example.my_cloud_drive.pojo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "login_log")
public class LoginLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String ip;

    @Column(nullable = false)
    private String browser;

    @Column(nullable = false)
    private String os;

    @Column(nullable = false)
    private String device;

    @Column(nullable = false)
    private LocalDateTime loginTime;
}
