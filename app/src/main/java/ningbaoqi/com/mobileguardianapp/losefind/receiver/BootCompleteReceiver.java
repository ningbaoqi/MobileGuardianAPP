package ningbaoqi.com.mobileguardianapp.losefind.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import ningbaoqi.com.mobileguardianapp.losefind.activity.ContactActivity;
import ningbaoqi.com.mobileguardianapp.losefind.service.ReceiveSMSService;
import ningbaoqi.com.mobileguardianapp.utils.SharedPreferenceItemConfig;

/**
 * Created by ningbaoqi on 18-4-22.
 * Android4.4以上版本已经不能让随意让第三方app接收短信了，所以需要开启服务来监听短信数据库的变化
 */

public class BootCompleteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPreferenceItemConfig.SharedPreferenceFileName, Context.MODE_PRIVATE);
        boolean protect = sharedPreferences.getBoolean(SharedPreferenceItemConfig.SharedPreferenceProtected, false);
        if (protect) {
            String sim = sharedPreferences.getString(SharedPreferenceItemConfig.SharedPreferenceSim, "");
            if (!TextUtils.isEmpty(sim)) {
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(ContactActivity.TELEPHONY_SERVICE);
                String currentSIM = telephonyManager.getSimSerialNumber();
                if (sim.equals(currentSIM)) {
                    Log.d("nbq", "safe");
                } else {
                    String phone = sharedPreferences.getString(SharedPreferenceItemConfig.SharedPreferenceSafeNumber, "");
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phone , null , "sim card change" , null , null);
                    context.startService(new Intent(context , ReceiveSMSService.class));
                }
            }
        }
    }
}
