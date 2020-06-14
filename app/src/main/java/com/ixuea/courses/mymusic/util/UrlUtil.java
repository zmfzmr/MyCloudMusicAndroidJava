package com.ixuea.courses.mymusic.util;

import java.util.HashMap;
import java.util.Map;

/**
 * url工具类
 */
public class UrlUtil {
    /**
     * 获取网址里面的查询参数
     * <p>
     * //http://dev-my-cloud-music-api-rails.ixuea.com/v1/monitors/version?u=6
     * <p>
     * //这里主要是获取后面的查询参数 key value 添加到Map返回
     */
    public static Map getUrlQuery(String url) {
        HashMap<String, Object> map = new HashMap<>();
        //把? 替换成 ;
        url = url.replace("?", ";");

        //不包含 ;  说明没有后面的u=6等查询参数
        if (!url.contains(";")) {
            return map;
        }


        //能走到这一步，说明后面都是有查询参数的
        if (url.split(";").length > 0) {
            //如果分割出来的长度大于0
            //比如： u=1&a=1
            String[] arr = url.split(";")[1].split("&");
            for (String s : arr) {
                //遍历分割出来的键值对  添加到Map集合
                String key = s.split("=")[0];
                String value = s.split("=")[1];
                map.put(key, value);
            }
            return map;

        } else {
            //分割出来的长度没有大于0，则说明没有后面的查询，直接返回map，
            // 其实这步可以不用写，因为前面的已经判断过了
            return map;
        }
    }
}
