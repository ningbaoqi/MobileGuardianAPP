package ningbaoqi.com.mobileguardianapp.communicationguard.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import ningbaoqi.com.mobileguardianapp.R;

/**
 * Created by ningbaoqi on 18-4-24.
 */

public class ComminicationGuard extends AppCompatActivity {

    private ListView listView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.communication_layout);
        initUi();
    }

    private void initUi() {
        listView = (ListView) findViewById(R.id.list_view);
    }
}
