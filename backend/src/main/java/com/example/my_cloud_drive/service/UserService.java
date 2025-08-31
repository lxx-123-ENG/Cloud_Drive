package com.example.my_cloud_drive.service;

import com.example.my_cloud_drive.pojo.dto.UserDTO;
import com.example.my_cloud_drive.pojo.vo.UserResponseVO;
import jakarta.servlet.http.HttpServletRequest;

public interface UserService {

    boolean register(UserDTO userRegisterDTO);

    UserResponseVO login(UserDTO userDTO, HttpServletRequest request);

    UserResponseVO loginByEmail(UserDTO userDTO, HttpServletRequest request);

    void updateInfo(UserDTO userDTO);
}
