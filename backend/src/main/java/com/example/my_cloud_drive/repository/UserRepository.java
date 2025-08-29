package com.example.my_cloud_drive.repository;

import com.example.my_cloud_drive.pojo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 根据用户名查询用户（登录、注册校验用）
    Optional<User> findByUsername(String username);

    // 根据邮箱查询用户（用于邮箱验证、找回密码）
    Optional<User> findByEmail(String email);

    // 根据手机号查询用户（登录、注册校验用）
    Optional<User> findByPhone(String phone);

    // 校验用户名是否已存在（注册时避免重复）
    boolean existsByUsername(String username);

    // 校验邮箱是否已存在（注册时避免重复）
    boolean existsByEmail(String email);

    // 校验手机号是否已存在(注册时避免重复）
    boolean existsByPhone(String phone);
}
