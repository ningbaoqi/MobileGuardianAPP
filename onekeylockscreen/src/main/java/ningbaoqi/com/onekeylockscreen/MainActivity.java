package ningbaoqi.com.onekeylockscreen;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private DevicePolicyManager devicePolicyManager;
    private ComponentName componentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        devicePolicyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        componentName = new ComponentName(this, AdminReceiver.class);//设备管理组件
//        devicePolicyManager.lockNow();
//        finish();
    }

    /**
     * 锁屏
     */
    public void lockScreen(View view) {
        if (devicePolicyManager.isAdminActive(componentName)) {
            devicePolicyManager.lockNow();//锁屏
            devicePolicyManager.resetPassword("111111", 0);//设置开机密码
        } else {
            Toast.makeText(this, "必须激活设备管理器", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 注册设备管理器
     */
    public void registerDeviceManager(View view) {
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "haha^_^haha");
        startActivity(intent);
    }

    /**
     * 卸载程序，卸载程序因为版本改动请参考安装程序步骤
     */
    public void uninstall(View view) {
        devicePolicyManager.removeActiveAdmin(componentName);//取消激活
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.ACTION_DEFAULT);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

    /**
     * 清除数据
     */
    public void wipeData(View view) {
        if (devicePolicyManager.isAdminActive(componentName)) {
            devicePolicyManager.wipeData(0);
        } else {
            Toast.makeText(this, "必须激活设备管理器", Toast.LENGTH_LONG).show();
        }
    }
}
