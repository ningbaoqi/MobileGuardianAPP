package ningbaoqi.com.mobileguardianapp.advancetool.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import ningbaoqi.com.mobileguardianapp.R;

/**
 * Created by ningbaoqi on 18-4-23.
 * 高级工具
 */

public class AdvanceToolsActivity extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advancetools_layout);
    }

    /**
     * 电话号码归属地查询
     *        数据库优化：是为了减少重复字段经过多张表的映射关系，将数据冗余降到最低
     * */
    public void numberAddressQuery(View view) {
        startActivity(new Intent(AdvanceToolsActivity.this , AddressActivity.class));
    }
}
