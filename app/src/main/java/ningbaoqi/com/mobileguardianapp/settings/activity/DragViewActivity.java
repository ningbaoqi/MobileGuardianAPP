package ningbaoqi.com.mobileguardianapp.settings.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
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

public class DragViewActivity extends Activity {

    private TextView tv_top;
    private TextView tv_bottom;
    private ImageView dragImage;
    private int startX;
    private int startY;
    private SharedPreferences sharedPreferences;
    private int windowWidth;
    private int windowHeight;
    private long[] hints;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drag_view_layout);
        sharedPreferences = getSharedPreferences(SharedPreferenceItemConfig.SharedPreferenceFileName, MODE_PRIVATE);
        tv_top = (TextView) findViewById(R.id.tv_top);
        tv_bottom = (TextView) findViewById(R.id.tv_bottom);
        dragImage = (ImageView) findViewById(R.id.iv_drag);
        windowWidth = getWindowManager().getDefaultDisplay().getWidth();
        windowHeight = getWindowManager().getDefaultDisplay().getHeight();
        int saveLeft = sharedPreferences.getInt(SharedPreferenceItemConfig.SharedPreferenceDragLastLeft, 0);
        int saveTop = sharedPreferences.getInt(SharedPreferenceItemConfig.SharedPreferenceDragLastTop, 0);
        if (saveTop > windowHeight / 2) {//上边显示下边隐藏
            tv_top.setVisibility(View.VISIBLE);
            tv_bottom.setVisibility(View.INVISIBLE);
        } else {//下边显示上边隐藏
            tv_top.setVisibility(View.INVISIBLE);
            tv_bottom.setVisibility(View.VISIBLE);
        }
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
                        editor.putInt(SharedPreferenceItemConfig.SharedPreferenceDragLastLeft, dragImage.getLeft());
                        editor.putInt(SharedPreferenceItemConfig.SharedPreferenceDragLastTop, dragImage.getTop());
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
                        if (left < 0 || right > windowWidth || top < 0 || bottom > windowHeight - 80) {//需要减掉状态栏的高度
                            break;
                        }
                        if (top > windowHeight / 2) {//上边显示下边隐藏
                            tv_top.setVisibility(View.VISIBLE);
                            tv_bottom.setVisibility(View.INVISIBLE);
                        } else {//下边显示上边隐藏
                            tv_top.setVisibility(View.INVISIBLE);
                            tv_bottom.setVisibility(View.VISIBLE);
                        }
                        dragImage.layout(left, top, right, bottom);//更新界面
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                }
                return false;
            }
        });
        hints = new long[2];
        dragImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.arraycopy(hints, 1, hints, 0, hints.length - 1);
                hints[hints.length - 1] = SystemClock.uptimeMillis();
                if (hints[0] >= (SystemClock.uptimeMillis() - 500)) {
                    dragImage.layout(windowWidth / 2 - dragImage.getWidth() / 2, dragImage.getTop(), windowWidth / 2 + dragImage.getWidth() / 2, dragImage.getBottom());
                }
            }
        });
    }
}
