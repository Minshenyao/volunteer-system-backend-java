package edu.ahut.volunteersystembackend.service;

import edu.ahut.volunteersystembackend.dto.user.ChangePasswordRequest;
import edu.ahut.volunteersystembackend.dto.user.UserDto;
import edu.ahut.volunteersystembackend.dto.user.UserUpdateRequest;

import java.util.Map;

public interface UserService {
    UserDto getUserById(Long id);
    Map<String, Object> GetAllUserInfo();
    void ChangePassword(Long id, ChangePasswordRequest changePasswordRequest);
    void UpdateProfile(Long id, UserUpdateRequest userUpdateRequest);
    UserDto UploadAvatar(Long id, String avatarUrl);
}
