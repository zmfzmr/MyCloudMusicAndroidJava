package com.ixuea.courses.mymusic.manager.impl;

import android.app.Activity;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * 界面管理器(系统也自带有个ActivityManager，但是我们自定义一个)
 */
public class ActivityManager {


    private static ActivityManager instance;//实例

    /**
     * 我们不希望activity引用重复，所以定义Set集合(这里用子类HashSet)
     * <p>
     * 已经显示的界面
     * 装在列表里面的目的是
     * 当退出后， 要关闭所有界面
     * 因为我们这个应用是只有登录了,才能按到主界面
     * 之所以使用HashSet 是因为他不会保存重复元素
     */
    private static Set<Activity> activities = new HashSet<>();

    /**
     * 单例模式
     * <p>
     * 这里并没有服务端的高并发，所以简单实现下
     * 这哥单例模式并不是一个标准的单例模式，面试的时候不能这样写
     *
     * @return
     */
    public static ActivityManager getInstance() {
        if (instance == null) {
            instance = new ActivityManager();
        }
        return instance;
    }

    /**
     * 添加界面
     *
     * @param activity
     */
    public void add(Activity activity) {
        activities.add(activity);
    }

    /**
     * 移除界面
     *
     * @param activity
     */
    public void remove(Activity activity) {
        activities.remove(activity);
    }

    /**
     * 关闭所有界面
     */
    public void clear() {
        //因为这里在遍历中，同时调动finish，同时调用activity中的onDestroy方法(
        // 对应ActivityLifeCycle回调中的onActivityDestroyed--> 本类remove方法 )
        //也就是说：遍历的同时，同时调用remove方法移除引用，可能导致并发修改异常
        //我们这里删除最好使用迭代器
//        for (Activity oldActivity : activities) {
//            oldActivity.finish();
//        }

        //使用迭代器遍历
        //否则可能会发生并发修改异常
        //因为在onActivityDestroyed方法中
        //从列表移除界面
        Iterator<Activity> iterator = activities.iterator();
        while (iterator.hasNext()) {
            iterator.next().finish();
        }

    }
}
