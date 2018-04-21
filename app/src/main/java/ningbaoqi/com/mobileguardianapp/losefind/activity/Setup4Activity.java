package ningbaoqi.com.mobileguardianapp.losefind.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import ningbaoqi.com.mobileguardianapp.R;
import ningbaoqi.com.mobileguardianapp.utils.SharedPreferenceItemConfig;

/**
 * Created by ningbaoqi on 18-4-21.
 */

public class Setup4Activity extends BaseSetupActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setup4_layout);
        final CheckBox cb_protect = (CheckBox) findViewById(R.id.cb_protect);
        boolean protect = sharedPreferences.getBoolean(SharedPreferenceItemConfig.SharedPreferenceProtected , false);
        if (protect) {
            cb_protect.setText(getResources().getString(R.string.setup4_content_1));
        } else {
            cb_protect.setText(getResources().getString(R.string.setup4_content_2));
        }
        cb_protect.setChecked(protect);
        cb_protect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cb_protect.setText(getResources().getString(R.string.setup4_content_1));
                    sharedPreferences.edit().putBoolean(SharedPreferenceItemConfig.SharedPreferenceProtected, true).commit();
                } else {
                    cb_protect.setText(getResources().getString(R.string.setup4_content_2));
                    sharedPreferences.edit().putBoolean(SharedPreferenceItemConfig.SharedPreferenceProtected, false).commit();
                }
            }
        });
    }

    @Override
    protected void showPreviousPage() {
        startActivity(new Intent(Setup4Activity.this, Setup3Activity.class));
        finish();
        overridePendingTransition(R.anim.traslate_previous_in , R.anim.translate_previous_out);
    }

    @Override
    protected void showNextPage() {
        startActivity(new Intent(Setup4Activity.this, LosedFindActivity.class));
        sharedPreferences.edit().putBoolean(SharedPreferenceItemConfig.SharedPreferenceConfiged, true).commit();
        finish();
    }
}
