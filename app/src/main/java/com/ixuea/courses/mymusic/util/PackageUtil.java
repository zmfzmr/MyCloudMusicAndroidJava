package com.ixuea.courses.mymusic.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Package(应用)相关工具方法
 */
public class PackageUtil {
    /**
     * 版本名称
     * 一般是用来显示给人类阅读的
     *
     * @param context
     * @return 一般都是3位：例如：2.0.1
     * <p>
     * 思路：获取包管理器->获取包info->获取versionName
     */
    public static String getViersionName(Context context) {
        //错误写法，包管理器不是new的，是上下文获取的
//        PackageManager packageManager = new PackageManager(context);

        try {
            PackageInfo packageInfo = getPackageInfo(context, context.getPackageName());
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * 版本号
     *
     * @param context
     * @return 一般是整形，例如：100；使用int更方便判断大小
     * <p>
     * 思路： 获取包管理器->获取包info->获取versionCode(新方法: getLongVersionCode获取)
     */

//    @RequiresApi(api = Build.VERSION_CODES.P) 这个先不写，也是可以运行的
    public static long getVersionCode(Context context) {
        try {
            PackageInfo packageInfo = getPackageInfo(context, context.getPackageName());
            return packageInfo.getLongVersionCode();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * 获取应用信息
     *
     * @param context     Context
     * @param packageName 包名
     * @return
     * @throws PackageManager.NameNotFoundException
     */
    private static PackageInfo getPackageInfo(Context context, String packageName) throws PackageManager.NameNotFoundException {
        PackageManager packageManager = context.getPackageManager();
        //1： 包名  2：flag 暂时用不到，先写0
        return packageManager.getPackageInfo(packageName, 0);
    }
}
