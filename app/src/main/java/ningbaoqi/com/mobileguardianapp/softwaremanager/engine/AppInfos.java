package ningbaoqi.com.mobileguardianapp.softwaremanager.engine;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ningbaoqi.com.mobileguardianapp.softwaremanager.bean.AppInfo;

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
 * 描    述: 业务逻辑类 获取安装包信息
 * <p/>
 * <p/>
 * 修订 历史:
 * <p/>
 * =========================================
 */

public class AppInfos {

    public static List<AppInfo> getAppInfos(Context context) {
        List<AppInfo> packageAppInfos = new ArrayList<>();
        PackageManager packageManager = context.getPackageManager();
        /**
         * 获取安装包信息
         * */
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        for (PackageInfo info : packageInfos) {
            AppInfo appInfo = new AppInfo();
            Drawable icon = info.applicationInfo.loadIcon(packageManager);//获取apk的图标
            appInfo.setIcon(icon);
            String label = (String) info.applicationInfo.loadLabel(packageManager);//获取apk的标题
            appInfo.setApkName(label);
            String packageName = info.packageName;//获取apk文件的包名
            appInfo.setApkPackageName(packageName);
            String sourceDie = info.applicationInfo.sourceDir;//获取到apk资源的路径
            File file = new File(sourceDie);
            long length = file.length();//获取apk的长度
            appInfo.setApkSize(length);
            Log.d("nbq", "------------------");
            Log.d("nbq", label);
            Log.d("nbq", packageName);
            Log.d("nbq", length + "");

            int flags = info.applicationInfo.flags;//获取到安装应用程序的标记
            if ((flags & ApplicationInfo.FLAG_SYSTEM) != 0) {//表示系统app
                appInfo.setUserApp(false);
            } else {//表示用户app
                appInfo.setUserApp(true);
            }
            if ((flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) != 0) {//表示在sdcard
                appInfo.setRom(false);
            } else {//表示手机内存
                appInfo.setRom(true);
            }
            packageAppInfos.add(appInfo);
        }
        return packageAppInfos;
    }
}

