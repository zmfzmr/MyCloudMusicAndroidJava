package com.ixuea.courses.mymusic;

import android.content.Context;

import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    /**
     * 测试当前应用包名
     */

    @Test
    public void useAppContext() {
        //app测试上下文
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        //测试包名 包名改为com.ixuea.courses.mymusic （改为和applicationId一样）
        assertEquals("com.ixuea.courses.mymusic", appContext.getPackageName());
    }
}
