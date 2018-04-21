package ningbaoqi.com.mobileguardianapp.losefind.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import ningbaoqi.com.mobileguardianapp.R;
import ningbaoqi.com.mobileguardianapp.utils.SharedPreferenceItemConfig;

/**
 * Created by ningbaoqi on 18-4-21.
 * 手机防盗
 */

public class LosedFindActivity extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferenceItemConfig.SharedPreferenceFileName, MODE_PRIVATE);
        boolean configed = sharedPreferences.getBoolean(SharedPreferenceItemConfig.SharedPreferenceConfiged, false);
        if (configed) {
            setContentView(R.layout.losed_find_layout);
            TextView safeNumber = (TextView) findViewById(R.id.textview2);
            safeNumber.setText(sharedPreferences.getString(SharedPreferenceItemConfig.SharedPreferenceSafeNumber, null));
            ImageView lock = (ImageView) findViewById(R.id.lock);
            boolean locked = sharedPreferences.getBoolean(SharedPreferenceItemConfig.SharedPreferenceProtected, false);
            if (locked) {
                lock.setImageResource(R.mipmap.lock);
            } else {
                lock.setImageResource(R.mipmap.unlock);
            }
        } else {
            /**
             * 第一次进入即没有配置过将会跳转到手机向导页面
             * */
            startActivity(new Intent(LosedFindActivity.this, Setup1Activity.class));
            finish();
        }
    }

    /***
     * 重新进入手机向导页面
     */
    public void reEnter(View view) {
        startActivity(new Intent(LosedFindActivity.this, Setup1Activity.class));
        finish();
    }
}
