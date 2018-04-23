package ningbaoqi.com.mobileguardianapp.splash.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import ningbaoqi.com.mobileguardianapp.R;
import ningbaoqi.com.mobileguardianapp.home.activity.HomeActivity;
import ningbaoqi.com.mobileguardianapp.utils.SharedPreferenceItemConfig;
import ningbaoqi.com.mobileguardianapp.utils.StreamUtils;

/**
 * Created by ningbaoqi on 18-4-20.
 * 导航页面：可以展示公司logo，项目初始化，检测版本更新，授权等功能
 */

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "nbq";
    private static final int CODE_UPDATE_DIALOG = 0;
    private static final int CODE_URL_ERROR = 1;
    private static final int CODE_NET_ERROR = 2;
    private static final int CODE_JSON_ERROR = 3;
    private static final int CODE_ENTER_HOME = 4;
    private static final String VersionPath = "http://192.168.1.32:8080/my.json";
    private RelativeLayout splash_root;
    private TextView loadProgress;
    private String mVersionName;//版本名
    private int mVersionCode;//版本号
    private String mDescription;//版本描述
    private String mDownloadUrl;//下载地址

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CODE_UPDATE_DIALOG:
                    showUpdateDialog();
                    break;
                case CODE_URL_ERROR:
                    Toast.makeText(SplashActivity.this, "url错误", Toast.LENGTH_LONG).show();
                    enterHome();
                    break;
                case CODE_NET_ERROR:
                    Toast.makeText(SplashActivity.this, "网络错误", Toast.LENGTH_LONG).show();
                    enterHome();
                    break;
                case CODE_JSON_ERROR:
                    Toast.makeText(SplashActivity.this, "数据解析错误", Toast.LENGTH_LONG).show();
                    enterHome();
                    break;
                case CODE_ENTER_HOME:
                    enterHome();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);
        splash_root = (RelativeLayout) findViewById(R.id.splash_root);
        TextView splashAppVersion = (TextView) findViewById(R.id.splash_app_version);
        splashAppVersion.setText(getResources().getString(R.string.version) + getVersionName());
        loadProgress = (TextView) findViewById(R.id.load_progress);
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferenceItemConfig.SharedPreferenceFileName, MODE_PRIVATE);
        copyDatabase("address.db");
        boolean autoUpdate = sharedPreferences.getBoolean(SharedPreferenceItemConfig.SharedPreferenceAutoUpdate, true);
        if (autoUpdate) {
            checkVersion();
        } else {
            Message message = Message.obtain();
            message.what = CODE_ENTER_HOME;
            handler.sendMessageDelayed(message, 2000);
        }
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.3f, 1f);
        alphaAnimation.setDuration(2000);
        splash_root.startAnimation(alphaAnimation);
    }

    /**
     * 获取版本名
     */
    private String getVersionName() {
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo info = packageManager.getPackageInfo(getPackageName(), 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 从服务器获取版本信息
     */
    private void checkVersion() {
        new Thread() {
            @Override
            public void run() {
                Message message = Message.obtain();
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(VersionPath);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);
                    connection.connect();
                    Log.d(TAG, connection.getResponseCode() + "");
                    if (connection.getResponseCode() == 200) {
                        InputStream inputStream = connection.getInputStream();
                        String result = StreamUtils.readFromStream(inputStream);
                        Log.d(TAG, result);
                        /**
                         * 解析json
                         * */
                        JSONObject jsonObject = new JSONObject(result);
                        mVersionName = jsonObject.getString("versionName");
                        mVersionCode = jsonObject.getInt("versionCode");
                        mDescription = jsonObject.getString("description");
                        mDownloadUrl = jsonObject.getString("downloadUrl");
                        Log.d(TAG, "mVersionName:" + mVersionName + ";mVersionCode:" + mVersionCode + "mDescription:" + mDescription + ";mDownloadUrl:" + mDownloadUrl);
                        /**
                         * 判断是否有更新
                         * */
                        if (mVersionCode > getVersionCode()) {
                            message.what = CODE_UPDATE_DIALOG;
                        } else {
                            message.what = CODE_ENTER_HOME;
                        }
                    }
                } catch (MalformedURLException e) {
                    message.what = CODE_URL_ERROR;
                } catch (IOException e) {
                    message.what = CODE_NET_ERROR;
                } catch (JSONException e) {
                    message.what = CODE_JSON_ERROR;
                } finally {
                    handler.sendMessageDelayed(message, 2000);
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }.start();
    }

    /**
     * 获取版本号
     */
    private int getVersionCode() {
        PackageManager manager = getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 弹出升级对话框
     */
    private void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("最新版本：" + mVersionName).setMessage(mDescription).setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                downLoad();
            }
        }).setNegativeButton("以后再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                enterHome();
            }
        });
//        builder.setCancelable(false);//用户点击返回键将无效
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            /**
             * 用户点击返回键时会触发
             * */
            @Override
            public void onCancel(DialogInterface dialog) {
                enterHome();
            }
        });
        builder.show();
    }

    /**
     * 进入主界面
     */
    private void enterHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 下载apk文件
     */
    private void downLoad() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            loadProgress.setVisibility(View.VISIBLE);
            String target = Environment.getExternalStorageDirectory() + "/update.apk";
            Log.d(TAG, Environment.getExternalStorageDirectory() + "");
            HttpUtils httpUtils = new HttpUtils();
            httpUtils.download(/*mDownloadUrl*/"http://192.168.1.32:8080/com.ijinshan.kbatterydoctor.apk", target, new RequestCallBack<File>() {
                /**
                 * 下载成功，下载成功之后，要跳转到系统的安装页面
                 */
                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {
                    loadProgress.setVisibility(View.GONE);
                    Toast.makeText(SplashActivity.this, "下载成功", Toast.LENGTH_LONG).show();
                    File apkFile = new File(Environment.getExternalStorageDirectory() + "/update.apk");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        String providerName = "ningbaoqi.com.mobileguardianapp.fileprovider";
                        Uri apkUri = FileProvider.getUriForFile(SplashActivity.this, providerName, apkFile);
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
                        /**
                         * 取消安装时，会返回结果
                         * */
                        startActivityForResult(intent, 0);
                    } else {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
                        /**
                         * 取消安装时，会返回结果
                         * */
                        startActivityForResult(intent, 0);
                    }
                }

                /**
                 * 下载失败
                 */
                @Override
                public void onFailure(HttpException e, String s) {
                    Toast.makeText(SplashActivity.this, "下载失败", Toast.LENGTH_LONG).show();
                    enterHome();
                }

                /**
                 * 表示文件的下载进度
                 */
                @Override
                public void onLoading(long total, long current, boolean isUploading) {
                    super.onLoading(total, current, isUploading);
                    loadProgress.setText(getResources().getString(R.string.load_progress) + (current * 100 / total) + "%");
                }
            });
        } else {
            Toast.makeText(SplashActivity.this, "没有SD卡", Toast.LENGTH_LONG).show();
            enterHome();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            enterHome();
        }
    }

    /**
     * 拷贝查询数据库
     */
    private void copyDatabase(String databaseName) {
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            /**
             * 获取原始文件输入流
             * */
            inputStream = getAssets().open(databaseName);
            File file = new File(getFilesDir(), databaseName);
            if (file.exists()) {
                return;
            }
            outputStream = new FileOutputStream(file);
            int length;
            byte[] buffer = new byte[1024];
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
