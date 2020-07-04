package com.ixuea.courses.mymusic.util;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import static com.ixuea.courses.mymusic.util.Constant.AES128_IV;
import static com.ixuea.courses.mymusic.util.Constant.AES128_KEY;

/**
 * 常用散列/哈希/加解密算法工具类
 */
public class DigestUtil {
    /**
     * sha1 签名
     *
     * @param data
     * @return
     */
    public static String sha1(String data) {
        //在传入的字符串前后加入了一些值，简称加盐
        //前面和后面的是加盐

        //什么是加盐？
        //就是混入一些内容

        //加盐的好处是
        //如果攻击者知道了我们使用的签名算法
        //但他不知道盐
        //也就无法计算出相同的结果

        //这里的盐是随机生成的
        //真实项目中可以随便更改
        //但要和服务端协商好
        data = String.format(Constant.SIGN_FORMAT, data);

        //这种方法在Android中会找不到如下方法
        //encodeHexString([B)Ljava/lang/String;
        //return DigestUtils.sha1Hex(string);

        //使用这种方法
        //DigestUtils.sha1(data) :原始数据其实是二进制，一般都是转为16进制，或者Base64编码，方便查看和传输。

        //Hex.encodeHex(DigestUtils.sha1(data))： 将sha1整除16进制 这个是byte数组,整除字符串
        // DigestUtils： 注意：这个DigestUtils是common包中和我们之前写的那个多个s

        return new String(Hex.encodeHex(DigestUtils.sha1(data)));
    }

    /**
     * 加密
     *
     * @param data
     * @return
     */

    public static String encryptAES(String data) {
        try {
            //获取密码算法类 Cipher.ENCRYPT_MODE： 加密的模式
            Cipher cipher = getAESCipher(Cipher.ENCRYPT_MODE);
            //加密
            byte[] result = cipher.doFinal(data.getBytes());

            //base64编码后返回 （有时候我们需要查看，所以这个base64编码 方便查看）
            return AndroidBase64Util.encodeByte2String(result);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 解密
     *
     * @param data
     * @return
     */
    public static String decryptAES(String data) {
        try {
            //获取密码库算法 类
            Cipher cipher = getAESCipher(Cipher.DECRYPT_MODE);

            //先使用base64解码(因为原来 加密返回的是 Bsse编码，那么解码的时候，也要Bsee64解码)
            byte[] dataByte = AndroidBase64Util.decodeString2byte(data);

            //解密
            byte[] result = cipher.doFinal(dataByte);

            //创建为字符串返回
            return new String(result);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 获取AES类
     */
    private static Cipher getAESCipher(int mode) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException {
        //使用PKCS5Padding
        //这是和服务端协商好的
        //所以这里不能随便更改

        //因为这里使用的是AES128算法
        //所以key，iv必须是16位
        //因为16位字符就是128位
        //16*8=128(1个字8位，16位字符就是16*8)，每个字符占用8位
        //也就是一个Byte

        //格式化key  Constant.AES128_KEY
        SecretKeySpec key = new SecretKeySpec(AES128_KEY.getBytes(), "AES");

        //确认算法
        //算法/模式/补码方式
        //AES有很多中模式，那我们需要哪种模式呢，那就要通过一种机制告诉他

        //算法/模式/补码方式
        //AES/CBC/PKCS5Padding:  和服务器协商好的，要一致
        //AES： 算法  CBC： 模式   PKCS5Padding： 填充(补码方式)
        //这里有异常，抛出去
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        //使用CBC模式
        //需要一个向量iv
        //可增加加密算法的强度
        IvParameterSpec iv = new IvParameterSpec(AES128_IV.getBytes());

        //初始化并传达key和iv (让Cipher 和key iv 产生关系)
        //异常向外抛出
        cipher.init(mode, key, iv);

        return cipher;
    }

}
