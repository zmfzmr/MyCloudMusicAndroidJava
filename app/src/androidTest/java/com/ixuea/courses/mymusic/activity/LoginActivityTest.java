package com.ixuea.courses.mymusic.activity;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.util.LogUtil;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

/**
 * 这类仿照ExampleInstrumentedTest类中写
 * <p>
 * UI测试可以使用android那边的APi类（比如LogUtil）
 * 如果是单元测试的话，是不可以使用的
 */
//@LargeTest 这个暂时先不要
@RunWith(AndroidJUnit4.class)//(注意：这里单元测试时没有的，这里是UI测试，所以需要加上)
public class LoginActivityTest {

    private static final String TAG = "LoginActivityTest";

    /**
     * 这里是仿照 录制方式保存的Temp文件里面的 来写的
     * <p>
     * 当前要测试的界面
     * 我们这里要测试的是LoginActivity（等下执行测试的时候回跳转到LoginActivity界面）
     */
    @Rule
    public ActivityTestRule<LoginActivity> activityTestRule
            = new ActivityTestRule<>(LoginActivity.class);



    /**
     * 测试方法
     * 每个测试方法是独立的
     * 也就是说执行每个测试方法前都会
     * 显示当前界面
     */
    @Test//记得加上这个
    public void testOther() {
        LogUtil.d(TAG, "testOther");
    }

    /**
     * 测试登录
     */
    @Test
    public void testLogin() {
        try {
            //等待1秒钟
            //因为应用启动要时间
            sleep();

            //现在是登录界面

            //找到登录按钮
//            ViewInteraction btLogin = Espresso.onView(withId (R.id.bt_login));
            ViewInteraction btLogin = findViewById(R.id.bt_login);

            //---------不输入用户名和密码
            //点击登录按钮
            btLogin.perform(click());

            //确认提示是否正确(检查Toast) （如果不正确到的话会抛出错误）
            checkToast(R.string.enter_username);

            //延迟1s
            //因为toast显示有时间（让原来的toast消失以后，再去测试）
            sleep();

            //---------输入1位用户名
            //找到用户名输入框
            ViewInteraction etUserName = findViewById(R.id.et_username);

            //输入用户名
            editTextInput(etUserName, "1");

            //点击登录按钮
            viewClick(btLogin);//这里用的是封装后的方法

            checkToast(R.string.error_username_format);

            //延迟1s
            //因为toast显示有时间（让原来的toast消失以后，再去测试）
            sleep();

            //---------输入正确的邮箱
            //清除原来的用户名
            clearEditText(etUserName);

            //输入用户名
            editTextInput(etUserName, "ixueaedu@163.com");

            //点击登录按钮
            viewClick(btLogin);

            //确认提示是否正确
            checkToast(R.string.enter_password);

            //延迟1s(这里改成了2s)
            sleep();

            //---------输入1位密码
            //找到密码输入框
            ViewInteraction etPassword = findViewById(R.id.et_password);

            //输入密码
            editTextInput(etPassword, "i");

            //点击登录按钮
            viewClick(btLogin);

            //确认提示是否正确
            checkToast(R.string.error_password_format);

            //延迟1s(这里改成了2s)
            sleep();

            //---------输入和账号匹配的密码
            //清除密码
            clearEditText(etPassword);

            //输入密码
            editTextInput(etPassword, "ixueaedu");

            //点击按钮
            viewClick(btLogin);

            //延迟3s(这里是3s)
            Thread.sleep(3000);

            //确认提示是否正确

            //判断DrawerLayout是否存在
            //因为首页有DrawerLayout
            //如果有就表示登陆成功
            //并进入了首页

            //找到DrawerLayout
            ViewInteraction dl = findViewById(R.id.dl);

            //判断是否显示了(就是检查这个控件在不在)
            dl.check(matches(isDisplayed()));


        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除EditText
     *
     * @param view
     */
    private void clearEditText(ViewInteraction view) {
        editTextInput(view, "");
    }

    /**
     * 延迟1s钟
     *
     * @throws InterruptedException
     */
    private void sleep() throws InterruptedException {
        Thread.sleep(2000);
    }

    /**
     * 点击按钮
     *
     * @param view ViewInteraction
     */
    private void viewClick(ViewInteraction view) {
        view.perform(click());
    }

    /**
     * EditText输入文本
     *
     * @param view   ViewInteraction
     * @param string String
     */
    private void editTextInput(ViewInteraction view, String string) {
        view.perform(replaceText(string), closeSoftKeyboard());
    }

    /**
     * 检查Toast是否存在
     *
     * @param resourceId resourceId
     */
    private void checkToast(int resourceId) {
        onView(withText(resourceId))
                .inRoot(RootMatchers.withDecorView(
                        Matchers.not(activityTestRule.getActivity().getWindow().getDecorView())
                ))//在某个根布局找
                //检查 这个Toast是不是显示状态
                .check(matches(isDisplayed()));//注意：这里是matches 不是matchers；是es不是er
    }

    /**
     * 根据id查找控件
     *
     * @param id 资源Id
     * @return onView(withId ( id))
     */
    private ViewInteraction findViewById(int id) {
        return onView(withId(id));
    }
}
