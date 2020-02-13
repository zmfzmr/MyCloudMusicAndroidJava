package com.ixuea.courses.mymusic.domain;

/**
 * 广告模型
 */
public class Ad extends BaseModel {
    private String title;//标题
    private String banner;//图片
    private String uri;//点击广告后跳转的地址

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
