package ningbaoqi.com.mobileguardianapp.softwaremanager.actviity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import ningbaoqi.com.mobileguardianapp.R;
import ningbaoqi.com.mobileguardianapp.softwaremanager.bean.AppInfo;
import ningbaoqi.com.mobileguardianapp.softwaremanager.engine.AppInfos;

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
 * 描    述: app管理器
 * <p/>
 * <p/>
 * 修订 历史:
 * <p/>
 * =========================================
 */

public class AppManagerActivity extends AppCompatActivity {
    @ViewInject(R.id.list_view)
    private ListView listView;
    @ViewInject(R.id.tv_rom)
    private TextView tv_rom;
    @ViewInject(R.id.tv_sdcard)
    private TextView tv_sdcard;
    private List<AppInfo> appInfos;
    private List<AppInfo> userAppInfos;//用户程序集合
    private List<AppInfo> systemAppInfos;//系统程序集合

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        initData();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            AppManagerAdapter appManagerAdapter = new AppManagerAdapter();
            listView.setAdapter(appManagerAdapter);
        }
    };

    private void initData() {
        new Thread() {
            @Override
            public void run() {
                appInfos = AppInfos.getAppInfos(AppManagerActivity.this);//获取到所有安装到手机上的应用程序
                userAppInfos = new ArrayList<>();
                systemAppInfos = new ArrayList<>();
                for (AppInfo info : appInfos) {
                    if (info.isUserApp()) {
                        userAppInfos.add(info);
                    } else {
                        systemAppInfos.add(info);
                    }
                }
                handler.sendEmptyMessage(0);
            }
        }.start();
    }

    /**
     * 初始化UI
     */
    private void initUI() {
        setContentView(R.layout.software_manager_layout);
//        ListView listView = (ListView) findViewById(R.id.list_view);
        ViewUtils.inject(this);
        long rom_space = Environment.getDataDirectory().getFreeSpace();//获取到ROM内存中的运行的剩余空间
        long sdcard_space = Environment.getExternalStorageDirectory().getFreeSpace();//获取SDcard的剩余空间
        tv_rom.setText(getResources().getString(R.string.ram_enable) + Formatter.formatFileSize(this, rom_space));
        tv_sdcard.setText(getResources().getString(R.string.sdcard_enable) + Formatter.formatFileSize(this, sdcard_space));
    }

    private class AppManagerAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return userAppInfos.size() + 1 + systemAppInfos.size() + 1;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            /**
             * 表示应用程序
             * */
            if (position == 0) {
                TextView textView = new TextView(AppManagerActivity.this);
                textView.setText(getResources().getString(R.string.user_application) + "(" + userAppInfos.size() + ")");
                textView.setTextColor(Color.WHITE);
                textView.setTextSize(getResources().getDimension(R.dimen.dimen_5sp));
                textView.setBackgroundColor(Color.BLACK);
                return textView;
            } else if (position == userAppInfos.size() + 1) {
                TextView textView = new TextView(AppManagerActivity.this);
                textView.setText(getResources().getString(R.string.system_application) + "(" + userAppInfos.size() + ")");
                textView.setTextColor(Color.WHITE);
                textView.setTextSize(getResources().getDimension(R.dimen.dimen_5sp));
                textView.setBackgroundColor(Color.BLACK);
                return textView;
            }
            AppInfo appInfo;
            if (position < userAppInfos.size() + 1) {
                appInfo = userAppInfos.get(position - 1);
            } else {
                appInfo = systemAppInfos.get(position - userAppInfos.size() - 2);
            }

            ViewHolder viewHolder;
            if (convertView != null && convertView instanceof LinearLayout) {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            {
                convertView = View.inflate(AppManagerActivity.this, R.layout.itenm_app_manager, null);
                viewHolder = new ViewHolder();
                viewHolder.ivIcon = convertView.findViewById(R.id.iv_icon);
                viewHolder.tvAppSize = convertView.findViewById(R.id.tv_app_size);
                viewHolder.tvLocation = convertView.findViewById(R.id.tv_location);
                viewHolder.tvName = convertView.findViewById(R.id.tv_name);
                convertView.setTag(viewHolder);
            }
            viewHolder.ivIcon.setBackground(appInfo.getIcon());
            viewHolder.tvAppSize.setText(Formatter.formatFileSize(AppManagerActivity.this, appInfo.getApkSize()));
            viewHolder.tvName.setText(appInfo.getApkName());
            if (appInfo.isRom()) {
                viewHolder.tvLocation.setText(getResources().getString(R.string.phone_rom));
            }
            return convertView;
        }
    }

    static class ViewHolder {
        ImageView ivIcon;
        TextView tvAppSize;
        TextView tvLocation;
        TextView tvName;
    }
}
