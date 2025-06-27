package edu.ahut.volunteersystembackend.utils;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class UserStatusUtil {
    public static String getUserStatus(Long lastLoginTime) {
        if (lastLoginTime == null || lastLoginTime <= 0) {
            return "未知"; // 处理空值情况
        }

        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();
        // 将时间戳转换为 LocalDateTime
        LocalDateTime lastLogin = Instant.ofEpochMilli(lastLoginTime)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        // 计算时间差
        Duration diff = Duration.between(lastLogin, now);

        if (diff.toMinutes() <= 30) {
            return "活跃";
        } else if (diff.toMinutes() <= 60) {
            return "空闲";
        } else {
            return "下线";
        }
    }
}