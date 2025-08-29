package com.example.my_cloud_drive.component;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class CodeProudct {
    public String createCode(){
        int length=6;//默认验证码长度为6
        StringBuilder code = new StringBuilder();
        String str = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        Random random=new Random();
        for(int i=0;i<length;i++){
            int index=random.nextInt(str.length());
            char c=str.charAt(index);
            code.append(c);
        }
        return code.toString();
    }
}
