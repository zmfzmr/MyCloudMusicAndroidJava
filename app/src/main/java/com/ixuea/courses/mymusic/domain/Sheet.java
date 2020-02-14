package com.ixuea.courses.mymusic.domain;

import java.util.List;

import static com.ixuea.courses.mymusic.util.Constant.TYPE_SHEET;

/**
 * 歌单对象
 */
//public class Sheet extends BaseModel {
public class Sheet extends BaseMultiItemEntity {
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

    /**
     * 描述
     */
    private String description;
    /**
     * 点击数(播放量)
     */
    private int clicks_count;
    /**
     * 收藏数
     */
    private int collections_count;
    /**
     * 评论数
     */
    private int comments_count;

    /**
     * 音乐数量
     * 这个字段可能用不上，先写上(这个json数据没有的，先写上)
     */
    private int songs_count;

    /**
     * 歌单创建者
     */
    private User user;

    /**
     * 歌曲
     */
    private List<Song> songs;
    /**
     * 是否收藏了
     * 有值就表示收藏了(收藏)
     * <p>
     * 这里用Integer
     * <p>
     * int 默认初始化为0；Integer默认初始化为null
     */
    private Integer collection_id;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getClicks_count() {
        return clicks_count;
    }

    public void setClicks_count(int clicks_count) {
        this.clicks_count = clicks_count;
    }

    public int getCollections_count() {
        return collections_count;
    }

    public void setCollections_count(int collections_count) {
        this.collections_count = collections_count;
    }

    public int getComments_count() {
        return comments_count;
    }

    public void setComments_count(int comments_count) {
        this.comments_count = comments_count;
    }

    public int getSongs_count() {
        return songs_count;
    }

    public void setSongs_count(int songs_count) {
        this.songs_count = songs_count;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    public Integer getCollection_id() {
        return collection_id;
    }

    public void setCollection_id(Integer collection_id) {
        this.collection_id = collection_id;
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
        return TYPE_SHEET;
    }

    /**
     * 占多少列
     */
    @Override
    public int getSpanSize() {
        //为啥是1，参考BaseMultiItemEntity中
        return 1;
    }
}
