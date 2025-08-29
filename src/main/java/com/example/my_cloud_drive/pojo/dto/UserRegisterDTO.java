package com.example.my_cloud_drive.pojo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegisterDTO {

    @NotBlank(message = "用户名不能为空")
    @Size(min = 4, max = 20, message = "用户名长度必须在 4-20 之间")
    private String username;

    @NotBlank (message = "密码不能为空")
    @Size (min = 6, max = 30, message = "密码长度必须在 6-30 之间")
    private String password;

    @NotBlank (message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    @NotBlank(message = "名称不能为空")
    private String nikeName;

}
