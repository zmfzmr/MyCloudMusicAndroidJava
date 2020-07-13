package com.ixuea.courses.mymusic.manager.impl;

import android.content.Context;

import com.ixuea.courses.mymusic.api.Api;
import com.ixuea.courses.mymusic.domain.User;
import com.ixuea.courses.mymusic.domain.response.DetailResponse;
import com.ixuea.courses.mymusic.listener.HttpObserver;
import com.ixuea.courses.mymusic.listener.UserListener;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户管理器
 */
public class UserManager {

    private static UserManager instance;//实例
    private final Context context;
    /**
     * 用户缓存Map
     */
    private Map<String, User> userCaches = new HashMap<>();

    public UserManager(Context context) {
        this.context = context;
    }

    /**
     * 获取用户管理器实例
     * synchronized: 也可以不用这个，因为android上也没有那么多的高并发
     *
     * @param context
     * @return
     */
    public static synchronized UserManager getInstance(Context context) {

        if (instance == null) {
            instance = new UserManager(context.getApplicationContext());
        }

        return instance;
    }

    /**
     * 获取用户(通过缓存或者从网络中 回调这个User对象到外面)
     *
     * @param userId
     * @param userListener
     */
    public void getUser(String userId, UserListener userListener) {
        //先从缓存中获取
        //这样的好处是：只要我们的应用没有关闭，避免重复去请求网路
        User data = userCaches.get(userId);
        if (data != null) {
            //说明有缓存，那么回调给子类那边
            userListener.onUser(data);
            return;
        }

        //请求网络获取(没有缓存那么就从网络获取)
        Api.getInstance()
                .userDetail(userId)
                .subscribe(new HttpObserver<DetailResponse<User>>() {
                    @Override
                    public void onSucceeded(DetailResponse<User> data) {
                        //回调请求成功(也就是回调User这个对象)
                        userListener.onUser(data.getData());

                        //添加到缓存 key: 用户id(String类型 只不过这里是参数传递进来的)
                        userCaches.put(userId, data.getData());
                    }
                });


    }
}
