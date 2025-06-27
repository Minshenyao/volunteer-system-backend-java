package edu.ahut.volunteersystembackend.service;

import edu.ahut.volunteersystembackend.dto.user.UserLoginRequest;
import edu.ahut.volunteersystembackend.dto.user.UserRegisterRequest;

public interface AuthService {
    Long register(UserRegisterRequest userRegisterDto);
    String login(UserLoginRequest userLoginDto);
}
