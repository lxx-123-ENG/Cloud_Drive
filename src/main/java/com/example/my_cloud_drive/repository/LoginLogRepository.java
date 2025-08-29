package com.example.my_cloud_drive.repository;

import com.example.my_cloud_drive.pojo.entity.LoginLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoginLogRepository extends JpaRepository<LoginLog, Long> {
    // 根据用户ID查询登录日志
    List<LoginLog> findByUserId(Long userId);

}
