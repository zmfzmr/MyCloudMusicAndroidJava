package com.ixuea.courses.mymusic.util;

import com.google.gson.GsonBuilder;

/**
 * json 工具类
 */
public class JsonUtil {
    /**
     * 将Json转为对象
     *
     * @param data
     * @param clazz
     * @param <T>   注意这里：返回是T，那么就 还是写这个<T> 规定写法
     * @return
     */
    public static <T> T formJson(String data, Class<T> clazz) {
        //通过gson转换
        return new GsonBuilder()
                .create()
                .fromJson(data, clazz);

    }
}
