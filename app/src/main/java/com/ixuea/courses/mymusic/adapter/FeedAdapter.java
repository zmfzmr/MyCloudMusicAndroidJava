package com.ixuea.courses.mymusic.adapter;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.domain.Feed;
import com.ixuea.courses.mymusic.domain.Resource;
import com.ixuea.courses.mymusic.listener.FeedListener;
import com.ixuea.courses.mymusic.util.ImageUtil;
import com.ixuea.courses.mymusic.util.LogUtil;
import com.ixuea.courses.mymusic.util.TimeUtil;

import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 动态适配器
 */
public class FeedAdapter extends BaseQuickAdapter<Feed, BaseViewHolder> {
    /**
     * 动态监听器
     */
    private final FeedListener feedListener;
    /**
     * 构造方法
     */
    public FeedAdapter(int layoutResId, FeedListener feedListener) {
        super(layoutResId);
        this.feedListener = feedListener;
    }

    /**
     * 显示数据
     */
    @Override
    protected void convert(@NonNull BaseViewHolder helper, Feed data) {
        //头像
        ImageView iv_avatar = helper.getView(R.id.iv_avatar);
        ImageUtil.showAvatar(mContext, iv_avatar, data.getUser().getAvatar());

        //昵称
        helper.setText(R.id.tv_nickname, data.getUser().getNickname());

        //时间
        helper.setText(R.id.tv_time, TimeUtil.commentFormat(data.getCreated_at()));

        StringBuilder sb = new StringBuilder();
        sb.append(data.getContent());
        if (StringUtils.isNotBlank(data.getProvince())) {
            //如果有省
            //就显示地理位置
            sb.append("\n来自：")
                    .append(data.getProvince())
                    .append(".")
                    .append(data.getCity());
        }

        //动态内容
//        helper.setText(R.id.tv_content,data.getContent());
        helper.setText(R.id.tv_content, sb.toString());

        //获取列表控件
        RecyclerView rv = helper.getView(R.id.rv);

        if (data.getImages() != null && data.getImages().size() > 0) {
            //有图片

            //显示图片列表控件
            rv.setVisibility(View.VISIBLE);

            //尺寸固定(当然这个也可以放到if外面)
            rv.setHasFixedSize(true);

            //禁用嵌套滚动（不希望内容多了后，里面的内容滚动(禁用内容滚动)）
            //注意：传入的是false
            rv.setNestedScrollingEnabled(false);
            //动态计算显示列数
            int spanCount = 1;

            if (data.getImages().size() > 4) {
                //大于4张图片

                //显示3列
                spanCount = 3;
            } else if (data.getImages().size() > 1) {
                //大于1张

                //显示2列
                spanCount = 2;
            }

            //设置布局管理器(注意：这里的上下文用的 是父类的mContext)
//            GridLayoutManager layoutManager = new GridLayoutManager(mContext, 3);
            //不管分几列，都会把item布局平分的
            GridLayoutManager layoutManager = new GridLayoutManager(mContext, spanCount);
            rv.setLayoutManager(layoutManager);

            //设置适配器
            ImageAdapter adapter = new ImageAdapter(R.layout.item_image);
            //设置item点击事件
            adapter.setOnItemClickListener(new OnItemClickListener() {
                /**
                 * 图片点击回调
                 */
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    LogUtil.d(TAG, "onItemClick: " + position);

                    //获取图片路径
                    //Java中有stream变换方法
                    //但是24及以上版本才能使用
                    //List<String> imageUris=data.getImages().stream().map

                    //使用Google的Guava变换方法
                    //(当然也可以使用RxJava来实现，我们使用的是Guava)
                    //其实这个方法相当于把data.getImages() 遍历把里面的每一个图片uri(字符串类型)，
                    // 添加到到这个集合List<String>里面
                    //注意：data.getImages()：里面单个对象是Resource(包裹这一个字符串Uri)
                    //这个Lists 是Guava 里面的package com.google.common.collect;

                    //参数1：需要的转换的集合
                    // 2.Function(1.转换的集合里面的 单个对象 2.把单个对象里面包裹的类型(String类型) 转换成String类型)
                    //原本Resource里面包裹了String类型，只是把外层Resource去掉了
                    List<String> imageUris = Lists.transform(data.getImages(), new Function<Resource, String>() {
                        /**
                         * 对每一个元素执行变换
                         * @param input
                         * @return
                         */
                        @NullableDecl
                        @Override
                        public String apply(@NullableDecl Resource input) {
                            //返回url
                            return input.getUri();
                        }
                    });
                    //回调监听器
                    feedListener.onImageClick(rv, imageUris, position);
                }
            });

            rv.setAdapter(adapter);
            //设置数据
            adapter.replaceData(data.getImages());
        } else {
            //没有图片

            //隐藏控件
            rv.setVisibility(View.GONE);

            //清除适配器(清除适配器或者清除数据都是一样的，最好清除适配器)
            rv.setAdapter(null);
        }

    }
}
