package com.ixuea.courses.mymusic.util;

import android.content.Context;

import com.ixuea.courses.mymusic.domain.SearchHistory;
import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.assit.QueryBuilder;

import java.util.List;

/**
 * Lite 数据库工具类
 */
public class LiteORMUtil {
    private static LiteORMUtil instance;//数据库工具类实例
    private final Context context;//应用的上下文
    private final LiteOrm orm;//数据库实例

    private LiteORMUtil(Context context) {
        this.context = context;//获取偏好奢姿工具类
        PreferenceUtil sp = PreferenceUtil.getInstance(context);

        //创建数据库实例(区分多用户)
        String databaseName = String.format("%s.db", sp.getUserId());
        orm = LiteOrm.newSingleInstance(context, databaseName);

        //设置调试模式
        orm.setDebugged(LogUtil.isDebug);
    }

    public static LiteORMUtil getInstance(Context context) {
        if (instance == null) {
            instance = new LiteORMUtil(context.getApplicationContext());
        }
        return instance;
    }

    /**
     * 创建或更新搜索历史(没有就创建，有就更新)
     *
     * @param data
     */
    public void createOrUpdate(SearchHistory data) {
        orm.save(data);
    }

    /**
     * 查询所有搜索历史(这个数据 根据 created_at值来排序)
     */
    public List<SearchHistory> querySearchHistory() {
        //QueryBuilder: 泛型 可以理解为一个集合  传入的表类SearchHistory.class 是传入构造方法里面的
        //appendOrderDescBy： 倒序  根据 这个created_at值来倒序
        QueryBuilder<SearchHistory> queryBuilder = new QueryBuilder<>(SearchHistory.class)
                .appendOrderDescBy("created_at");
        return orm.query(queryBuilder);
    }

    /**
     * 删除搜索历史
     */
    public void deleteSearchHistory(SearchHistory data) {
        orm.delete(data);
    }
}
