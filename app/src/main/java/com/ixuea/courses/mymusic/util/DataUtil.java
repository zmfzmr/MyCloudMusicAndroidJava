package com.ixuea.courses.mymusic.util;

import com.ixuea.courses.mymusic.domain.Song;
import com.ixuea.courses.mymusic.domain.User;

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
