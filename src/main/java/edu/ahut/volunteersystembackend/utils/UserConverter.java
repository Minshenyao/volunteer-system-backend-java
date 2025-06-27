package edu.ahut.volunteersystembackend.utils;

import edu.ahut.volunteersystembackend.dto.user.UserCountDto;
import edu.ahut.volunteersystembackend.dto.user.UserDto;
import edu.ahut.volunteersystembackend.dto.user.UserRegisterRequest;
import edu.ahut.volunteersystembackend.model.User;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class UserConverter {

    public static UserDto convertUserToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setVolunteerID(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setNickname(user.getNickname());
        if (user.getGender()) {
            userDto.setGender("男");
        } else {
            userDto.setGender("女");
        }
        userDto.setStatus(UserStatusUtil.getUserStatus(user.getLastLoginTime()));
        userDto.setAdmin(user.getAdmin());
        userDto.setPhone(user.getPhone());
        userDto.setAvatar(user.getAvatar());
        userDto.setDuration(user.getDuration());
        ZonedDateTime zonedDateTime = Instant.ofEpochMilli(user.getLastLoginTime())
                .atZone(ZoneId.of("Asia/Shanghai"));
        String formattedTime = zonedDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        userDto.setLastLoginTime(formattedTime);
        return userDto;
    }

    public static UserCountDto convertUserToUserCountDto(User user) {
        UserCountDto userCountDto = new UserCountDto();
        userCountDto.setId(user.getId());
        userCountDto.setEmail(user.getEmail());
        userCountDto.setNickname(user.getNickname());
        userCountDto.setStatus(UserStatusUtil.getUserStatus(user.getLastLoginTime()));
        userCountDto.setPhone(user.getPhone());
        userCountDto.setAvatar(user.getAvatar());
        userCountDto.setDuration(user.getDuration());
        if (user.getGender()) {
            userCountDto.setGender("男");
        } else {
            userCountDto.setGender("女");
        }
        ZonedDateTime zonedDateTime = Instant.ofEpochMilli(user.getLastLoginTime())
                .atZone(ZoneId.of("Asia/Shanghai"));
        String formattedTime = zonedDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        userCountDto.setLast_login_time(formattedTime);
        return userCountDto;
    }
    public static User convertUserRegisterReqistToUser(UserRegisterRequest userRegisterRequest) {
        User user = new User();
        user.setEmail(userRegisterRequest.getEmail());
        user.setPassword(userRegisterRequest.getPassword());
        user.setNickname(userRegisterRequest.getNickname());
        user.setAdmin(false);
        user.setGender(!userRegisterRequest.getGender().equals("女"));
        user.setPhone(userRegisterRequest.getPhone());
        user.setAvatar(null);
        user.setDuration(0);
        user.setStatus(1);
        user.setCreateAt(System.currentTimeMillis());
        user.setLastLoginTime(System.currentTimeMillis());
        return user;
    }
}
