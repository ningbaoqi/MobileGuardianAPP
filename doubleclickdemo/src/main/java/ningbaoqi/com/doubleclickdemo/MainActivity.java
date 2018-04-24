package ningbaoqi.com.doubleclickdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private long firstClickTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void clickme(View view) {
        if (firstClickTime > 0) {
            if (System.currentTimeMillis() - firstClickTime < 500) {
                Toast.makeText(this, "双击了", Toast.LENGTH_SHORT).show();
                firstClickTime = 0;
                return;
            }
        }
        firstClickTime = System.currentTimeMillis();
    }
}
