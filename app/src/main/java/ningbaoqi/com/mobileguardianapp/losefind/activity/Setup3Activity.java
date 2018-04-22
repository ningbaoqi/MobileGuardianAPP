package ningbaoqi.com.mobileguardianapp.losefind.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import ningbaoqi.com.mobileguardianapp.R;
import ningbaoqi.com.mobileguardianapp.utils.SharedPreferenceItemConfig;

/**
 * Created by ningbaoqi on 18-4-21.
 * 第三个手机向导页
 */

public class Setup3Activity extends BaseSetupActivity {

    private EditText et_phone;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setup3_layout);
        et_phone = (EditText) findViewById(R.id.et_phone);
        String number = sharedPreferences.getString(SharedPreferenceItemConfig.SharedPreferenceSafeNumber, "");
        et_phone.setText(number);
    }

    @Override
    protected void showPreviousPage() {
        startActivity(new Intent(Setup3Activity.this, Setup2Activity.class));
        finish();
        overridePendingTransition(R.anim.traslate_previous_in, R.anim.translate_previous_out);
    }

    @Override
    protected void showNextPage() {
        String number = et_phone.getText().toString().trim();
        if (TextUtils.isEmpty(number)) {
            Toast.makeText(this, "安全号码不能为空", Toast.LENGTH_LONG).show();
            return;
        }
        sharedPreferences.edit().putString(SharedPreferenceItemConfig.SharedPreferenceSafeNumber, number).commit();
        startActivity(new Intent(Setup3Activity.this, Setup4Activity.class));
        finish();
        overridePendingTransition(R.anim.tranlate_in, R.anim.tranlate_out);
    }

    public void chooseContact(View view) {
        Intent intent = new Intent(this, ContactActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            String phone = data.getStringExtra("phone");
            phone = phone.replaceAll("-", "").replaceAll(" ", "");
            et_phone.setText(phone);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
