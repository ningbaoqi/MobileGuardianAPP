package ningbaoqi.com.mobileguardianapp.utils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by root on 18-4-23.
 * 归属地查询数据库工具
 */

public class LocationNumberSearchDatabase {
    private static final String PATH = "data/data/ningbaoqi.com.mobileguardianapp/files/address.db";//该路径必须是手机的真实目录
    private SQLiteDatabase sqLiteDatabase;

    public static String getAddress(String number) {
        String address = "未知号码";
        /**
         * 打开导入的数据库,根据路径和模式打开数据库
         * */
        SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openDatabase(PATH, null, SQLiteDatabase.OPEN_READONLY);
        /**
         * 使用正则表达式
         * */
        if (number.matches("^1[3-8]\\d{9}$")) {
            Cursor cursor = sqLiteDatabase.rawQuery("select location from data2 where id=(select outkey from data1 where id = ?)", new String[]{number.substring(0, 7)});
            if (cursor.moveToNext()) {
                address = cursor.getString(0);
            }
            cursor.close();
        } else if (number.matches("^\\d+$")) {
            switch (number.length()) {
                case 3:
                    address = "报警电话";
                    break;
                case 4:
                    address = "模拟器";
                    break;
                case 5:
                    address = "客服电话";
                    break;
                case 7:
                case 8:
                    address = "本地号码";
                    break;
                default:
                    if (number.startsWith("0") && number.length() > 10) {//有可能是长途电话，有些区号是四位，有些区号是三位
                        Cursor cursor = sqLiteDatabase.rawQuery("select location from data2 where area = ?", new String[]{number.substring(1, 4)});//截取4位区号
                        if (cursor.moveToNext()) {
                            address = cursor.getString(0);
                        } else {
                            cursor.close();
                            cursor = sqLiteDatabase.rawQuery("select location from data2 where area = ?", new String[]{number.substring(1, 3)});//‘截取三位区号
                            if (cursor.moveToNext()) {
                                address = cursor.getString(0);
                            }
                            cursor.close();
                        }
                    }
            }
        }
        sqLiteDatabase.close();
        return address;
    }
}
