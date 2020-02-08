package com.ixuea.courses.mymusic.domain;

import static com.ixuea.courses.mymusic.util.Constant.TYPE_SONG;

/**
 * 歌曲
 */
public class Song extends BaseMultiItemEntity {

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
