package edu.ahut.volunteersystembackend.dto.user;


//type ChangePasswordRequest struct {
//    OldPassword string `json:"oldPassword" binding:"required"`
//    NewPassword string `json:"newPassword" binding:"required"`
//}

public class ChangePasswordRequest {
    private String oldPassword;
    private String newPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
