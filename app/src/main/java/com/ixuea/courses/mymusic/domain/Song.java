package com.ixuea.courses.mymusic.domain;

import static com.ixuea.courses.mymusic.util.Constant.TYPE_SONG;

/**
 * 歌曲
 */
public class Song extends BaseMultiItemEntity {

    private String title;//标题
    private String banner;//封面
    private String uri;//音乐地址
    private int clicks_count;//点击数
    private int comments_count;//评论数
    private Integer style;//歌曲类型 (可以yogaInteger，因为这个可能没有值)
    private String lyric;//歌曲内容
    /**
     * 创建改音乐的人
     */
    private User user;

    /**
     * 歌手
     * <p>
     * 注意：这个歌手也是属于User这个类型的
     */
    private User singer;

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

    public int getClicks_count() {
        return clicks_count;
    }

    public void setClicks_count(int clicks_count) {
        this.clicks_count = clicks_count;
    }

    public int getComments_count() {
        return comments_count;
    }

    public void setComments_count(int comments_count) {
        this.comments_count = comments_count;
    }

    public Integer getStyle() {
        return style;
    }

    public void setStyle(Integer style) {
        this.style = style;
    }

    public String getLyric() {
        return lyric;
    }

    public void setLyric(String lyric) {
        this.lyric = lyric;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getSinger() {
        return singer;
    }

    public void setSinger(User singer) {
        this.singer = singer;
    }

    /**
     * 使用了BaseRecyclerViewAdapterHelper框架
     * 实现多类型列表
     * 需要实现该接口返回类型
     *
     * @return Item类型
     */
    @Override
    public int getItemType() {
        return TYPE_SONG;
    }
    //这里默认返回3，调用父类的getSpanSize方法
}
