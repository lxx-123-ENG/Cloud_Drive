package com.example.my_cloud_drive.service.Impl;

import com.example.my_cloud_drive.exception.BusinessException;
import com.example.my_cloud_drive.pojo.dto.UserDTO;
import com.example.my_cloud_drive.pojo.entity.LoginLog;
import com.example.my_cloud_drive.pojo.entity.User;
import com.example.my_cloud_drive.pojo.vo.UserResponseVO;
import com.example.my_cloud_drive.repository.LoginLogRepository;
import com.example.my_cloud_drive.repository.UserRepository;
import com.example.my_cloud_drive.service.UserService;
import com.example.my_cloud_drive.service.VerificationService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserRepository userRepository;

    @Resource
    private LoginLogRepository loginLogRepository;

    @Resource
    private PasswordEncoder passwordEncoder; // 密码加密器

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private VerificationService verificationService;

    @Override
    public boolean register(UserDTO userDTO) {

        // 1. 校验参数
        if (userDTO == null) {
            throw new BusinessException("用户注册参数不能为空");
        }

        // 2. 校验用户名是否重复，校验邮箱是否重复
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new BusinessException("用户名已存在");
        }
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new BusinessException("邮箱已存在");
        }

        // 3. 密码加密
        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
        // 4. 插入数据库
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        user.setPassword(encodedPassword);
        user.setRegisterTime(LocalDateTime.now());
        try {
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            throw new BusinessException("用户注册失败");
        }
    }

    @Override
    @Transactional
    public UserResponseVO login(UserDTO userDTO, HttpServletRequest request) {
        // 1. 校验参数
        if (userDTO == null) {
            throw new BusinessException("用户登录参数不能为空");
        }
        // 2. 校验用户名是否存在
        Optional<User> user = userRepository.findByUsername(userDTO.getUsername());
        if (user.isEmpty()) {
            throw new BusinessException("用户名不存在");
        }
        // 3. 检验该用户名对应的密码与登录时输入的密码是否一致
        boolean match=passwordEncoder.matches(userDTO.getPassword(),user.get().getPassword());
        if(!match){
            throw new BusinessException("用户名或密码错误");
        }

        // 4. 校验账号状态（新增：防止禁用账号登录）
        if (user.get().getStatus() == 0) { // 假设0为禁用状态
            throw new BusinessException("账号已被禁用，请联系管理员",403);
        }

        // 5. 登录成功，更新用户信息
        user.get().setLastLoginTime(LocalDateTime.now());
        userRepository.save(user.get());

        // 6. 组装登录日志
        LoginLog log = new LoginLog();
        log.setUserId(user.get().getId());
        log.setLoginTime(LocalDateTime.now());


        // 6-1 IP 地址
        log.setIp(request.getRemoteAddr());

        // 6-2 浏览器、操作系统、设备
        String ua = request.getHeader("User-Agent");
        if (StringUtils.hasText(ua)) {
            eu.bitwalker.useragentutils.UserAgent agent =
                    eu.bitwalker.useragentutils.UserAgent.parseUserAgentString(ua);
            log.setBrowser(agent.getBrowser().getName());
            log.setOs(agent.getOperatingSystem().getName());
            log.setDevice(agent.getOperatingSystem().getDeviceType().getName());
        }else {
            log.setBrowser("未知");
            log.setOs("未知");
            log.setDevice("未知");
        }

        loginLogRepository.save(log);

        UserResponseVO userResponseVO=new UserResponseVO();
        BeanUtils.copyProperties(user.get(),userResponseVO);
        return userResponseVO;
    }


    @Override
    @Transactional
    public UserResponseVO loginByEmail(UserDTO userDTO, HttpServletRequest request) {
        String code = userDTO.getCode();
        if (code == null) {
            throw new BusinessException("验证码不存在");
        }
        boolean verify = verificationService.verifyCode(userDTO.getEmail(), code);
        if (!verify) {
            throw new BusinessException("验证码错误");
        }

        //删去对应的redis的值
        redisTemplate.delete(userDTO.getEmail());

        Optional<User> user = userRepository.findByEmail(userDTO.getEmail());

        // 4. 登录成功，更新用户信息
        user.get().setLastLoginTime(LocalDateTime.now());
        userRepository.save(user.get());

        // 6. 组装登录日志
        LoginLog log = new LoginLog();
        log.setUserId(user.get().getId());
        log.setLoginTime(LocalDateTime.now());


        // 6-1 IP 地址
        log.setIp(request.getRemoteAddr());

        // 6-2 浏览器、操作系统、设备
        String ua = request.getHeader("User-Agent");
        if (StringUtils.hasText(ua)) {
            eu.bitwalker.useragentutils.UserAgent agent =
                    eu.bitwalker.useragentutils.UserAgent.parseUserAgentString(ua);
            log.setBrowser(agent.getBrowser().getName());
             log.setOs(agent.getOperatingSystem().getName());
            log.setDevice(agent.getOperatingSystem().getDeviceType().getName());
        }else {
            log.setBrowser("未知");
            log.setOs("未知");
            log.setDevice("未知");
        }

        loginLogRepository.save(log);

        UserResponseVO userResponseVO=new UserResponseVO();
        BeanUtils.copyProperties(user.get(),userResponseVO);
        return userResponseVO;
    }

    @Override
    public void updateInfo(UserDTO userDTO) {
        String password = userDTO.getPassword();
        String encodedPassword = passwordEncoder.encode(password);
        userDTO.setPassword(encodedPassword);
        Optional<User> user = userRepository.findByUsername(userDTO.getUsername());
        if (user.isEmpty()) {
            throw new BusinessException("用户不存在");
        }
        BeanUtils.copyProperties(userDTO,user.get());
        userRepository.save(user.get());
    }
}
