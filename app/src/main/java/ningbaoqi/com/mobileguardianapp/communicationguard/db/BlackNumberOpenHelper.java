package ningbaoqi.com.mobileguardianapp.communicationguard.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

public class BlackNumberOpenHelper extends SQLiteOpenHelper {
    private static final String BLACKNUMBERDATABASENAME = "safe.db";

    public BlackNumberOpenHelper(Context context) {
        super(context, BLACKNUMBERDATABASENAME, null, 1);
    }

    /**
     * blacknumber 表名
     * _id 主键自动增长
     * number 电话号码
     * mode 拦截模式 电话拦截 短信拦截
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table blacknumber (_id , integer primary key autoincrement , number varchar(20) , mode varchar(2))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
