package ningbaoqi.com.mobileguardianapp.settings.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import ningbaoqi.com.mobileguardianapp.R;
import ningbaoqi.com.mobileguardianapp.advancetool.service.AddressService;
import ningbaoqi.com.mobileguardianapp.communicationguard.service.CallSafaService;
import ningbaoqi.com.mobileguardianapp.settings.view.SettingItemClick;
import ningbaoqi.com.mobileguardianapp.settings.view.SettingItemView;
import ningbaoqi.com.mobileguardianapp.utils.ServiceStatusUtils;
import ningbaoqi.com.mobileguardianapp.utils.SharedPreferenceItemConfig;

/**
 * Created by ningbaoqi on 18-4-21.
 */

public class SettingsActivity extends AppCompatActivity {

    private SettingItemView sivAddress;
    private SettingItemView sivUpdate;
    private SettingItemView sivBlackNumber;
    private SettingItemClick scvAddressStyle;
    private SettingItemClick scvAddressLocation;
    private SharedPreferences sharedPreferences;
    private String[] items;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);
        sharedPreferences = getSharedPreferences(SharedPreferenceItemConfig.SharedPreferenceFileName, MODE_PRIVATE);
        initUpdate();
        initAddress();
        initAddressStyle();
        initAddressLocation();
        if (!Settings.canDrawOverlays(this)) {
            getGrant();
        }
        initBlackNumberUI();
    }

    /**
     * 初始化黑名单设置
     */
    private void initBlackNumberUI() {
        sivBlackNumber = (SettingItemView) findViewById(R.id.siv_blackNumber);
        boolean isRunning = ServiceStatusUtils.isServiceRunning(this, "ningbaoqi.com.mobileguardianapp.communicationguard.service.CallSafaService");
        if (isRunning) {
            sivBlackNumber.setChecked(true);
        } else {
            sivBlackNumber.setChecked(false);
        }
        sivBlackNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sivBlackNumber.isChecked()) {
                    sivBlackNumber.setChecked(false);
                    stopService(new Intent(SettingsActivity.this, CallSafaService.class));
                } else {
                    sivBlackNumber.setChecked(true);
                    startService(new Intent(SettingsActivity.this, CallSafaService.class));
                }
            }
        });
    }

    /**
     * 初始化自动升级逻辑
     */
    private void initUpdate() {
        sivUpdate = (SettingItemView) findViewById(R.id.sivupdate);
        boolean isAuto = sharedPreferences.getBoolean(SharedPreferenceItemConfig.SharedPreferenceAutoUpdate, true);
        sivUpdate.setChecked(isAuto);
        sivUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sivUpdate.isChecked()) {
                    sivUpdate.setChecked(false);
                    sharedPreferences.edit().putBoolean(SharedPreferenceItemConfig.SharedPreferenceAutoUpdate, false).commit();
                } else {
                    sivUpdate.setChecked(true);
                    sharedPreferences.edit().putBoolean(SharedPreferenceItemConfig.SharedPreferenceAutoUpdate, true).commit();
                }
            }
        });
    }

    /**
     * 初始化归属地逻辑
     */
    private void initAddress() {
        sivAddress = (SettingItemView) findViewById(R.id.sivAddress);
        boolean isRunning = ServiceStatusUtils.isServiceRunning(this, "ningbaoqi.com.mobileguardianapp.advancetool.service.AddressService");
        if (isRunning) {
            sivAddress.setChecked(true);
        } else {
            sivAddress.setChecked(false);
        }
        sivAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sivAddress.isChecked()) {
                    sivAddress.setChecked(false);
                    stopService(new Intent(SettingsActivity.this, AddressService.class));
                } else {
                    sivAddress.setChecked(true);
                    startService(new Intent(SettingsActivity.this, AddressService.class));
                }
            }
        });
    }

    /**
     * 修改提示框的显示风格
     * */
    private void initAddressStyle() {
        items = getResources().getStringArray(R.array.address_single_choose_array);
        scvAddressStyle = (SettingItemClick) findViewById(R.id.scv_address_style);
        scvAddressStyle.setSettingTitle(getResources().getString(R.string.addressShowStyle));
        int position = sharedPreferences.getInt(SharedPreferenceItemConfig.SharedPreferenceAddressStyle , 0);
        scvAddressStyle.setSettingDesc(items[position]);
        scvAddressStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSingleChooseDialog();
            }
        });
    }

    /**
     * 弹出选择风格的单选框
     * */
    private void showSingleChooseDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        int position = sharedPreferences.getInt(SharedPreferenceItemConfig.SharedPreferenceAddressStyle , 0);
        builder.setIcon(R.mipmap.ic_launcher).setTitle(getResources().getString(R.string.address_dialog_title));
        builder.setSingleChoiceItems(items, position, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sharedPreferences.edit().putInt(SharedPreferenceItemConfig.SharedPreferenceAddressStyle, which).commit();
                dialog.dismiss();
                scvAddressStyle.setSettingDesc(items[which]);
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.cancel), null);
        builder.show();
    }

    /**
     * 初始化归属地位置
     * */
    private void initAddressLocation() {
        scvAddressLocation = (SettingItemClick) findViewById(R.id.scv_address_location);
        scvAddressLocation.setSettingTitle(getResources().getString(R.string.address_location));
        scvAddressLocation.setSettingDesc(getResources().getString(R.string.set_address_location));
        scvAddressLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this , DragViewActivity.class));
            }
        });
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
