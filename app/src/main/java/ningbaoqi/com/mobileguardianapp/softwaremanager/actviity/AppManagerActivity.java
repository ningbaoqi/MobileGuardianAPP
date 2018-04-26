package ningbaoqi.com.mobileguardianapp.softwaremanager.actviity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
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

public class AppManagerActivity extends AppCompatActivity implements View.OnClickListener{
    @ViewInject(R.id.list_view)
    private ListView listView;
    @ViewInject(R.id.tv_rom)
    private TextView tv_rom;
    @ViewInject(R.id.tv_sdcard)
    private TextView tv_sdcard;
    @ViewInject(R.id.tv_app)
    private TextView tv_app;
    private List<AppInfo> appInfos;
    private List<AppInfo> userAppInfos;//用户程序集合
    private List<AppInfo> systemAppInfos;//系统程序集合
    private PopupWindow popupWindow;
    private AppInfo clickAppInfo;

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
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                Log.d("nbq", firstVisibleItem + "---" + visibleItemCount + "--------" + totalItemCount);
                popwindowDismiss();
                if (userAppInfos != null && systemAppInfos != null) {
                    if (firstVisibleItem > (userAppInfos.size() + 1)) {
                        tv_app.setText(getResources().getString(R.string.system_application) + "(" + systemAppInfos.size() + ")个");
                    } else {
                        tv_app.setText(getResources().getString(R.string.user_application) + "(" + userAppInfos.size() + ")个");
                    }
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /**
                 * 获取当前点击的对象
                 * */
                Object itemAtPosition = listView.getItemAtPosition(position);
                if (itemAtPosition != null && itemAtPosition instanceof AppInfo) {
                    clickAppInfo = (AppInfo) itemAtPosition;
                    View convertview = View.inflate(AppManagerActivity.this, R.layout.popup_window, null);
                    LinearLayout ll_uninstall = convertview.findViewById(R.id.ll_uninstall);
                    LinearLayout ll_running = convertview.findViewById(R.id.ll_running);
                    LinearLayout ll_share = convertview.findViewById(R.id.ll_share);
                    LinearLayout ll_details = convertview.findViewById(R.id.ll_details);
                    ll_uninstall.setOnClickListener(AppManagerActivity.this);
                    ll_share.setOnClickListener(AppManagerActivity.this);
                    ll_running.setOnClickListener(AppManagerActivity.this);
                    ll_details.setOnClickListener(AppManagerActivity.this);
                    popupWindow = new PopupWindow(convertview, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                    popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    int[] location = new int[2];
                    view.getLocationInWindow(location);//获取当前view展示在窗体上面的位置
                    popupWindow.showAtLocation(parent, Gravity.LEFT + Gravity.TOP, 200, location[1]);
                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.5f, 1.0f, 0.5f, 1.0f, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0.5f);
                    scaleAnimation.setDuration(300);
                    AlphaAnimation alphaAnimation = new AlphaAnimation(0.5f, 1.0f);
                    alphaAnimation.setDuration(300);
                    AnimationSet animationSet = new AnimationSet(true);
                    animationSet.addAnimation(alphaAnimation);
                    animationSet.addAnimation(scaleAnimation);
                    convertview.startAnimation(animationSet);
                }
            }
        });
    }

    private void popwindowDismiss(){
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            popupWindow = null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_running:
                Intent intent = this.getPackageManager().getLaunchIntentForPackage(clickAppInfo.getApkPackageName());
                this.startActivity(intent);
                this.popwindowDismiss();
                break;
            case R.id.ll_share:
                Intent shareIntent = new Intent("android.intent.action.SEND");
                shareIntent.setType("text/plain");
                shareIntent.putExtra("android.intent.extra.SUBJECT", "f分享");
                shareIntent.putExtra("android.intent.extra.TEXT", "Hi 推荐您一个软件:" + clickAppInfo.getApkName() + "下载地址：" + "https://play.google.com/store/apps/details?id=" + clickAppInfo.getApkPackageName());
                this.startActivity(Intent.createChooser(shareIntent, "分享"));
                this.popwindowDismiss();
                break;
            case R.id.ll_uninstall:
                break;
//            case R.id.ll_details:
//                Intent detailsIntent = new Intent();
//                detailsIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
//                detailsIntent.addCategory(Intent.CATEGORY_DEFAULT);
//
//                this.startActivity(detailsIntent);
//                break;
        }
    }

    private class AppManagerAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return userAppInfos.size() + 1 + systemAppInfos.size() + 1;
        }

        @Override
        public Object getItem(int position) {
            if (position == 0) {
                return null;
            } else if (position == userAppInfos.size() + 1) {
                return null;
            }
            AppInfo appInfo;
            if (position < userAppInfos.size() + 1) {
                appInfo = userAppInfos.get(position + 1);
            } else {
                appInfo = systemAppInfos.get(position - userAppInfos.size() - 2);
            }
            return appInfo;
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
            } else {
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
            } else {
                viewHolder.tvLocation.setText(getResources().getString(R.string.external));
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        popwindowDismiss();
    }

    /**
     * 通过反射，需要将aidl文件拷贝过来
     * */
//    private void endCall(){
//        try {
//            //通过类加载器加载ServiceManager
//            Class<?> clazz = getClassLoader().loadClass("android.os.ServiceManager");
//            //通过反射得到当前的方法
//            Method method = clazz.getDeclaredMethod("getService", String.class);
//            IBinder iBinder = (IBinder) method.invoke(null, TELEPHONY_SERVICE);
//            ITelephony iTelephont = ITelephony.Stub.asInterface(iBinder);
//            iTelephont.endCall();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//
//    }
}
