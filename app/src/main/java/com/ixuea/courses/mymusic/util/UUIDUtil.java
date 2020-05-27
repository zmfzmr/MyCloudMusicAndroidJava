package com.ixuea.courses.mymusic.util;

import java.util.UUID;

/**
 * uuid工具类(可以理解为这个工具生成的每一个都不可能重复)
 * <p>
 * UUID 是指Universally Unique Identifier，翻译为中文是通用唯一识别码，UUID 的目的是让分布式系统中的所有元素都能有唯一的识别信息。如此一来，每个人都可以创建不与其它人冲突的 UUID，就不需考虑数据库创建时的名称重复问题
 */
public class UUIDUtil {
    /**
     * 获取uuid (例如：123e4567-e89b-12d3-a456-426655440000)
     */
    public static String getUUID() {
        //UUID: java.util 包的类
        String uuid = UUID.randomUUID().toString();

        //去掉-
        return uuid.replace("-", "");
    }
}
