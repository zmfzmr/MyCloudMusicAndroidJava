package com.ixuea.courses.mymusic.domain;

/**
 * 用户模型
 */
public class User extends BaseModel {
    private String nickname;//昵称
    private String avatar;//头像
    private String phone;//手机号
    private String email;//邮箱
    private String qq_id;//QQ第三方登录后Id
    private String weibo_id;//微博第三方登录后Id
    //因为字段只有一个code，所以写到User对象里面
    private String code;//验证码 只有找回密码的时候才会用到
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getQq_id() {
        return qq_id;
    }

    public void setQq_id(String qq_id) {
        this.qq_id = qq_id;
    }

    public String getWeibo_id() {
        return weibo_id;
    }

    public void setWeibo_id(String weibo_id) {
        this.weibo_id = weibo_id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
