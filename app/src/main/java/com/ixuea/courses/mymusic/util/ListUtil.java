package com.ixuea.courses.mymusic.util;

import com.ixuea.courses.mymusic.listener.Consumer;

import java.util.List;

/**
 * 列表工具类
 */
public class ListUtil {
    /**
     * 遍历每一个接口
     */
    public static <T> void eachListener(List<T> datum, Consumer<T> action) {
        for (T listener : datum) {
            //将列表中每一个对象传递给action(将列表中对象传递给接口中的泛型方法中的参数)
            action.accept(listener);
        }
    }
}
