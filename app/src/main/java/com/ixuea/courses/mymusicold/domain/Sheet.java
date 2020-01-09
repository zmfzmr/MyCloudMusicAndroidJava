package com.ixuea.courses.mymusicold.domain;

/**
 * 歌单对象
 */
public class Sheet extends BaseModel {
    //id可以删除了，因为已经定义到父类了
//    /**
//     * 歌单Id
//     */
//    private String id;//这个用字符串类型，防止以后id变为字符串了，不好搞
    /**
     * 歌单标题
     */
    private String title;
    /**
     * 歌单封面
     */
    private String banner;

//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }

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
}
