package com.ixuea.courses.mymusic.util;

import com.google.common.collect.Ordering;
import com.ixuea.courses.mymusic.domain.Song;
import com.ixuea.courses.mymusic.domain.User;
import com.ixuea.courses.mymusic.domain.UserResult;

import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据处理工具类
 */
public class DataUtil {
    /**
     * 更改是否在播放列表字段
     * <p>
     * 这个方法的局部变量比如datum，如有变量传入进来，都是一起指向堆内存的对象的
     */
    public static void changePlayListFlag(List<Song> datum, boolean value) {
        //遍历播放列表所有Song，更改其标志（是否在播放列表）
        for (Song data : datum) {
            //如果传入true（标志flag为：true），则datum（播放列表）里面的Song都在播放列表；
            // 否则不在播放列表
            data.setPlayList(value);
        }
    }

    /**
     * 根据用户昵称计算出拼音
     * 因为这里是数据处理，所以放到DataUtil中来处理，当然如果放到拼音工具类中也是可以的
     * 思路：List<User>(包含nickname) 遍历-->设置拼音 拼音首字母 拼音首字母中的首字母、首字母
     */
    public static List<User> processUserPinyin(List<User> datum) {
        for (User user : datum) {
            //获取该用户user的全拼
            user.setPinyin(PinyinUtil.pinyin(user.getNickname()));
            //获取拼音首字母
            //例如："爱学啊"
            //结果为：axa
            user.setPinyinFirst(PinyinUtil.pinyinFirst(user.getNickname()));
            //拼音首字母的首字母
            //因为前面已经设置了拼音中的首字母了，所以这里直接获取设置就行了
            user.setFirst(user.getPinyinFirst().substring(0, 1));
        }
        //记得返回list(此时list user中不仅包含了昵称,还包含了昵称的拼音)
        return datum;
    }

    /**
     * 根据用户首字母分组(List<User>排序，遍历并添加标题如A和user对象到list集合中，用UserResult包装起来)
     * <p>
     * UserResult:为啥要创建一个对象，后面要返回多个数据，所以用result包装下
     * <p>
     * 添加
     * A (标题)
     * user(包含有用户昵称和拼音)
     * user
     * B(标题)
     * user
     * user
     * 这种形式
     * 添加到集合list<Object>上,并用UserResult包装到里面
     */
    public static UserResult processUser(List<User> datum) {

        //创建结果数组(这里用Object：要添加不同的类型)
        List<Object> results = new ArrayList<>();

        //按照第一个字母排序
        //这里使用的Guava提供的排序方法(需要添加依赖，google给我们封装的)
        //也可以使用Java的排序方法
        Ordering<User> byFirst = new Ordering<User>() {
            @Override
            public int compare(@NullableDecl User left, @NullableDecl User right) {
                //根据第一个字母排序(这里是字母升序a-z)
                //如果是降序则把left和right倒过来就行了
                return left.getFirst().compareTo(right.getFirst());
            }
        };

        //按照拼音首字母的第一个字母分组
        //这些操作都可以使用响应式编程方法
        //这里为了简单使用了最普通的方法
        //因为只要明白了原理
        //使用其他方法就是语法不同而已

        //排序(//immutable:表示返回的数组不可变，不可变可以理解为在一定程度是是更高效的)
        datum = byFirst.immutableSortedCopy(datum);

        //循环所有数据

        //上一次用户
        User lastUser = null;

        for (User data :
                datum) {
            if (lastUser != null && lastUser.getFirst().endsWith(data.getFirst())) {
                //相等
                //上一次用户的首字母，是否等于当前用户的首字母
                //这个用户昵称(比如:爱学啊smile1 和 爱学啊smile2 的首字母 都是a)，
                // 所以就不添加大写，直接添加用户user到集合就行了
            } else {
                //添加标题(就是把首字母大写添加 到results集合list里面)
                String letter = data.getFirst().toUpperCase();

                //添加标题
                results.add(letter);
            }

            //添加用户(添加用户User对象)
            results.add(data);
            //赋值给上一个用户(记得赋值给上一次用户)
            lastUser = data;
        }
        //包装到UserResult对象上
        return new UserResult(results);
    }

    /**
     * 返回用户测试数据(list user中包含了昵称)
     * w (我的云音乐)
     * a (爱学啊smile 或者 爱学啊李薇3)
     * <p>
     * 我们希望数据处理完成 顺序为 a w
     *
     * @return
     */
    public static List<User> getTestUserData() {
        //创建一个列表
        ArrayList<User> results = new ArrayList<>();

        //全中文(这里的拼音首字母为w)
        User user = null;
        for (int i = 0; i < 50; i++) {
            user = new User();
            user.setNickname("我的云音乐" + i);
            results.add(user);
        }

        //下面2个user中首字母都是a，而上面的那个不是a
        //有单词(数据中带有单词的)
        for (int i = 0; i < 50; i++) {
            user = new User();
            user.setNickname("爱学啊smile" + i);
            results.add(user);
        }
        //全中文
        for (int i = 0; i < 50; i++) {
            user = new User();
            user.setNickname("爱学啊李薇" + i);
            results.add(user);
        }
        //List<User>  user中包含了昵称
        return results;
    }
}
