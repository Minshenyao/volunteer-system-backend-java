package edu.ahut.volunteersystembackend.controller;

import edu.ahut.volunteersystembackend.dto.user.ChangePasswordRequest;
import edu.ahut.volunteersystembackend.dto.user.UserDto;
import edu.ahut.volunteersystembackend.dto.user.UserUpdateRequest;
import edu.ahut.volunteersystembackend.response.Response;
import edu.ahut.volunteersystembackend.service.UserService;
import edu.ahut.volunteersystembackend.utils.AliyunOssService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private AliyunOssService aliyunOssService;

    @GetMapping("/profile")
    public Response<UserDto> getUserProfile(HttpServletRequest request) {
        try{
            Long UserId = (Long) request.getAttribute("userId");
            UserDto userDto = userService.getUserById(UserId);
            return Response.success("查询成功", userDto);
        } catch (Exception e) {
            log.error("获取用户信息失败", e);
            return Response.error(500, "查询失败: " + e.getMessage());
        }
    }
    @GetMapping("/volunteer_count")
    public Response<Map<String, Object>> GetAllUserInfo(HttpServletRequest request) {
        try {
            Map<String, Object> volunteersData = userService.GetAllUserInfo();
            return Response.success("获取志愿者信息成功", volunteersData);
        } catch (Exception e) {
            return Response.error(500, "获取志愿者信息失败: " + e.getMessage());
        }
    }

    @PutMapping("/change_password")
    public Response<UserDto> ChangePassword(HttpServletRequest request, @RequestBody ChangePasswordRequest changePasswordRequest) {
        try {
            Long UserId = (Long) request.getAttribute("userId");
            userService.ChangePassword(UserId, changePasswordRequest);
            UserDto userDto = userService.getUserById(UserId);
            return Response.success("更改密码成功", userDto);
        } catch (Exception e) {
            return Response.error(500, "更改密码失败: " + e.getMessage());
        }
    }

    @PutMapping("/update_profile")
    public Response<UserDto> UpdateProfile(HttpServletRequest request, @RequestBody UserUpdateRequest userUpdateRequest) {
        if (userUpdateRequest.getNickname() == null || !userUpdateRequest.getGender().equals("男") && !userUpdateRequest.getGender().equals("女") || !userUpdateRequest.getPhone().matches("^1[3-9]\\d{9}$")) {
            return Response.error(400, "参数错误");
        }
        try {
            Long UserId = (Long) request.getAttribute("userId");
            userService.UpdateProfile(UserId, userUpdateRequest);
            UserDto userDto = userService.getUserById(UserId);
            return Response.success("用户信息更新成功", userDto);
        }catch (Exception e){
            return Response.error(500, "更新用户信息失败: " + e.getMessage());
        }
    }

    @PostMapping("/upload_avatar")
    public Response<UserDto> UploadAvatar(HttpServletRequest request, @RequestParam(value = "avatar", required = false) MultipartFile avatarFile) {
        if (avatarFile == null || avatarFile.isEmpty()) {
            return Response.error(400, "文件错误");
        }
        try {
            Long UserId = (Long) request.getAttribute("userId");
            String avatarUrl = aliyunOssService.uploadFile(avatarFile);
            userService.UploadAvatar(UserId, avatarUrl);
            UserDto userDto = userService.getUserById(UserId);
            return Response.success("上传头像成功", userDto);
        }catch (Exception e){
            return Response.error(500, "上传用户头像失败: " + e.getMessage());
        }
    }
}
