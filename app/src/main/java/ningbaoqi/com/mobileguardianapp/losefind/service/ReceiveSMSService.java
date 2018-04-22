package ningbaoqi.com.mobileguardianapp.losefind.service;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import ningbaoqi.com.mobileguardianapp.R;
import ningbaoqi.com.mobileguardianapp.utils.SharedPreferenceItemConfig;
import ningbaoqi.com.mobileguardianapp.utils.SpecialCommand;

/**
 * Created by ningbaoqi on 18-4-22.
 * 监听短信数据库变化的服务
 */

public class ReceiveSMSService extends Service {
    private ContentResolver contentResolver;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("nbq", "onCreate");
        sharedPreferences = getSharedPreferences(SharedPreferenceItemConfig.SharedPreferenceFileName, MODE_PRIVATE);
        contentResolver = getContentResolver();
        contentResolver.registerContentObserver(Uri.parse("content://sms"), true, new MyContentObserver(new Handler()));
    }

    class MyContentObserver extends ContentObserver {

        private MediaPlayer mediaPlayer;

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
                    if (address.equals(sharedPreferences.getString(SharedPreferenceItemConfig.SharedPreferenceSafeNumber, "--")) && body.equals(SpecialCommand.ALARM)) {
                        //启动铃声
                        Log.d("nbq", "启动铃声");
                        mediaPlayer = MediaPlayer.create(ReceiveSMSService.this , R.raw.ylzs);
                        if (mediaPlayer.isPlaying()) {
                            return;
                        }
                        mediaPlayer.setVolume(1f , 1f);
                        mediaPlayer.setLooping(true);
                        mediaPlayer.start();
                    } else if (address.equals(sharedPreferences.getString(SharedPreferenceItemConfig.SharedPreferenceSafeNumber, "--")) && body.equals(SpecialCommand.LOCATION)) {
                        //启动定位
                        Log.d("nbq", "启动定位");
                    } else if (address.equals(sharedPreferences.getString(SharedPreferenceItemConfig.SharedPreferenceSafeNumber, "--")) && body.equals(SpecialCommand.WIPEDATA)) {
                        //启动擦除数据
                        Log.d("nbq", "启动擦除数据");
                    } else if (address.equals(sharedPreferences.getString(SharedPreferenceItemConfig.SharedPreferenceSafeNumber, "--")) && body.equals(SpecialCommand.LOCATION)) {
                        //启动锁屏
                        Log.d("nbq", "启动锁屏");
                    }
                }
            }
        }
    }
}
