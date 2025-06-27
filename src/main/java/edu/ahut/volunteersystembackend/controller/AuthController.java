package edu.ahut.volunteersystembackend.controller;

import edu.ahut.volunteersystembackend.dto.user.UserDto;
import edu.ahut.volunteersystembackend.dto.user.UserLoginRequest;
import edu.ahut.volunteersystembackend.dto.user.UserRegisterRequest;
import edu.ahut.volunteersystembackend.response.Response;
import edu.ahut.volunteersystembackend.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;



@RestController
@RequestMapping("/api")
public class AuthController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public Response<UserDto> register(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (!userRegisterRequest.getEmail().matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.com$") || userRegisterRequest.getPassword() == null ||
                userRegisterRequest.getNickname() == null || !userRegisterRequest.getGender().equals("男") && !userRegisterRequest.getGender().equals("女") && !userRegisterRequest.getGender().equals("保密") || !userRegisterRequest.getPhone().matches("^1[3-9]\\d{9}$")) {
            return Response.error(400, "参数错误");
        }

        try {
            authService.register(userRegisterRequest);
            return Response.success("注册成功", null);
        } catch (Exception e) {
            log.error("用户注册失败", e);
            return Response.error(500, "注册失败: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public Response<Map<String, Object>> login(@RequestBody UserLoginRequest userLoginRequest) {
        if (userLoginRequest.getEmail() == null || userLoginRequest.getPassword() == null) {
            return Response.error(400, "参数错误");
        }
        try {
            String authorization = authService.login(userLoginRequest);
            Map<String, Object> resultData = new HashMap<>();
            resultData.put("token", authorization);
            return Response.success("登录成功", resultData);
        } catch (Exception e) {
            log.error("用户登录失败", e);
            return Response.error(500, "登录失败: " + e.getMessage());
        }
    }

//    @DeleteMapping("/users/{id}")
//    public Response<String> deleteUser(@PathVariable Long id, @RequestHeader("Authorization") String authorization) {
//        try {
//            userService.deleteUser(id);
//            return Response.success("删除成功", "用户ID:" + id + "已删除");
//        } catch (Exception e) {
//            log.error("删除用户失败", e);
//            return Response.error(500, "删除失败: " + e.getMessage());
//        }
//    }
//
//    @PutMapping("/users/{id}")
//    public Response<UserDto> updateUser(@PathVariable Long id, @RequestBody UserUpdateRequest userUpdateRequest) {
//        try {
//            userService.updateUser(id, userUpdateRequest);
//            UserDto userDto = userService.getUserById(id);
//            return Response.success("更新成功", userDto);
//        } catch (Exception e) {
//            log.error("更新用户信息失败", e);
//            return Response.error(500, "更新失败: " + e.getMessage());
//        }
//    }

}
