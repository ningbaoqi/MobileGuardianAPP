package ningbaoqi.com.mobileguardianapp.settings.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import ningbaoqi.com.mobileguardianapp.R;
import ningbaoqi.com.mobileguardianapp.settings.view.SettingItemView;
import ningbaoqi.com.mobileguardianapp.utils.SharedPreferenceItemConfig;

/**
 * Created by ningbaoqi on 18-4-21.
 */

public class SettingsActivity extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);
        final SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferenceItemConfig.SharedPreferenceFileName, MODE_PRIVATE);
        final SettingItemView sivUpdate = (SettingItemView) findViewById(R.id.sivupdate);
        boolean isAuto = sharedPreferences.getBoolean(SharedPreferenceItemConfig.SharedPreferenceAutoUpdate , true);
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
}
