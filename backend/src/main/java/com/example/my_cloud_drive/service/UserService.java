package com.example.my_cloud_drive.service;

import com.example.my_cloud_drive.pojo.dto.UserDTO;
import com.example.my_cloud_drive.pojo.vo.UserLoginVO;
import com.example.my_cloud_drive.pojo.vo.UserResponseVO;
import jakarta.servlet.http.HttpServletRequest;

public interface UserService {

    boolean register(UserDTO userRegisterDTO);

    UserLoginVO login(UserDTO userDTO, HttpServletRequest request);

    UserLoginVO loginByEmail(UserDTO userDTO, HttpServletRequest request);

    void updateInfo(UserDTO userDTO);
}
