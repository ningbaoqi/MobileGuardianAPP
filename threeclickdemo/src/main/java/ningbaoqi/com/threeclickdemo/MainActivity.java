package ningbaoqi.com.threeclickdemo;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    long[] mHints = new long[2];
    public void click(View view) {
        System.arraycopy(mHints , 1 , mHints , 0 , mHints.length -1);
        mHints[mHints.length - 1] = SystemClock.uptimeMillis();
        if (mHints[0] >= (SystemClock.uptimeMillis() - 500)) {
            Toast.makeText(this , "多次点击" , Toast.LENGTH_LONG).show();
        }
    }
}
