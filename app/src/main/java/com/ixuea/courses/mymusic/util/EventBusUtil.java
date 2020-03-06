package com.ixuea.courses.mymusic.util;

import com.ixuea.courses.mymusic.domain.event.PlayListChangeEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * 发布订阅框架工具类
 */
public class EventBusUtil {
    /**
     * 发布音乐列表改变通知
     */
    public static void postPlayListChangeEvent() {
        //发送列表改变了通知
        EventBus.getDefault().post(new PlayListChangeEvent());
    }
}
