package com.ixuea.courses.mymusic.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 视频模型
 */

/*

 {
            "id": 1,
            "title": "一晃就老了 秋裤大叔(赵小兵) 1",
            "uri": "assets/yi_huang_jiu_lao_le.mp4",
            "banner": "assets/s3.png",
            "duration": 36,
            "clicks_count": 621,
            "comments_count": 9,
            "created_at": "2019-05-01T05:35:47.000Z",
            "user": {
                "id": 1,
                "nickname": "爱学啊dev666",
                "avatar": "67133b479b364e8c9bfceb58015cd71f.jpg",
                "gender": 0
            }
        }
 */
public class Video extends BaseModel {
    /**
     * 标题
     */
    private String title;
    /**
     * 视频地址
     * 和图片图片地址一样
     * 都是相对地址
     */
    private String uri;
    /**
     * 封面地址
     */
    private String banner;
    /**
     * 视频时长
     * 单位：秒
     */
    private long duration;
    /**
     * 点击数
     */
    private long clicks_count;

    /**
     * 评论数
     */
    private long comments_count;
    /**
     * 谁发布了这个视频(目前客户端是不能发布这个视频，是后台发布的)
     */
    private User user;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getClicks_count() {
        return clicks_count;
    }

    public void setClicks_count(long clicks_count) {
        this.clicks_count = clicks_count;
    }

    public long getComments_count() {
        return comments_count;
    }

    public void setComments_count(long comments_count) {
        this.comments_count = comments_count;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("title", title)
                .append("uri", uri)
                .append("banner", banner)
                .append("duration", duration)
                .append("clicks_count", clicks_count)
                .append("comments_count", comments_count)
                .append("user", user)
                .toString();
    }
}
