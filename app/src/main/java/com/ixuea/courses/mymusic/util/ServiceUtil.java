package com.ixuea.courses.mymusic.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

import java.util.List;

/**
 * 服务相关工具类
 */
public class ServiceUtil {
    /**
     * 启动Service
     * 参数2 泛型传入？ 这样可以启动任何的（比如启动任何的service）
     */
    public static void startService(Context context, Class<?> clazz) {
        if (!ServiceUtil.isServiceRunning(context, clazz)) {
            //创建Intent
            Intent intent = new Intent(context, clazz);

            //启动服务
            context.startService(intent);
        }
    }

    /**
     * service是否在运行
     *
     * @param context Context
     * @param clazz   Class<?>
     */
    private static boolean isServiceRunning(Context context, Class<?> clazz) {
        //获取活动管理器
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        //获取所有运行运行的服务
        List<ActivityManager.RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);

        //
        if (services == null || services.size() == 0) {
            //没有找到运行的服务
            return false;
        }

        //遍历所有服务
        //查看是否有我们需要的服务
        for (ActivityManager.RunningServiceInfo service : services) {
            if (service.service.getClassName().equals(clazz.getName())) {
                //找到了
                return true;
            }
        }

        return false;
    }
}
