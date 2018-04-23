package ningbaoqi.com.mobileguardianapp.settings.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ningbaoqi.com.mobileguardianapp.R;
import ningbaoqi.com.mobileguardianapp.utils.SharedPreferenceItemConfig;

/**
 * Created by ningbaoqi on 18-4-23.
 */

public class DragViewActivity extends AppCompatActivity {

    private TextView tv_top;
    private TextView tv_bottom;
    private ImageView dragImage;
    private int startX;
    private int startY;
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drag_view_layout);
        sharedPreferences = getSharedPreferences(SharedPreferenceItemConfig.SharedPreferenceFileName, MODE_PRIVATE);
        tv_top = (TextView) findViewById(R.id.tv_top);
        tv_bottom = (TextView) findViewById(R.id.tv_bottom);
        dragImage = (ImageView) findViewById(R.id.iv_drag);
        int saveLeft = sharedPreferences.getInt(SharedPreferenceItemConfig.SharedPreferenceDragLastLeft, 0);
        int saveTop = sharedPreferences.getInt(SharedPreferenceItemConfig.SharedPreferenceDragLastTop, 0);
        /**
         * 拿到布局参数
         * */
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) dragImage.getLayoutParams();
        params.leftMargin = saveLeft;
        params.topMargin = saveTop;
        dragImage.setLayoutParams(params);
        dragImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN://记录按下开始位置
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP://保存最后的坐标
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt(SharedPreferenceItemConfig.SharedPreferenceDragLastLeft , dragImage.getLeft());
                        editor.putInt(SharedPreferenceItemConfig.SharedPreferenceDragLastTop , dragImage.getTop());
                        editor.commit();
                        break;
                    case MotionEvent.ACTION_MOVE://计算移动偏移量
                        int endX = (int) event.getRawX();
                        int endY = (int) event.getRawY();
                        int dx = endX - startX;
                        int dy = endY - startY;
                        int left = dragImage.getLeft() + dx;
                        int right = dragImage.getRight() + dx;
                        int top = dragImage.getTop() + dy;
                        int bottom = dragImage.getBottom() + dy;
                        dragImage.layout(left, top, right, bottom);
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                }
                return true;
            }
        });
    }
}
