package ningbaoqi.com.mobileguardianapp.communicationguard.db;

import android.content.Context;

/**
 * =========================================
 * <p/>
 * 版    权: ningxiansheng
 * <p/>
 * 作    者: 宁宝琪
 * <p/>
 * 版    本:1.0
 * <p/>
 * 创建 时间:18-4-24
 * <p/>
 * 描    述:
 * <p/>
 * <p/>
 * 修订 历史:
 * <p/>
 * =========================================
 */

public class BlackNumber {

    private final BlackNumberOpenHelper openHelper;

    public BlackNumber(Context context) {
        openHelper = new BlackNumberOpenHelper(context);
    }

    /**
     * @param number 黑名单号码
     * @param mode   拦截模式
     */
    public boolean add(String number, String mode) {
        return false;
    }

    /**
     * 根据电话号码删除
     *
     * @param number
     */
    public boolean delete(String number) {
        return false;
    }

    /**
     * 通过电话号码修改拦截模式
     *
     * @param number
     */
    public boolean changeNumberMode(String number) {
        return false;
    }

    /**
     * 通过电话号码查找
     *
     * @return
     */
    public String findNumber() {
        return "";
    }
}
