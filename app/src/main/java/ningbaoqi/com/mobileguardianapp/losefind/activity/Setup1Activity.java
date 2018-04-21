package ningbaoqi.com.mobileguardianapp.losefind.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import ningbaoqi.com.mobileguardianapp.R;

/**
 * Created by ningbaoqi on 18-4-21.
 * 第一个手机向导页面
 */

public class Setup1Activity extends BaseSetupActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setup1_layout);
    }

    @Override
    protected void showPreviousPage() {

    }

    @Override
    protected void showNextPage() {
        startActivity(new Intent(Setup1Activity.this, Setup2Activity.class));
        finish();
        /**
         * 两个界面之间的切换动画
         * */
        overridePendingTransition(R.anim.tranlate_in, R.anim.tranlate_out);
    }
}
