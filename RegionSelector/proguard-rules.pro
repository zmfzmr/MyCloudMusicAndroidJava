# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\AndroidStudio\sdk\android-sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

#警告：该文件只配置了部分混淆规则
#所以直接开启混淆后
#可能无法打包或者打出来的包无法正常工作

#Glide图片加载框架
#保留 实现了接口的类
#保留 继承了该类的子类
#保留 公开的枚举
#-keep public class * implements com.bumptech.glide.module.GlideModule
#-keep public class * extends com.bumptech.glide.module.AppGlideModule
#-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
#  **[] $VALUES;
#  public *;
#}
#
## for DexGuard only    这个DexGuard是Android才有的，java的是其他的
## 保留了android资源文件下的   值为GlideModule 的这种标签
#-keepresourcexmlelements manifest/application/meta-data@value=GlideModule
##end Glide图片加载框架

# 上面的部分先注释 因为开混淆后，打包可能会出错 所以先注释
