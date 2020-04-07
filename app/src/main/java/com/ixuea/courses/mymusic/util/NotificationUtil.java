package com.ixuea.courses.mymusic.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.ixuea.courses.mymusic.MainActivity;
import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.domain.Song;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import static com.ixuea.courses.mymusic.util.Constant.NOTIFICATION_UNLOCK_LYRIC_ID;

/**
 * 通知相关工具类
 */
public class NotificationUtil {
    private static final String IMPORTANCE_LOW_CHANNEL_ID = "IMPORTANCE_LOW_CHANNEL_ID";
    private static final String TAG = "NotificationUtil";
    private static NotificationManager notificationManager;//通知管理器实例

    /**
     * 获取一个设置service为前台的通知
     * 他只是一个测试通知
     * 无任何实际意义
     *
     * @return 这里只是获取Notification，需要在MusicPlayerService配合startForeGround(0,notification) ，
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

        //创建通知渠道
        createNotificationChannel();

        //创建一个通知
        //内容随便写
        //通知的配置有很多
        //这里就不在讲解了
        //参数2：渠道id（前面的那个渠道id）
        //注意：这里用的NotificationCompat，兼容低版本
        Notification notification = new NotificationCompat.Builder(context, IMPORTANCE_LOW_CHANNEL_ID)
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
     * 创建通知渠道
     */
    private static void createNotificationChannel() {
        //因为这个API是8.0才有的
        //所以要这么判断版本
        //不然低版本会崩溃
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //创建渠道
            //可以多次创建
            //但Id一样只会创建一个
            //参数1：渠道id 2：渠道描述 3：优先级
            NotificationChannel channel = new NotificationChannel(IMPORTANCE_LOW_CHANNEL_ID,
                    "重要通知", NotificationManager.IMPORTANCE_HIGH);
            //创建渠道
            //（虽然前面创建了NotificationChannel实例，但是并没有在系统中创建通知渠道，所以需要用通知管理器创建通知渠道）
            notificationManager.createNotificationChannel(channel);
        }
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

    /**
     * 显示音乐通知（3个参数的重载方法）+ 1个参数isShowGlobalLyric：是否显示桌面歌词（控件）
     */
    public static void showMusicNotification(Context context, Song data, boolean isPlaying, boolean isShowGlobalLyric) {
        LogUtil.d(TAG, "showMusicNotification:" + data.getTitle() + "," + isPlaying);


        //先加载图片
        //因为我们的图片是在线的
        //而显示通知时没法直接显示网络图片
        //所以需要我们先把图片下载下来

        //创建请求选项

        RequestOptions options = ImageUtil.getCommonRequestOptions();

        //下载图片(使用Glide加载网络图片)
        //注意： .asBitmap()和.apply(options)记得要设置
        //into（这里是CustomTarget<Bitmap>）
        Glide.with(context)
                .asBitmap()
                .load(ResourceUtil.resourceUri(data.getBanner()))
                .apply(options)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap banner, @Nullable Transition<? super Bitmap> transition) {
                        //图片下载完成
                        showMusicNotification(context, data, isPlaying, banner, isShowGlobalLyric);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    /**
     * 显示音乐通知(4个参数的重载方法)
     * 等3个参数重载方法，加载完成图片后，才显示这个通知
     */
    public static void showMusicNotification(Context context, Song data, boolean isPlaying, Bitmap banner, boolean isShowGlobalLyric) {
        //创建通知渠道
        createNotificationChannel();

        //创建RemoteView
        //显示自定义通知固定写法

        //RemoteViews:远程的view，也就是说，这个通知栏，不属于应用里面，属于远程的view，所以用RemoteViews
        RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.notification_music_play);

        //显示数据 isPlaying:表示是否播放 banner:Bitmap对象
        setData(data, contentView, isPlaying, banner, isShowGlobalLyric);

        //设置通知点击事件
        setClick(context, contentView);

        //创建大通知
        RemoteViews contentBigView = new RemoteViews(context.getPackageName(), R.layout.notification_music_play_large);

        //显示数据 这个是contentBigView
        setData(data, contentBigView, isPlaying, banner, isShowGlobalLyric);

        //设置通知点击事件（大通知）
        setClick(context, contentBigView);

        //Activity中需要的PendingIntent，配合NotificationCompat.Builder中.setContentIntent(pendingIntent)使用
        //设置通知点击后启动的界面
        Intent intent = new Intent(context, MainActivity.class);
        //传递一个动作
        intent.setAction(Constant.ACTION_MUSIC_PLAY_CLICK);
        //在Activity以外启动界面
        //都要写这个标识
        //具体的还比较复杂
        //基础课程中讲解
        //这里学会这样用就行了
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //创建通知点击的PendingIntent
        PendingIntent contentPendingIntent = PendingIntent.getActivity(context
                , Constant.ACTION_MUSIC_PLAY_CLICK.hashCode()
                , intent
                , PendingIntent.FLAG_UPDATE_CURRENT);

        //收藏和下一曲 是大通知里面有的（小通知没有）

        //点赞（收藏）
        PendingIntent likePendingIntent = PendingIntent.getBroadcast(context
                , Constant.ACTION_LIKE.hashCode()
                , new Intent(Constant.ACTION_LIKE)
                , PendingIntent.FLAG_UPDATE_CURRENT);

        contentBigView.setOnClickPendingIntent(R.id.iv_like, likePendingIntent);

        //上一首
        PendingIntent previousPendingIntent = PendingIntent.getBroadcast(context
                , Constant.ACTION_PREVIOUS.hashCode()
                , new Intent(Constant.ACTION_PREVIOUS)
                , PendingIntent.FLAG_UPDATE_CURRENT);

        contentBigView.setOnClickPendingIntent(R.id.iv_previous, previousPendingIntent);

        //end 收藏和下一曲 是大通知里面有的（小通知没有）

        //创建NotificationCompat.Builder
        //这是构建者设计模式
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, IMPORTANCE_LOW_CHANNEL_ID)
                //点击后不消失
                .setAutoCancel(false)
                .setSmallIcon(R.mipmap.ic_launcher)
                //设置样式
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                //自定义view内容
                .setCustomContentView(contentView)
                //设置大内容view
                .setCustomBigContentView(contentBigView)
                //点击后执行的动作
                .setContentIntent(contentPendingIntent);

