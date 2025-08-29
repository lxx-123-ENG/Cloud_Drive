package com.example.my_cloud_drive.pojo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserLoginDTO {

    private String username;

    private String password;

    private String email;

    private String code;
}
