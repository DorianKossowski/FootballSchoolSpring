package com.football_school_spring.models.dto;

public class EditPasswordDTO {
    private long id;
    private String oldPassword;
    private String newPassword;
    private String repNewPassword;

    public EditPasswordDTO() {
    }

    public EditPasswordDTO(long id) {
        this.id = id;
    }

    public EditPasswordDTO(long id, String oldPassword, String newPassword, String repNewPassword) {
        this.id = id;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.repNewPassword = repNewPassword;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public String getRepNewPassword() {
        return repNewPassword;
    }

    public void setRepNewPassword(String repNewPassword) {
        this.repNewPassword = repNewPassword;
    }
}