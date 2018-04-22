package ningbaoqi.com.receivesmsservice;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by ningbaoqi on 18-4-22.
 */

public class MyService extends Service {

    private ContentResolver contentResolver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("nbq", "onCreate");
        contentResolver = getContentResolver();
        contentResolver.registerContentObserver(Uri.parse("content://sms"), true, new MyContentObserver(new Handler()));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("nbq", "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("nbq", "onDestroy");
    }

    class MyContentObserver extends ContentObserver {


        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        public MyContentObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            Log.d("nbq", "短信数据库发生变化了");
            Cursor cursor = contentResolver.query(Uri.parse("content://sms"), new String[]{"address", "body"}, null, null, null);
            if (cursor != null) {
                if (cursor.moveToNext()) {
                    String address = cursor.getString(0);
                    String body = cursor.getString(1);
                    Log.d("nbq", "address : " + address + "----------" + "body:" + body);

                }
            }
        }
    }
}
