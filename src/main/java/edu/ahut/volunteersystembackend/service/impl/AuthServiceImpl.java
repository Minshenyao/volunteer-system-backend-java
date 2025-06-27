package edu.ahut.volunteersystembackend.service.impl;

import edu.ahut.volunteersystembackend.dao.UserRepository;
import edu.ahut.volunteersystembackend.dto.user.UserLoginRequest;
import edu.ahut.volunteersystembackend.dto.user.UserRegisterRequest;
import edu.ahut.volunteersystembackend.model.User;
import edu.ahut.volunteersystembackend.service.AuthService;
import edu.ahut.volunteersystembackend.utils.JwtUtils;
import edu.ahut.volunteersystembackend.utils.UserConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Long register(UserRegisterRequest userRegisterRequest) {
        User users = userRepository.findByEmail(userRegisterRequest.getEmail());
        if (users != null) {
            throw new RuntimeException("Email: " + userRegisterRequest.getEmail() + "已被注册!");
        }
        User user = UserConverter.convertUserRegisterReqistToUser(userRegisterRequest);
        user.setCreateAt(System.currentTimeMillis());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return user.getId();
    }

    @Override
    public String login(UserLoginRequest userLoginRequest) {
        User user = userRepository.findByEmail(userLoginRequest.getEmail());
        if (user != null) {
            if (passwordEncoder.matches(userLoginRequest.getPassword(), user.getPassword())){
                user.setLastLoginTime(System.currentTimeMillis());
                userRepository.save(user);
                // 创建带有自定义声明的JWT
                Map<String, Object> claims = new HashMap<>();
                claims.put("id", user.getId());
                claims.put("nickname", user.getNickname());
                claims.put("email", user.getEmail());
                return JwtUtils.createToken("login", claims);
            } else {
                throw new RuntimeException("密码错误!");
            }
        } else {
            throw new RuntimeException("Email: " + userLoginRequest.getEmail() + "不存在!");
        }
    }
}
