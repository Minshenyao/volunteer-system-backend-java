package edu.ahut.volunteersystembackend.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserDto {
    private Long volunteerID;
    private String email;
    @JsonProperty("nickName")
    private String nickname;
    private String gender;
    private String phone;
    private String avatar;
    private Integer duration;
    @JsonProperty("lastActivity")
    private String lastLoginTime;
    @JsonProperty("isAdmin")
    private Boolean Admin;
    private String status;

    public Long getVolunteerID() {
        return volunteerID;
    }

    public void setVolunteerID(Long volunteerID) {
        this.volunteerID = volunteerID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    @JsonProperty("isAdmin")
    public Boolean getAdmin() {
        return Admin;
    }

    public void setAdmin(Boolean admin) {
        Admin = admin;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
