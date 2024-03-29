package com.ixuea.courses.mymusic.domain;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 用户模型
 */
public class User extends BaseModel {
    /**
     * 性别这里服务器返回的是这个int字段(10(男) 20(保密) 0(保密))，需要配合下面的getGenderFormat方法返回男或者女
     * 当然这2个也可以放到常量类中
     */
    public static final int UNKNOWN = 0;//
    public static final int MALE = 10;
    public static final int FEMALE = 20;
    private String nickname;//昵称
    private String avatar;//头像 (其实这里存储的是头像的相对路径（正常注册的）和绝对路径（QQ登录的，有个完整的地址）)
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

    /**
     * 描述
     */
    private String description;

    private String area;//区
    /**
     * 区编码
     * <p>
     * SerializedName是GSON框架的功能
     * 所以如果使用其他JSON框架可能不支持
     * 更多的功能这里就不讲解了
     * <p>
     * 作用是指定序列化和反序列化时字段
     * 也就说说在JSON中该字段为area_code
     * 当然也可以不使用这个功能
     * 字段就定义为area_code
     * 只是在Java中推荐使用驼峰命名法
     * <p>
     * 其实就是服务器返回的json中是area_code
     * Gson框架通过area_code这个字段找到这个值，并赋值给areaCode
     */
    @SerializedName("area_code")
    private String areaCode;//区代码

    private String province;//省
    @SerializedName("province_code")
    private String provinceCode;//省编码

    private String city;//市
    @SerializedName("city_code")
    private String cityCode;//市编码

    /**
     * 我的关注的人 (好友)
     */
    private long followings_count;

    /**
     * 关注我的人 (粉丝)
     */
    @SerializedName("followers_count")
    private long followersCount;

    /**
     * 是否关注
     * 1:关注
     * 在用户详情才会返回
     * <p>
     * 注意：这个是Integer类型
     */
    private Integer following;

    /**
     * 性别
     * 0：保密，10：男，20：女
     * 可以定义为枚举
     * 但枚举性能没有int高
     * 但int没有一些编译验证
     * Android中有替代方式
     * 这里用不到就不讲解了
     */
    private int gender;

    /**
     * 生日
     * 格式为：yyyy-MM-dd
     */
    private String birthday;
    /**
     * 推送注册后的Id
     * 设备唯一
     * 作用是后台用来判断是否推送退出事件
     * 当然也可以通过其他方式实现
     * <p>
     * 思路： 后台服务器获取这个Push id 会和原来的的去匹配，
     * 如果一样，那么这个用户在同一个设备上登录，这个时候不应该推送 比如： 推送json字段
     * 如果不一样，那么就给原来的设备推送一个退出
     */
    private String push;

    //本地过滤字段
    /**
     * 拼音 比如：aixuea
     */
    private String pinyin;

    /**
     * 拼音首字母 比如：axa
     */
    private String pinyinFirst;

    /**
     * 拼音首字母的首字母 比如：首字母axa中 a
     */
    private String first;
    //end 本地过滤字段

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getPinyinFirst() {
        return pinyinFirst;
    }

    public void setPinyinFirst(String pinyinFirst) {
        this.pinyinFirst = pinyinFirst;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public long getFollowings_count() {
        return followings_count;
    }

    public void setFollowings_count(long followings_count) {
        this.followings_count = followings_count;
    }

    public long getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(long followersCount) {
        this.followersCount = followersCount;
    }

    public Integer getFollowing() {
        return following;
    }

    public void setFollowing(Integer following) {
        this.following = following;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPush() {
        return push;
    }

    public void setPush(String push) {
        this.push = push;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("nickname", nickname)
                .append("avatar", avatar)
                .append("phone", phone)
                .append("email", email)
                .append("qq_id", qq_id)
                .append("weibo_id", weibo_id)
                .append("code", code)
                .append("password", password)
                .append("description", description)
                .append("area", area)
                .append("areaCode", areaCode)
                .append("province", province)
                .append("provinceCode", provinceCode)
                .append("city", city)
                .append("cityCode", cityCode)
                .append("followings_count", followings_count)
                .append("followersCount", followersCount)
                .append("following", following)
                .append("gender", gender)
                .append("birthday", birthday)
                .append("pinyin", pinyin)
                .append("pinyinFirst", pinyinFirst)
                .append("first", first)
                .toString();
    }

    //辅助方法

    /**
     * 格式化的性别(其实就是根据User对象里面的gender值，返回"男" "女" "保密")
     */
    public String getGenderFormat() {
        switch (gender) {
            case 10:
                return "男";
            case 20:
                return "女";
            default:
                //0
                return "保密";
        }
    }

    /**
     * 格式化的描述
     * <p>
     * 这样写的好处是，有可能首页显示（也就是抽屉布局首页），也可能点击进入也可能用到
     * 所以直接在返回data数据里面的User对象里面定义，方便复用（不然到时候需要在各个节目判断，麻烦）
     *
     * @return 描述
     */
    public String getDescriptionFormat() {
        if (TextUtils.isEmpty(description)) {
            return "这个人很懒，没有填写个人介绍!";
        }
        return description;
    }

    /**
     * 是否关注了
     * <p>
     * following: 这个值是跟服务端协商好的(关注后，服务端会返回一个自动"following": 1)
     */
    public boolean isFollowing() {
        return following != null;
    }

    //end辅助方法

}