        //显示通知
        //id一样，会替换通知；id不一样，会显示新通知；这里用的是常量（也就是以后id用的都是这个）
        NotificationUtil.notify(context, Constant.NOTIFICATION_MUSIC_ID, builder.build());
    }

    private static void setClick(Context context, RemoteViews contentView) {
        //播放按钮点击事件
        //PendingIntent
        //可以理解为某个时间
        //就会触发的事件

        //FLAG_UPDATE_CURRENT
        //会替换到原来的广播（更新当前）
        //更深入的原理在详解课程中设置

        //2：标识请求码的
        //hashCode():字符串的hashCode方法，转换成int值（同一个字符串，int值是一样；不同字符串，int值不一样）
        //3:new Intent(Constant.ACTION_PLAY):通过intent 发送这个广播，动作是Constant.ACTION_PLAY;
        //接收广播的时候注册广播监听到这个ACTION动作
        PendingIntent playPendingIntent = PendingIntent.getBroadcast(context
                , Constant.ACTION_PLAY.hashCode()
                , new Intent(Constant.ACTION_PLAY)
                , PendingIntent.FLAG_UPDATE_CURRENT);

        //设置到播放按钮
        contentView.setOnClickPendingIntent(R.id.iv_play, playPendingIntent);

        //下一曲点击事件
        PendingIntent nextPendingIntent = PendingIntent.getBroadcast(context
                , Constant.ACTION_NEXT.hashCode()
                , new Intent(Constant.ACTION_NEXT)
                , PendingIntent.FLAG_UPDATE_CURRENT);

        contentView.setOnClickPendingIntent(R.id.iv_next, nextPendingIntent);

        //歌词点击事件
        PendingIntent lyricPendingIntent = PendingIntent.getBroadcast(context
                , Constant.ACTION_LYRIC.hashCode()
                , new Intent(Constant.ACTION_LYRIC)
                , PendingIntent.FLAG_UPDATE_CURRENT);

        contentView.setOnClickPendingIntent(R.id.iv_lyric, lyricPendingIntent);
    }

    /**
     * 大小通知设置数据
     * <p>
     * banner:Bitmap对象
     */
    private static void setData(Song data, RemoteViews contentView, boolean isPlaying, Bitmap banner, boolean isShowGlobalLyric) {
        //封面
        contentView.setImageViewBitmap(R.id.iv_banner, banner);

        //标题
        contentView.setTextViewText(R.id.tv_title, data.getTitle());

        //信息
        //由于服务端没有实现专辑
        //所以就显示测试信息
        contentView.setTextViewText(R.id.tv_info, String.format("%s - 这是专辑1", data.getSinger().getNickname()));

        //显示播放按钮
        int playButtonResourceId = isPlaying ? R.drawable.ic_music_notification_pause
                : R.drawable.ic_music_notification_play;

        contentView.setImageViewResource(R.id.iv_play, playButtonResourceId);

        //歌词按钮图标
        int lyricButtonResourceId = isShowGlobalLyric
                ? R.drawable.ic_music_notification_lyric_selected
                : R.drawable.ic_music_notification_lyric;

        //设置歌词按钮图标
        contentView.setImageViewResource(R.id.iv_lyric, lyricButtonResourceId);
    }

    /**
     * 显示通知
     */
    public static void notify(Context context, int id, Notification notification) {
        //获取通知栏管理器
        getNotificationManager(context);

        //显示通知(显示通知 需要用到通知管理器来时通知显示 这个通知管理器可能为null，所以前面先获取下)
        notificationManager.notify(id, notification);

    }

    /**
     * 显示解锁全局歌词通知
     */
    public static void showUnLockGlobalLyricNotification(Context context) {
        //创建渠道通知
        getNotificationManager(context);

        //创建通知渠道
        createNotificationChannel();

        //点击事件
        //参数2 发送广播的请求码

        // //FLAG_UPDATE_CURRENT
        //会替换到原来的广播（更新当前）
        PendingIntent contentPendingIntent = PendingIntent.getBroadcast(context,
                Constant.ACTION_UNLOCK_LYRIC.hashCode(),
                new Intent(Constant.ACTION_UNLOCK_LYRIC),
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, IMPORTANCE_LOW_CHANNEL_ID)
                //设置logo（小图标）
                .setSmallIcon(R.mipmap.ic_launcher)
                //设置标题
                .setContentTitle(context.getResources().getString(R.string.lock_lyric_title))
                //设置标题
                .setContentText(context.getResources().getString(R.string.lock_lyric_content))
                //设置点击后执行额意图
                .setContentIntent(contentPendingIntent);

        //显示通知
        notify(context, NOTIFICATION_UNLOCK_LYRIC_ID, builder.build());

    }

    /**
     * 解锁后清除通知
     */
    public static void clearUnlockGlobalLyricNotification(Context context) {
        //创建渠道通知
        getNotificationManager(context);

        //清除通知
        notificationManager.cancel(NOTIFICATION_UNLOCK_LYRIC_ID);
    }
}
