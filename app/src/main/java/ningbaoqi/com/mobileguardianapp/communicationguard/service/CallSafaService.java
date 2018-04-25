package ningbaoqi.com.mobileguardianapp.communicationguard.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * =========================================
 * <p/>
 * 版    权: ningxiansheng
 * <p/>
 * 作    者: 宁宝琪
 * <p/>
 * 版    本:1.0
 * <p/>
 * 创建 时间:18-4-25
 * <p/>
 * 描    述: 由于版本修改已经不能拦截广播，所以不实现该功能
 * <p/>
 * <p/>
 * 修订 历史:
 * <p/>
 * =========================================
 */

public class CallSafaService extends Service{
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
