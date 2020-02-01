package com.ixuea.courses.mymusic.util;

import android.app.Activity;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ixuea.courses.mymusic.R;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;

/**
 * 图片相关工具类
 */
public class ImageUtil {
    public static void showAvatar(Activity activity, ImageView view, String uri) {
        if (TextUtils.isEmpty(uri)) {
            //没有头像

            //显示默认头像
//            iv_avatar.setImageResource(R.drawable.placeholder);
            show(activity, view, R.drawable.placeholder);

        } else {
            //有头像 是否以http 开头
            if (uri.startsWith("http")) {
                showFull(activity, view, uri);
            } else {
                //相对路径
                show(activity, view, uri);

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
     * @param activity   Activity
     * @param view       ImageView
     * @param resourceId 图片资源id
     */
    public static void show(Activity activity, ImageView view, @RawRes @DrawableRes @Nullable int resourceId) {
        Glide.with(activity)
                .load(resourceId)
                .into(view);
    }

    /**
     * 绝对路径图片显示
     *
     * @param activity Activity
     * @param view     ImageView
     * @param uri      图片完整地址
     */
    public static void showFull(Activity activity, ImageView view, String uri) {
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
        Glide.with(activity)
                .load(uri)
                .apply(options)
                .into(view);
    }

    /**
     * 相对路径
     *
     * @param activity Activity
     * @param view     ImageView
     * @param uri      uri 这里是showAvatar中外界传入过来的data.getAvatar()
     */
    public static void show(Activity activity, ImageView view, String uri) {
        //将图片地址转为绝对路径
        uri = String.format(Constant.RESOURCE_ENDPOINT, uri);
        //地址已经拼接好，复用绝对路径的方法
        showFull(activity, view, uri);

    }

    /**
     * 获取公共配置
     */
    private static RequestOptions getCommonRequestOptions() {
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


}
