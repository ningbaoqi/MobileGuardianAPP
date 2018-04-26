package ningbaoqi.com.looperdemo;

import android.os.Bundle;
import android.os.Looper;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("nbq" , String.valueOf(Process.myTid()));
        setContentView(R.layout.activity_main);
        new Thread(){
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(MainActivity.this , "sub thread" + this.getId() , Toast.LENGTH_LONG).show();//可以在子线程显示Toast
                Looper.loop();
            }
        }.start();
    }
}
