package ningbaoqi.com.mobileguardianapp.utils;

import java.security.MessageDigest;

/**
 * Created by ningbaoqi on 18-4-20.
 * MD5加密工具类
 * MD5的加盐处理，就是如将 密码 + 用户名 + 标志位一起进行MD5运算，然后进行加密
 */

public class MD5Utils {
    /**
     * 加密
     */
    public static String encode(String password) {
        try {
            /**
             * 获取MD5算法对象
             * */
            MessageDigest instance = MessageDigest.getInstance("MD5");
            /**
             * 对字符串加密，返回字节数组
             * */
            byte[] digest = instance.digest(password.getBytes());
            StringBuffer buffer = new StringBuffer();
            for (byte b : digest) {
                /**
                 * 获取字节的低八位有效值
                 * */
                int i = b & 0xff;
                /**
                 * 将整数转换为16进制数
                 * */
                String hexString = Integer.toHexString(i);
                if (hexString.length() < 2) {
                    /**
                     * 如果是一位则补0
                     * */
                    hexString = "0" + hexString;
                }
                buffer.append(hexString);
            }
            return buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
