package ningbaoqi.com.mobileguardianapp.advancetool.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import ningbaoqi.com.mobileguardianapp.R;
import ningbaoqi.com.mobileguardianapp.utils.LocationNumberSearchDatabase;
import ningbaoqi.com.mobileguardianapp.utils.SharedPreferenceItemConfig;

/**
 * Created by ningbaoqi on 18-4-23.
 * 来电提醒服务
 */

public class AddressService extends Service {

    private MyListener listener;
    private TelephonyManager telephonyManager;
    private OutCallReceiver receiver;
    private WindowManager windowManager;
    private TextView textView;
    private View view;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        listener = new MyListener();
        telephonyManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);//来电监听
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
        receiver = new OutCallReceiver();
        registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        telephonyManager.listen(listener, PhoneStateListener.LISTEN_NONE);//取消监听
        unregisterReceiver(receiver);
    }

    class MyListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    Log.d("nbq", "响铃");
                    String address = LocationNumberSearchDatabase.getAddress(incomingNumber);
                    showToast(address);
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    Log.d("nbq", "空闲");
                    if (windowManager != null && view != null) {
                        windowManager.removeView(view);
                        view = null;
                    }
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Log.d("nbq", "挂断");
                    break;
            }
            super.onCallStateChanged(state, incomingNumber);
        }
    }

    /**
     * 去电广播接收器,应该是系统修改了不让第三方应用监听去电广播了
     */
    class OutCallReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
                String number = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
                Log.d("nbq", "number:-----" + number);
                String address = LocationNumberSearchDatabase.getAddress(number);
                Toast.makeText(context, address, Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * 自定义归属地浮窗
     */
    private void showToast(String text) {
        //可以在其他第三方app中弹出自己的浮窗
        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();//设置配置属性
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        params.format = PixelFormat.TRANSLUCENT;
        params.type = WindowManager.LayoutParams.TYPE_TOAST;
        params.setTitle("Toast");
        sharedPreferences = getSharedPreferences(SharedPreferenceItemConfig.SharedPreferenceFileName, MODE_PRIVATE);
        int position = sharedPreferences.getInt(SharedPreferenceItemConfig.SharedPreferenceAddressStyle, 0);
        TypedArray typedArray = getResources().obtainTypedArray(R.array.address_single_drawable_array );
        view = View.inflate(this, R.layout.toast_view, null);
        view.setBackgroundResource(typedArray.getResourceId(position , 0));
        typedArray.recycle();
        textView = view.findViewById(R.id.tv_number);
        textView.setText(text);
        windowManager.addView(view, params);//将view添加到屏幕上，即Window上
    }
}
