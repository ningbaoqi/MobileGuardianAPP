package ningbaoqi.com.mobileguardianapp.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by ningbaoqi on 18-4-23.
 * 服务状态工具类
 */

public class ServiceStatusUtils {
    /**
     * 检测服务是否正在运行
     */
    public static final boolean isServiceRunning(Context context , String service) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> list = activityManager.getRunningServices(100);
        for (ActivityManager.RunningServiceInfo info : list) {
            String serviceName = info.service.getClassName();
            Log.d("nbq", "runing service:---------" + serviceName);
            if (serviceName.equals(service)) {
                return true;
            }
        }
        return false;
    }
}
