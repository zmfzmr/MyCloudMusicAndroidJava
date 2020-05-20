package com.ixuea.courses.mymusic.domain;

import java.util.List;

/**
 * 动态模型
 */

/*
 {
            "id": 2,
            "content": "你好呀，我看见你发的动态了哟",
            "province": "浙江",
            "city": "杭州",
            "created_at": "2200-04-10T00:28:49.000Z",
            "user": {
                "id": 2,
                "nickname": "爱学啊edu",
                "avatar": "dda6b71dfc794970bd739008187fa1ca.jpg",
                "gender": 0
            },
            "images": [
                {
                    "uri": "assets/4.jpg"
                },
                {
                    "uri": "assets/5.jpg"
                },
                {
                    "uri": "assets/6.jpg"
                },
                {
                    "uri": "assets/7.jpg"
                },
                {
                    "uri": "assets/11.jpg"
                }
            ]
        }
 */
public class Feed extends BaseModel {
    private String content;//动态内容
    private String province;//省
    private String city;//市
    /**
     * 图片 集合
     */
    private List<Resource> images;

    /**
     * 创建用户
     */
    private User user;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<Resource> getImages() {
        return images;
    }

    public void setImages(List<Resource> images) {
        this.images = images;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
