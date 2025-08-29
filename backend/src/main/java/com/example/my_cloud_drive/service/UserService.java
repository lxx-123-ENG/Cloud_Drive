package com.example.my_cloud_drive.service;

import com.example.my_cloud_drive.pojo.dto.UserLoginDTO;
import com.example.my_cloud_drive.pojo.dto.UserRegisterDTO;
import com.example.my_cloud_drive.pojo.vo.UserResponseVO;
import jakarta.servlet.http.HttpServletRequest;

public interface UserService {

    boolean register(UserRegisterDTO userRegisterDTO);

    UserResponseVO login(UserLoginDTO userLoginDTO, HttpServletRequest request);

    UserResponseVO loginByEmail(UserLoginDTO userLoginDTO, HttpServletRequest request);
}
