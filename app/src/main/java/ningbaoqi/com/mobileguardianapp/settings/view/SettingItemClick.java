package ningbaoqi.com.mobileguardianapp.settings.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ningbaoqi.com.mobileguardianapp.R;

/**
 * Created by ningbaoqi on 18-4-21.
 */

public class SettingItemClick extends RelativeLayout {
    private TextView settingTitle;
    private TextView settingDesc;

    public SettingItemClick(Context context) {
        super(context);
        initView();
    }

    public SettingItemClick(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SettingItemClick(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View.inflate(getContext(), R.layout.settings_item_click, this);
        settingTitle = findViewById(R.id.setting_title);
        settingDesc = findViewById(R.id.setting_desc);
    }

    public void setSettingTitle(String title) {
        settingTitle.setText(title);
    }

    public void setSettingDesc(String desc) {
        settingDesc.setText(desc);
    }

}
