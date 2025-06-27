package edu.ahut.volunteersystembackend.dto.user;


//// UpdateUserInfoRequest 用户信息更新请求
//type UpdateUserInfoRequest struct {
//    Nickname string `json:"nickname"`
//    Gender   string `json:"gender"`
//    Phone    string `json:"phone"`
//}
public class UpdateUserInfoRequest {
    private String nickname;
    private String gender;
    private String phone;

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
}
