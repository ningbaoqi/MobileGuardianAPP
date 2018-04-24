package ningbaoqi.com.rocket;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!Settings.canDrawOverlays(this)) {
            getGrant();
        }
    }

    public void startRocket(View view) {
        startService(new Intent(this, RocketService.class));
        finish();
    }

    public void stopRocket(View view) {
        stopService(new Intent(this, RocketService.class));
    }
    /**
     * 动态获取浮动框权限
     * */
    private void getGrant(){
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent,100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Settings.canDrawOverlays(this)) {
            Toast.makeText(this , "已经过去浮动框权限", Toast.LENGTH_LONG).show();
        }
    }
}
