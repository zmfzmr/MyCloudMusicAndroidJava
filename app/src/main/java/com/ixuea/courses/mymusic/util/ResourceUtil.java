package com.ixuea.courses.mymusic.util;

/**
 * 资源工具类
 */
public class ResourceUtil {
    /**
     * 将相对资源路径转为绝对路径
     *
     * @param uri Uri
     * @return 绝对路径
     */
    public static String resourceUri(String uri) {
        return String.format(Constant.RESOURCE_ENDPOINT, uri);
    }
}
