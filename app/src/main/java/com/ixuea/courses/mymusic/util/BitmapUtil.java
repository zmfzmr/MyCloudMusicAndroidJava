package com.ixuea.courses.mymusic.util;

import android.graphics.Bitmap;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Bitmap，相关工具方法
 */
public class BitmapUtil {
    /**
     * 将Bitmap保存到文件
     *
     * @param bitmap Bitmap
     * @param file   File
     */
    public static boolean saveToFile(Bitmap bitmap, File file) {
//        //写法1：
//        //Java对流常规写法
//        FileOutputStream out = null;
//        try {
//            //创建输出流
//            out = new FileOutputStream(file);
//            //参数1：格式（比如jpg）2：质量，越大越好 3，输出流   就是Bitmap保存压缩到这个输出流（包含文件）上
//           bitmap.compress(Bitmap.CompressFormat.JPEG,90,out);
//
//           return true;//表示Bitmap保存到File成功
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } finally {
//            //关闭流
//            //标准的关闭流写法还是比较复杂的
//            //同时由于这不是Java基础课程
//            //所以使用了apache的common io工具包关闭
//
//            //可以看到该方法以及过时了
//            //后面会讲解新的写法
//            IOUtils.closeQuietly(out);
//
//        }
        //写法2:
        //使用try-with-resource
        //他是JDK7实现了一个语法糖
        //语法糖 只是在编译这个层面 做了一个处理，编译完成后，还是会把代码编译成上面的那种代码（try catch finally这种代码）
        //这种写法，这io流用完后，会自动关闭流
        try (FileOutputStream out = new FileOutputStream(file)) {
            //保存bitmap
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            return true;//表示Bitmap保存到File成功
        } catch (Exception e) {
            e.printStackTrace();
        }

        //默认：表示Bitmap保存到File失败
        return false;
    }
}
