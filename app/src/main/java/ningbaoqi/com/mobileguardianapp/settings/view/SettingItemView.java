package ningbaoqi.com.mobileguardianapp.settings.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ningbaoqi.com.mobileguardianapp.R;

/**
 * Created by ningbaoqi on 18-4-21.
 */

public class SettingItemView extends RelativeLayout {
    private String mTitle;
    private String mDescOn;
    private String mDescOff;
    private TextView settingTitle;
    private TextView settingDesc;
    private CheckBox checkBox;

    public SettingItemView(Context context) {
        super(context);
        initView();
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        /**
         * 根据属性名称，获取自定义属性的值
         * */
        TypedArray typedArray = context.obtainStyledAttributes(attrs , R.styleable.SettingItemView);
        mTitle = typedArray.getString(R.styleable.SettingItemView_title);
        mDescOn = typedArray.getString(R.styleable.SettingItemView_desc_on);
        mDescOff = typedArray.getString(R.styleable.SettingItemView_desc_off);
        typedArray.recycle();
        initView();
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        /**
         * 解析setting_item布局，并且将该布局设置父布局
         * */
        View.inflate(getContext(), R.layout.settings_item, this);
        settingTitle = findViewById(R.id.setting_title);
        settingDesc = findViewById(R.id.setting_desc);
        checkBox = findViewById(R.id.cb_status);
        setSettingTitle(mTitle);
    }

    public void setSettingTitle(String title) {
        settingTitle.setText(title);
    }

    public void setSettingDesc(String desc) {
        settingDesc.setText(desc);
    }

    public boolean isChecked(){
        return checkBox.isChecked();
    }

    public void setChecked(boolean checked) {
        checkBox.setChecked(checked);
        if (checked) {
            setSettingDesc(mDescOn);
        } else {
            setSettingDesc(mDescOff);
        }
    }
}
