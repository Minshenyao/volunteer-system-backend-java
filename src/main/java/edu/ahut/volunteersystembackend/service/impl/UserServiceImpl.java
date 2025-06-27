package edu.ahut.volunteersystembackend.service.impl;

import edu.ahut.volunteersystembackend.dao.UserRepository;
import edu.ahut.volunteersystembackend.dto.user.*;
import edu.ahut.volunteersystembackend.model.User;
import edu.ahut.volunteersystembackend.service.UserService;
import edu.ahut.volunteersystembackend.utils.UserConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository repository) {
        this.userRepository = repository;
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("id:" + id + "不存在!")) ;
        return UserConverter.convertUserToUserDto(user);
    }

    @Override
    public Map<String, Object> GetAllUserInfo() {
        List<User> users = userRepository.findAll();

        List<UserCountDto> userDtoList = users.stream()
                .map(UserConverter::convertUserToUserCountDto)
                .toList();

        // 构造符合 JSON 结构的数据
        Map<String, Object> resultData = new HashMap<>();
        resultData.put("volunteers", userDtoList);

        return resultData;
    }

//    @Override
//    public void deleteUser(Long id) {
//        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("id:" + id + "不存在!"));
//        userRepository.delete(user);
//    }

    @Override
    public void ChangePassword(Long id, ChangePasswordRequest changePasswordRequest) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("id:" + id + "不存在！"));
        user.setPassword(changePasswordRequest.getNewPassword());
        userRepository.save(user);
    }

    @Override
    public void UpdateProfile(Long id, UserUpdateRequest userUpdateRequest) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("id:" + id + "不存在！"));
        if (!userUpdateRequest.getNickname().equals(user.getNickname())) {
            user.setNickname(userUpdateRequest.getNickname());
        }
        if (userUpdateRequest.getGender() != null) {
            if (userUpdateRequest.getGender().equals("男")){
                user.setGender(true);
            }
            if (userUpdateRequest.getGender().equals("女")){
                user.setGender(false);
            }
        }
        if (!userUpdateRequest.getPhone().equals(user.getPhone())) {
            user.setPhone(userUpdateRequest.getPhone());
        }
        userRepository.save(user);
    }

    @Override
    public UserDto UploadAvatar(Long id, String avatarUrl) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("id:" + id + "不存在！"));
        user.setAvatar(avatarUrl);
        userRepository.save(user);
        return null;
    }
}
