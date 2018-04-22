package ningbaoqi.com.mobileguardianapp.utils;

/**
 * Created by ningbaoqi on 18-4-20.
 */

public class SharedPreferenceItemConfig {
    /**
     * SharedPreference的文件名
     */
    public static final String SharedPreferenceFileName = "config";
    /**
     * 在设置中心设置的，控制是否自动检测服务器版本
     */
    public static final String SharedPreferenceAutoUpdate = "auto_update";
    /**
     * 手机防盗对话框设置的密码
     */
    public static final String SharedPreferencePassword = "password";
    /**
     * 控制手机防盗功能有没有配置过，配置了就不会去再进入导航界面
     */
    public static final String SharedPreferenceConfiged = "configed";
    /**
     * 设置的安全号码
     */
    public static final String SharedPreferenceSafeNumber = "safe_number";
    /**
     * 防盗保护是否已经开启
     */
    public static final String SharedPreferenceProtected = "protected";
    /**
     * sim卡的序列号,有序列号说明绑定了
     * */
    public static final String SharedPreferenceSim = "sim";
    /**
     * 坐标
     */
    public static final String SharedPreferenceLocation = "location";
}
