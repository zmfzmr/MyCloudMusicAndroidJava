package com.ixuea.courses.mymusic.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.ixuea.courses.mymusic.R;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;

/**
 * 图片相关工具类
 */
public class ImageUtil {
    public static void showAvatar(Context context, ImageView view, String uri) {
        if (TextUtils.isEmpty(uri)) {
            //没有头像

            //显示默认头像
//            iv_avatar.setImageResource(R.drawable.placeholder);
//            show(context, view, R.drawable.placeholder);
            showCircle(context, view, R.drawable.placeholder);

        } else {
            //有头像 是否以http 开头
            if (uri.startsWith("http")) {
//                showFull(context, view, uri);
                showCircleFull(context, view, uri);//（绝对路径）显示圆形头像
            } else {
                //相对路径
                showCircle(context, view, uri);

                //将图片地址转为绝对地址
                //data.getAvatar():相对路径，需要前面的前缀拼接起来，
                // 比如http://dev-courses-misuc.ixuea.com/ 加上ata.getAvatar()
                //注意：这里用uri，统一资源标识符（而url是网址的意思）
//                String uri = String.format(Constant.RESOURCE_ENDPOINT, data.getAvatar());
//                Glide.with(getMainActivity())
//                        .load(uri)
//                        .into(iv_avatar);
            }
        }
    }

    /**
     * 显示资源目录图片
     * <p>
     * （在本类中只使用了占位图）
     *
     * @param context   Context
     * @param view       ImageView
     * @param resourceId 图片资源id
     *
     *                   注意：这里记得加上公共的配置
     */
//    public static void show(Activity activity, ImageView view, @RawRes @DrawableRes @Nullable int resourceId) {
    public static void show(Context context, ImageView view, @RawRes @DrawableRes @Nullable int resourceId) {
        //获取公共配置
        RequestOptions options = getCommonRequestOptions();
        //显示图片
        Glide.with(context)
                .load(resourceId)//这里传入的是资源id
                .apply(options)
                .into(view);
    }

    /**
     * 绝对路径图片显示
     *
     * @param context context
     * @param view     ImageView
     * @param uri      图片完整地址
     */
    public static void showFull(Context context, ImageView view, String uri) {
//        if (StringUtils.isBlank(uri)) {
//            show(activity,view,R.drawable.placeholder);
//        } else {
//            //获取功能配置
//            RequestOptions options = getCommonRequestOptions();
//            //显示图片
//            Glide.with(activity)
//                    .load(uri)
//                    .apply(options)
//                    .into(view);
//        }
        //上面的可以不用判断，因为如果出错，已经在RequestOptions对象中处理了

        //获取功能配置
        RequestOptions options = getCommonRequestOptions();
        //显示图片
        Glide.with(context)
                .load(uri)
                .apply(options)
                .into(view);
    }

    /**
     * 显示相对路径图片
     *
     * @param context Activity
     * @param view     ImageView
     * @param uri      uri 这里是showAvatar中外界传入过来的data.getAvatar()
     */
    public static void show(Context context, ImageView view, String uri) {
        //将图片地址转为绝对路径
        uri = ResourceUtil.resourceUri(uri);//这个转成绝对路径需要经常用，所以放到一个专门的类里面
        //地址已经拼接好，复用绝对路径的方法
        showFull(context, view, uri);

    }

    /**
     * 显示圆形相对路径图片
     *
     * @param context Activity
     * @param view     ImageView
     * @param uri      Uri 统一资源定位符
     */
    public static void showCircle(Context context, ImageView view, String uri) {
        //将相对资源路径转为绝对路径
        uri = ResourceUtil.resourceUri(uri);

        //显示图片
        showCircleFull(context, view, uri);
    }

    /**
     * 显示圆形 绝对路径图片
     *
     * @param context Activity
     * @param view     ImageView
     * @param uri      String
     */
    public static void showCircleFull(Context context, ImageView view, String uri) {
        //获取圆形通用的配置
        RequestOptions options = getCircleCommonRequestOptions();

        //显示图片
        Glide.with(context)
                .load(uri)
                .apply(options)
                .into(view);
    }

    /**
     * 显示圆形资源目录图片
     *
     * @param context   Activity
     * @param view       ImageView
     * @param resourceId 资源id
     */
    public static void showCircle(Context context, ImageView view, @RawRes @DrawableRes @Nullable int resourceId) {
        //获取圆形通用的配置
        RequestOptions options = getCommonRequestOptions();
        //显示图片
        Glide.with(context)
                .load(resourceId)
                .apply(options)
                .into(view);
    }

    /**
     * 获取圆形通用的配置
     *
     * @return RequestOptions
     */
    private static RequestOptions getCircleCommonRequestOptions() {
        //获取通用配置
        RequestOptions options = getCommonRequestOptions();

        //圆形裁剪
        options.circleCrop();//注意：这类是圆形circleCrop，而不是centerCrop

        return options;
    }

    /**
     * 获取公共配置
     */
    public static RequestOptions getCommonRequestOptions() {
        //创建配置选项
        RequestOptions options = new RequestOptions();
        //占位图
        options.placeholder(R.drawable.placeholder);
        //出错后显示的图片
        //包括：图片不存在等情况
        options.error(R.drawable.placeholder);
        //从中心裁剪
        options.centerCrop();
        return options;
    }

    /**
     * 显示小的（10dp）圆角相对路径图片
     *
     * @param activity Activity
     * @param view
     * @param name
     */
    public static void showSmallRadius(Activity activity, ImageView view, String name) {
        showFullSmallRadius(activity, view, ResourceUtil.resourceUri(name));
    }

    /**
     * 显示小的（10dp）圆角绝对路径图片
     *
     * @param activity
     * @param view
     * @param name     这个是url地址
     */
    public static void showFullSmallRadius(Activity activity, ImageView view, String name) {
        //获取通用的配置
        RequestOptions options = getCommonRequestOptions();

        //创建两个变换
        //一个是从中心裁剪
        //另一个是圆角
        //注意：泛型是：Bitmap  参数2：是一个RoundedCorners  Rounded ：圆心的  Corners： 角
        //               DensityUtil.dip2px(activity, 10): 10dp 转成px

        //Glide：裁剪后，图片就是裁剪后的样子； CircleImage依赖控件：控件是圆形的，但是图片不是裁剪后
        MultiTransformation<Bitmap> multiTransformation = new MultiTransformation<>(
                new CenterCrop(),
                new RoundedCorners(DensityUtil.dip2px(activity, 10))
        );

        Glide.with(activity)
                .load(name)
                //应用变换
                .apply(options.bitmapTransform(multiTransformation))
                .into(view);
    }

    /**
     * 显示本地图片(也没啥复杂的，只是把本地的路径加载到图片控件上而已)
     *
     * @param context
     * @param view
     * @param data  本地图片路径
     */
    public static void showLocalImage(Context context, ImageView view, String data) {
        //获取通用配置
        RequestOptions options = getCommonRequestOptions();

        //使用Glide显示图片
        Glide.with(context)
                .load(data)
                .apply(options)
                .into(view);

    }

    /**
     * 显示资源图片
     *
     * @param context
     * @param view
     * @param resourceId 图片资源id
     */
    public static void showLocalImage(Context context, ImageView view, int resourceId) {
        //获取通用配置
        RequestOptions options = getCommonRequestOptions();

        //显示图片
        Glide.with(context)
                .load(resourceId)
                .apply(options)
                .into(view);
    }
}
