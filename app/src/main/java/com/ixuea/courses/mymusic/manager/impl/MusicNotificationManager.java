package com.ixuea.courses.mymusic.manager.impl;

import android.content.Context;

/**
 * 在第一版课程中，我们是将通知写到播放列表中的，虽然可以运行，但其他项目却不是很好复用，
 * 因为一个应用可能需要播放列表，但不一定需要音乐通知，现在将音乐通知写到单独的类中，
 * 好处是，将逻辑分散开了便于维护和重用；
 * <p>
 * 坏处是，会增加代码量；
 * 但我们这里还是放到单独的类中。
 */
public class MusicNotificationManager {

    private static MusicNotificationManager instance;//实例
    private final Context context;//上下文

    /**
     * 构造方法
     *
     * @param context Context
     */
    public MusicNotificationManager(Context context) {
        this.context = context.getApplicationContext();
    }

    /**
     * 获取通知管理器实例
     */
    public static MusicNotificationManager getInstance(Context context) {
        if (instance == null) {
            instance = new MusicNotificationManager(context);
        }
        return instance;
    }
}
