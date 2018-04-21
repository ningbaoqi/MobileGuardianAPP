package ningbaoqi.com.mobileguardianapp.losefind.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import ningbaoqi.com.mobileguardianapp.R;
import ningbaoqi.com.mobileguardianapp.settings.view.SettingItemView;
import ningbaoqi.com.mobileguardianapp.utils.SharedPreferenceItemConfig;

/**
 * Created by ningbaoqi on 18-4-21.
 * 第二个手机向导页
 */

public class Setup2Activity extends BaseSetupActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setup2_layout);
        final SettingItemView settingItemView = (SettingItemView) findViewById(R.id.sivSIM);
        String sim = sharedPreferences.getString(SharedPreferenceItemConfig.SharedPreferenceSim, null);
        if (!TextUtils.isEmpty(sim)) {
            settingItemView.setChecked(true);
        } else {
            settingItemView.setChecked(false);
        }
        settingItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (settingItemView.isChecked()) {
                    settingItemView.setChecked(false);
                    sharedPreferences.edit().remove(SharedPreferenceItemConfig.SharedPreferenceSim).commit();
                } else {
                    settingItemView.setChecked(true);
                    /**
                     * 保存SIM卡的信息，获取SIM卡的序列号，识别SIM卡的唯一标识，并保存起来
                     * */
                    TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    String simSerialNumber = telephonyManager.getSimSerialNumber();
                    sharedPreferences.edit().putString(SharedPreferenceItemConfig.SharedPreferenceSim, simSerialNumber).commit();
                }
            }
        });
    }

    @Override
    protected void showPreviousPage() {
        startActivity(new Intent(Setup2Activity.this, Setup1Activity.class));
        finish();
        overridePendingTransition(R.anim.traslate_previous_in, R.anim.translate_previous_out);
    }

    @Override
    protected void showNextPage() {
        if (TextUtils.isEmpty(sharedPreferences.getString(SharedPreferenceItemConfig.SharedPreferenceSim, null))) {
            Toast.makeText(this, "必须绑定SIM卡", Toast.LENGTH_LONG).show();
            return;
        }
        startActivity(new Intent(Setup2Activity.this, Setup3Activity.class));
        finish();
        overridePendingTransition(R.anim.tranlate_in, R.anim.tranlate_out);
    }
}
