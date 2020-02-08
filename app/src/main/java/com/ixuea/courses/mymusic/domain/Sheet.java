package com.ixuea.courses.mymusic.domain;

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
