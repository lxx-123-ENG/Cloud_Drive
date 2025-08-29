package com.example.my_cloud_drive.service;

public interface VerificationService {
    boolean sendCode(String email);
    boolean verifyCode(String email, String code);
}
