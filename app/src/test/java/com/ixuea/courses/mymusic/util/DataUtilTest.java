package com.ixuea.courses.mymusic.util;

import com.ixuea.courses.mymusic.domain.User;
import com.ixuea.courses.mymusic.domain.UserResult;

import org.junit.Test;

import java.util.List;

import static junit.framework.TestCase.assertEquals;

/**
 * 数据工具类测试
 */
public class DataUtilTest {
    /**
     * 测试 根据用户昵称计算出拼音
     * 思路：List<User>(包含nickname) 遍历-->设置拼音 拼音首字母 拼音首字母中的首字母、首字母
     */
    @Test
    public void testProcessUserPinyin() {
        //创建测试数据(list包含user(此时有nickname昵称))
        List<User> users = DataUtil.getTestUserData();

        //处理数据(把list中包含nickname的user 都设置昵称拼音 昵称拼音首字母、首字母中的首字母 )
        //(其实里面就是遍历设置)
        List<User> results = DataUtil.processUserPinyin(users);

        //---------判断第40个用户的数据
        //判断全拼
        //这里没有处理多音
        //正确的应该是wodeyunyinyue40
        //但他默认为wodeyunyinle40
        //这里就不在处理了
        //但大家要知道有这样的问题
        //但如果要在真实项目中使用
        //还需要根据特定的情况处理
        //官方文档中有多音配置文件方法

        //数据中：我的云音乐1 类似的数据有50个，这里获取其中一个
        //这里的索引40，其实是获取第41个用户，所以是wodeyunyinle40(这个索引为40的User对象拼音)
        User user = results.get(40);
        //因为第40条的昵称是：我的云音乐40；在前面的DataUtil.processUserPinyin(users)处理的时候，
        // 里面调用PinyinUtil.pinyin等方法处理的时候，会遍历每一个字符，因为40不是中文，所以没有转成拼音，
        // 所以直接返回来了
        assertEquals(user.getPinyin(), "wodeyunyinle40");
        //判断拼音首字母
        assertEquals(user.getPinyinFirst(), "wdyyl40");
        //判断拼音首字母的首字母
        assertEquals(user.getFirst(), "w");

        //---------判断第60个用户的数据(这里是第二组数据，所以第10个)
        user = results.get(60);

        assertEquals(user.getPinyin(), "aixueasmile10");

        //smile:就算遍历每个字符都不是中文字符，所以还是会返回smile
        assertEquals(user.getPinyinFirst(), "axasmile10");

        assertEquals(user.getFirst(), "a");

        //---------判断第120个用户的数据
        user = results.get(120);

        assertEquals(user.getPinyin(), "aixuealiwei20");

        //smile:就算遍历每个字符都不是中文字符，所以还是会返回smile
        assertEquals(user.getPinyinFirst(), "axalw20");

        assertEquals(user.getFirst(), "a");

    }

    /**
     * 测试 根据首字母分组
     */
    @Test
    public void testProcessUser() {
        //获取测试数据
        List<User> users = DataUtil.getTestUserData();
        //获取拼音(把list中user都设置拼音)
        users = DataUtil.processUserPinyin(users);
        //分组(UserResult:包含了list（分组数据的集合）)
        UserResult userResult = DataUtil.processUser(users);
        //判断总数据数量
        List<Object> datum = userResult.getDatum();
        //3组数据+2个标题
        assertEquals(userResult.getDatum().size(), 152);
        //TODO 更多测试条件
    }
}
