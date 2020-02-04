package com.ixuea.courses.mymusic.activity;

import com.ixuea.courses.mymusic.util.LogUtil;

import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;

/**
 * 这类仿照ExampleInstrumentedTest类中写
 * <p>
 * UI测试可以使用android那边的APi类（比如LogUtil）
 * 如果是单元测试的话，是不可以使用的
 */
@RunWith(AndroidJUnit4.class)//(注意：这里单元测试时没有的，这里是UI测试，所以需要加上)
public class LoginActivityTest {

    private static final String TAG = "LoginActivityTest";

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

    }
}
