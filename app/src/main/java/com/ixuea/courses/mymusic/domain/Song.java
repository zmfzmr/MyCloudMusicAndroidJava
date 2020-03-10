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

    //播放后才有值
    /**
     * 总进度
     * 单位：毫秒
     * 这个字段是后面添加的（不是json数据里面有的）
     */
    private long duration;
    /**
     * 播放进度
     */
    private long progress;

    //end 播放后才有值

    /**
     * 是否在播放列表
     * true
     */
    private boolean playList;

    /**
     * 音乐来源
     */
    private int source;




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

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getProgress() {
        return progress;
    }

    public void setProgress(long progress) {
        this.progress = progress;
    }

    public boolean isPlayList() {
        return playList;
    }

    public void setPlayList(boolean playList) {
        this.playList = playList;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
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

    /**
     * 将Song转为SongLocal对象
     *
     * @return
     */
    public SongLocal toSongLocal() {
        //创建对象
        SongLocal songLocal = new SongLocal();

        //设置
        songLocal.setId(getId());
        songLocal.setTitle(title);
        songLocal.setBanner(banner);
        songLocal.setUri(uri);

        //歌手相关的信息
        songLocal.setSinger_id(getSinger().getId());
        songLocal.setSinger_nickname(getSinger().getNickname());
        songLocal.setSinger_avatar(getSinger().getAvatar());
        //是否在播放列表
        songLocal.setPlayList(playList);

        //来源(这个也可以不保存，因为如果是本地的音乐，可以直接转成SongLocal)
        //但是这里还是保存下，防止从数据库里面恢复出来，还是有的（这个source还是有的）
        songLocal.setSource(source);

        //音乐时长
        songLocal.setDuration(duration);

        //播放进度
        songLocal.setProgress(progress);

        return songLocal;//返回对象
    }
}
