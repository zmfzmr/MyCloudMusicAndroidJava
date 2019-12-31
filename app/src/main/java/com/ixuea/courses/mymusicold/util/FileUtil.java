package com.ixuea.courses.mymusicold.util;

/**
 * 文件工具类
 */
public class FileUtil {
    /**
     * 文件大小格式化
     *
     * @param value 文件大小；单位byte
     * @return 格式化的文件大小；例如：1.22M
     * K M G 统一为大写 防止出错
     */
    public static String formatFileSize(long value) {
        if (value > 0) {
            double size = (double) value;//记得要先转换成double类型的
            double kiloByte = size / 1024;
            if (kiloByte < 1 && kiloByte > 0) {//kiloByte > 0有这个
                //不足1k
//                return size + "Byte";//不能用这个，用这个会报错
                return String.format("%.2fByte", size);
            }
            //%.2f :%.2f自动四舍五入(保留2位小数并四舍五入)
            double megaByte = kiloByte / 1024;
            if (megaByte < 1) {
                //不足1M（注意：不足1M，用的是kiloByte）
                return String.format("%.2fK", kiloByte);
            }

            double gigaByte = megaByte / 1024;
            if (gigaByte < 1) {
                //不足1G，用的是M
                return String.format("%.2fM", megaByte);
            }

            double teraByte = gigaByte / 1024;
            if (teraByte < 1) {
                //不足1T
                return String.format("%.2fG", gigaByte);
            }

        }
        return "1k";
    }
}
