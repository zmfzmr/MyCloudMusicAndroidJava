package com.ixuea.courses.mymusicold.domain;

/**
 * 用户模型
 */
public class User extends BaseModel {
    private String phone;//手机号
    private String email;//邮箱
    /**
     * 用户的密码,登录，注册向服务端传递
     */
    private String password;//密码

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
