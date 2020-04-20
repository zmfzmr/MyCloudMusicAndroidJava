package com.ixuea.courses.mymusic.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 评论模型
 */
public class Comment extends BaseModel {
    private String content;//评论内容
    private long likes_count;//点赞数
    private String created_at;//日期时间（包含秒 毫秒）
    private User user;//评论发布人
    private Sheet sheet;//评论关联的歌单
    /**
     * 是否点赞
     * 优质表示点赞
     * null表示没有点赞
     */
    private String like_id;
    /**
     * 被恢复的评论（这个Comment就是本类）
     */
    private Comment parent;
    /**
     * 被回复评论id
     * 只有发布评论时候才可能有值
     */
    private String parent_id;
    /**
     * 歌单id
     * 只有发布评论时候才可能有值
     */
    private String sheet_id;


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getLikes_count() {
        return likes_count;
    }

    public void setLikes_count(long likes_count) {
        this.likes_count = likes_count;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Sheet getSheet() {
        return sheet;
    }

    public void setSheet(Sheet sheet) {
        this.sheet = sheet;
    }

    public String getLike_id() {
        return like_id;
    }

    public void setLike_id(String like_id) {
        this.like_id = like_id;
    }

    public Comment getParent() {
        return parent;
    }

    public void setParent(Comment parent) {
        this.parent = parent;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getSheet_id() {
        return sheet_id;
    }

    public void setSheet_id(String sheet_id) {
        this.sheet_id = sheet_id;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("content", content)
                .append("likes_count", likes_count)
                .append("user", user)
                .append("sheet", sheet)
                .append("like_id", like_id)
                .append("parent", parent)
                .append("parent_id", parent_id)
                .append("sheet_id", sheet_id)
                .toString();
    }

    //下面的equals和 hashCode暂时还没有用到
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Comment comment = (Comment) o;

        if (likes_count != comment.likes_count) return false;
        if (content != null ? !content.equals(comment.content) : comment.content != null)
            return false;
        if (user != null ? !user.equals(comment.user) : comment.user != null) return false;
        if (sheet != null ? !sheet.equals(comment.sheet) : comment.sheet != null) return false;
        if (like_id != null ? !like_id.equals(comment.like_id) : comment.like_id != null)
            return false;
        if (parent != null ? !parent.equals(comment.parent) : comment.parent != null) return false;
        if (parent_id != null ? !parent_id.equals(comment.parent_id) : comment.parent_id != null)
            return false;
        return sheet_id != null ? sheet_id.equals(comment.sheet_id) : comment.sheet_id == null;
    }

    @Override
    public int hashCode() {
        int result = content != null ? content.hashCode() : 0;
        result = 31 * result + (int) (likes_count ^ (likes_count >>> 32));
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (sheet != null ? sheet.hashCode() : 0);
        result = 31 * result + (like_id != null ? like_id.hashCode() : 0);
        result = 31 * result + (parent != null ? parent.hashCode() : 0);
        result = 31 * result + (parent_id != null ? parent_id.hashCode() : 0);
        result = 31 * result + (sheet_id != null ? sheet_id.hashCode() : 0);
        return result;
    }
}
