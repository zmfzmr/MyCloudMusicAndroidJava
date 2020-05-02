package com.ixuea.courses.mymusic.domain;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * 保存歌曲信息
 * <p>
 * 为什么单独创建一个对象保存歌曲信息呢？
 * 第一个原因是：因为Realm框架
 * 从数据库中查询回来的对象
 * 如果更改字段的值
 * 他会自动同步到数据中
 * <p>
 * 真实项目中，最好还是使用第一种，
 * 因为使用第二种，中间多了一层转换
 * 理论上效率要低一点
 * 但用单独一个对象保存
 * 将逻辑分散到多个类
 * 降低了难度
 * <p>
 * 所以真实项目中还要做一个权衡
 */
public class SongLocal extends RealmObject {

    //注意：这2个常量没有set get方法
    /**
     * 其他来源音乐
     * 包括在线的，下载的
     */
    public static final int SOURCE_OTHER = 0;
    /**
     * 本地音乐
     */
    public static final int SOURCE_LOCAL = 1;
    /**
     * 音乐排序key
     * 音乐id，音乐名称，歌手名
     * <p>
     * 如果传入的索引为0，那就是id排序；传入1的话就是title排序，3的话就是歌手名排序（因为第三个是专辑，我们这里没有专辑，所以用歌手名）
     */
    public static final String[] SORT_KEYS = new String[]{"id", "title", "singer_nickname"};

    /**
     * 歌曲Id
     * 数据库主键
     */
    @PrimaryKey
    private String id;
    private String title;//歌曲标题
    private String banner;//歌曲封面
    private String uri;//歌曲路径

    /**
     * 歌手Id
     * <p>
     * 在sqlite，mysql这样的数据库中
     * 字段名建议用下划线
     * 而不是驼峰命名
     * <p>
     * 但Realm中可以使用驼峰命名
     * 但我们还是使用下划线命名
     */
    private String singer_id;
    /**
     * 歌手名称
     */
    private String singer_nickname;
    /**
     * 歌手头像
     * 可选值 默认都是可选的（数据库框架中，没有指定可选类型，默认都是可选的）
     */
    private String singer_avatar;

    /**
     * 是否在播放列表中
     * true：在
     */
    private boolean playList;

    /**
     * 音乐来源(有本地音乐和网络音乐)，要区分来源
     */
    private int source;

    //播放音乐后才有值
    /**
     * 总进度
     * 单位：毫秒
     */
    private long duration;

    /**
     * 音乐播放进度
     */
    private long progress;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getSinger_id() {
        return singer_id;
    }

    public void setSinger_id(String singer_id) {
        this.singer_id = singer_id;
    }

    public String getSinger_nickname() {
        return singer_nickname;
    }

    public void setSinger_nickname(String singer_nickname) {
        this.singer_nickname = singer_nickname;
    }

    public String getSinger_avatar() {
        return singer_avatar;
    }

    public void setSinger_avatar(String singer_avatar) {
        this.singer_avatar = singer_avatar;
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

    /**
     * 将SongLocal（本地）转换成Song（音乐）对象
     * <p>
     * 注意：歌手（Song对象里面有User对象，所以需要创建）
     *
     * @return
     */
    public Song toSong() {
        //创建对象
        Song song = new Song();
        //赋值
        song.setId(getId());
        song.setTitle(title);
        song.setBanner(banner);
        song.setUri(uri);

        //歌手（Song对象里面有User对象，所以需要创建）
        User singer = new User();
        singer.setId(singer_id);
        singer.setNickname(singer_nickname);
        singer.setAvatar(singer_avatar);
        song.setSinger(singer);//设置到Song对象上

        //播放列表标志(这个标志好像不用还原回来，可能用不到)
        //playList：Song对象是否在播放列表
        song.setPlayList(playList);

        //来源(还原回来，目前也用不到)
        song.setSource(source);

        //音乐时长
        song.setDuration(duration);

        //播放进度
        song.setProgress(progress);

        return song;//返回数据
    }
}
