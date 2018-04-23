package ningbaoqi.com.mobileguardianapp.advancetool.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import ningbaoqi.com.mobileguardianapp.utils.LocationNumberSearchDatabase;

/**
 * Created by ningbaoqi on 18-4-23.
 * 来电提醒服务
 */

public class AddressService extends Service {

    private MyListener listener;
    private TelephonyManager telephonyManager;
    private OutCallReceiver receiver;

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
        telephonyManager.listen(listener , PhoneStateListener.LISTEN_NONE);//取消监听
        unregisterReceiver(receiver);
    }

    class MyListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    Log.d("nbq", "响铃");
                    String address = LocationNumberSearchDatabase.getAddress(incomingNumber);
                    Toast.makeText(AddressService.this , address , Toast.LENGTH_LONG).show();
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    Log.d("nbq", "空闲");
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
     * */
    class OutCallReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
                String number = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
                Log.d("nbq", "number:-----" + number);
                String address = LocationNumberSearchDatabase.getAddress(number);
                Toast.makeText(context , address , Toast.LENGTH_LONG).show();
            }
        }
    }
}
