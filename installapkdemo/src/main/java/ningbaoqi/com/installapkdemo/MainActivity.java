package ningbaoqi.com.installapkdemo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void install(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        File file  = new File(Environment.getExternalStorageDirectory() + "/update.apk");
        Uri apkUri = FileProvider.getUriForFile(this , "ningbaoqi.com.installapkdemo.fileprovider" , file);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        /**
         * 取消安装时，会返回结果
         * */
        startActivityForResult(intent, 0);
    }
}
