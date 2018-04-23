package ningbaoqi.com.mobileguardianapp.settings.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import ningbaoqi.com.mobileguardianapp.R;
import ningbaoqi.com.mobileguardianapp.advancetool.service.AddressService;
import ningbaoqi.com.mobileguardianapp.settings.view.SettingItemView;
import ningbaoqi.com.mobileguardianapp.utils.ServiceStatusUtils;
import ningbaoqi.com.mobileguardianapp.utils.SharedPreferenceItemConfig;

/**
 * Created by ningbaoqi on 18-4-21.
 */

public class SettingsActivity extends AppCompatActivity {

    private SettingItemView sivAddress;
    private SettingItemView sivUpdate;
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);
        sharedPreferences = getSharedPreferences(SharedPreferenceItemConfig.SharedPreferenceFileName, MODE_PRIVATE);
        initUpdate();
        initAddress();
    }

    /**
     * 初始化自动升级逻辑
     */
    private void initUpdate() {
        sivUpdate = (SettingItemView) findViewById(R.id.sivupdate);
        boolean isAuto = sharedPreferences.getBoolean(SharedPreferenceItemConfig.SharedPreferenceAutoUpdate, true);
        sivUpdate.setChecked(isAuto);
        sivUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sivUpdate.isChecked()) {
                    sivUpdate.setChecked(false);
                    sharedPreferences.edit().putBoolean(SharedPreferenceItemConfig.SharedPreferenceAutoUpdate, false).commit();
                } else {
                    sivUpdate.setChecked(true);
                    sharedPreferences.edit().putBoolean(SharedPreferenceItemConfig.SharedPreferenceAutoUpdate, true).commit();
                }
            }
        });
    }

    /**
     * 初始化归属地逻辑
     */
    private void initAddress() {
        sivAddress = (SettingItemView) findViewById(R.id.sivAddress);
        boolean isRunning = ServiceStatusUtils.isServiceRunning(this, "ningbaoqi.com.mobileguardianapp.advancetool.service.AddressService");
        if (isRunning) {
            sivAddress.setChecked(true);
        } else {
            sivAddress.setChecked(false);
        }
        sivAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sivAddress.isChecked()) {
                    sivAddress.setChecked(false);
                    startService(new Intent(SettingsActivity.this, AddressService.class));
                } else {
                    sivAddress.setChecked(true);
                    startService(new Intent(SettingsActivity.this, AddressService.class));
                }
            }
        });
    }
}
