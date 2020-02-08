package com.ixuea.courses.mymusic.domain;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * 这个MultiItemEntity是添加BaseRecyclerViewAdapterHelper后才有的，点进去就知道了
 * <p>
 * 通用多类型模型
 * 定义抽象的，getItemType由子类实现
 * extends BaseModel(公共字段) implements MultiItemEntity（多类型就要实现这个接口）
 */
public abstract class BaseMultiItemEntity extends BaseModel implements MultiItemEntity {
    /**
     * 占用多少列
     * 意思说：前面我们设置了RecyclerView3列
     * 所以：我们这里默认return 3，就是一行显示一个（比如标题，歌曲）；
     * 如果return 1，就是一行显示3个（比如歌曲3列）
     *
     * <p>
     * 可以理解为：除整或者取余（3 / 3 = 1; 1 / 3  除不尽就取余3）
     * <p>
     * spansize：span：跨距 size：大小
     */
    public int getSpanSize() {
        return 3;
    }
}
