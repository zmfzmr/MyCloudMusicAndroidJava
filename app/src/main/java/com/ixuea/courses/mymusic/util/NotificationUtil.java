package com.ixuea.courses.mymusic.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Build;

import com.ixuea.courses.mymusic.R;

import androidx.core.app.NotificationCompat;

/**
 * 通知相关工具类
 */
public class NotificationUtil {
    private static final String IMPORTANCE_HIGH_CHANNEL_ID = "IMPORTANCE_HIGH_CHANNEL_ID";
    private static NotificationManager notificationManager;//通知管理器实例

    /**
     * 获取一个设置service为前台的通知
     * 他只是一个测试通知
     * 无任何实际意义
     *
     * @return
     *
     * 这里只是获取Notification，需要在MusicPlayerService配合startForeGround(0,notification) ，
     * 设置称为前台通知，也就是设置成了前台Service（这样就提高了应用的保活，防止应用过一段被杀死）
     */
    public static Notification getServiceForeground(Context context) {
        //渠道是8.0中新增的
        //简单来说就是
        //为了给通知分组
        //例如：我们这个应用有聊天消息
        //还有其他广告推送消息
        //那么如果要把这个应用做好
        //这两种类型的通知就应该设置不同的渠道
        //一个是：聊天消息渠道；优先级为紧急
        //另一个是：推送广告通知；优先级为高
        //好处是用户可以屏蔽某部分通知
        //还能不错过重要的通知
        //当然这只是Google希望大家遵循这个规则
        //但往往实际情况下
        //也会把广告设置为紧急的
        //因为推送广告就是要让用户看到

        //获取通知管理器
        getNotificationManager(context);

        //因为这个API是8.0才有的
        //所以要这么判断版本
        //不然低版本会崩溃
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //创建渠道
            //可以多次创建
            //但Id一样只会创建一个
            //参数1：渠道id 2：渠道描述 3：优先级
            NotificationChannel channel = new NotificationChannel(IMPORTANCE_HIGH_CHANNEL_ID,
                    "重要通知", NotificationManager.IMPORTANCE_HIGH);
            //创建渠道
            //（虽然前面创建了NotificationChannel实例，但是并没有在系统中创建通知渠道，所以需要用通知管理器创建通知渠道）
            notificationManager.createNotificationChannel(channel);
        }

        //创建一个通知
        //内容随便写
        //通知的配置有很多
        //这里就不在讲解了
        //参数2：渠道id（前面的那个渠道id）
        //注意：这里用的NotificationCompat，兼容低版本
        Notification notification = new NotificationCompat.Builder(context, IMPORTANCE_HIGH_CHANNEL_ID)
                .setContentTitle("我们是爱学啊")//通知标题
                .setContentText("人生苦短，我们只做好课")//通知内容
                //通知小图标
                .setSmallIcon(R.mipmap.ic_launcher)
                //设置大图标BitmapFactory.decodeResource:把一个资源图片转换成Bitmap
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                //创建通知
                .build();

        return notification;
    }

    /**
     * 获取通知管理器
     */
    private static void getNotificationManager(Context context) {
        if (notificationManager == null) {
            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
    }

    /**
     * 显示通知
     */
    public static void showNotification(int id, Notification notification) {
        notificationManager.notify(id, notification);
    }
}
