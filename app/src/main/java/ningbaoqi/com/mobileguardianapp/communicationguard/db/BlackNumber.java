package ningbaoqi.com.mobileguardianapp.communicationguard.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;

import java.util.ArrayList;
import java.util.List;

import ningbaoqi.com.mobileguardianapp.communicationguard.bean.BlackNumberInfo;

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

    private BlackNumberOpenHelper openHelper;

    public BlackNumber(Context context) {
        openHelper = new BlackNumberOpenHelper(context);
    }

    /**
     * @param number 黑名单号码
     * @param mode   拦截模式
     */
    public boolean add(String number, String mode) {
        SQLiteDatabase db = openHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("number", number);
        values.put("mode", mode);
        long lowid = db.insert("blacknumber", null, values);
        if (lowid == -1) {
            return false;
        }
        return true;
    }

    /**
     * 根据电话号码删除
     *
     * @param number
     */
    public boolean delete(String number) {
        SQLiteDatabase db = openHelper.getWritableDatabase();
        int rowNumber = db.delete("blacknumber", "number = ?", new String[]{number});
        if (rowNumber == 0) {
            return false;
        }
        return true;
    }

    /**
     * 通过电话号码修改拦截模式
     *
     * @param number
     */
    public boolean changeNumberMode(String number, String mode) {
        SQLiteDatabase db = openHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("mode", mode);
        int rowNumber = db.update("blacknumber", values, "number = ?", new String[]{number});
        if (rowNumber == 0) {
            return false;
        }
        return true;
    }

    /**
     * 通过电话号码查找
     *
     * @return
     */
    public String findNumber(String number) {
        String mode = "";
        SQLiteDatabase db = openHelper.getReadableDatabase();
        Cursor cursor = db.query("blacknumber", new String[]{"mode"}, "number = ?", new String[]{number}, null, null, null);
        if (cursor.moveToNext()) {
            mode = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return mode;
    }

    /**
     * 查询所有的黑名单
     *
     * @return
     */
    public List<BlackNumberInfo> findAll() {
        SQLiteDatabase db = openHelper.getReadableDatabase();
        List<BlackNumberInfo> list = new ArrayList<>();
        Cursor cursor = db.query("blacknumber", new String[]{"number", "mode"}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String number = cursor.getString(0);
            String mode = cursor.getString(1);
            BlackNumberInfo info = new BlackNumberInfo();
            info.setNumber(number);
            info.setMode(mode);
            list.add(info);
        }
        cursor.close();
        db.close();
        SystemClock.sleep(3000);
        return list;
    }

    /**
     * 实现分页加载数据
     *
     * @param pageNumber 表示当前是哪一页
     * @param pageSize   表示每一页有多少条数据
     * @return limit 表示限制，表示当前限制有多少数据
     * offset 表示跳过，从第几条开始
     */
    public List<BlackNumberInfo> findPar(int pageNumber, int pageSize) {
        SQLiteDatabase db = openHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select number , mode from blacknumber limit ? offset ?", new String[]{String.valueOf(pageSize), String.valueOf(pageSize * pageNumber)});
        ArrayList<BlackNumberInfo> blackNumberInfos = new ArrayList<>();
        while (cursor.moveToNext()) {
            BlackNumberInfo info = new BlackNumberInfo();
            info.setMode(cursor.getString(1));
            info.setNumber(cursor.getString(0));
            blackNumberInfos.add(info);
        }
        cursor.close();
        db.close();
        return blackNumberInfos;
    }

    /**
     * 获取总的记录数
     *
     * @return
     */
    public int getTotalNumber() {
        SQLiteDatabase db = openHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from blacknumber", null);
        cursor.moveToNext();
        int count = cursor.getInt(0);
        cursor.close();
        db.close();
        return count;
    }
}
