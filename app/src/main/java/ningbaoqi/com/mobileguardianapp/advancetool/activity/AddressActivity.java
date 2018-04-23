package ningbaoqi.com.mobileguardianapp.advancetool.activity;

import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import ningbaoqi.com.mobileguardianapp.R;
import ningbaoqi.com.mobileguardianapp.utils.LocationNumberSearchDatabase;

/**
 * Created by ningbaoqi on 18-4-23.
 * 归属地查询页面
 */

public class AddressActivity extends AppCompatActivity {

    private TextView tvResult;
    private EditText etSearchNumber;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.address_layout);
        etSearchNumber = (EditText) findViewById(R.id.et_searchnumber);
        tvResult = (TextView) findViewById(R.id.tv_result);
        /**
         * 监听EditText的变化
         * */
        etSearchNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String location = LocationNumberSearchDatabase.getAddress(s.toString());
                tvResult.setText(location);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 开始查询
     */
    public void query(View view) {
        String number = etSearchNumber.getText().toString().trim();
        if (!TextUtils.isEmpty(number)) {
            String location = LocationNumberSearchDatabase.getAddress(number);
            tvResult.setText(location);
        } else {
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
//            /**
//             * 插补器远离
//             */
//            shake.setInterpolator(new Interpolator() {
//                /**
//                 * x表示时间轴，y表示距离轴，根据时间变化。返回距离
//                 * */
//                @Override
//                public float getInterpolation(float x) {
//                    int y = 0;
//                    return y;
//                }
//            });
            etSearchNumber.startAnimation(shake);
            vibrate();
        }
    }

    /**
     * 手机震动
     * */
    public void vibrate(){
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
//        vibrator.vibrate(500);
        vibrator.vibrate(new long[]{1000 , 2000, 1000 , 3000} , -1);
    }
}
