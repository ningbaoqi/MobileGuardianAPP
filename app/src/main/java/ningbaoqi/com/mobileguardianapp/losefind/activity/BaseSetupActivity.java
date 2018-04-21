package ningbaoqi.com.mobileguardianapp.losefind.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import ningbaoqi.com.mobileguardianapp.utils.SharedPreferenceItemConfig;

/**
 * Created by root on 18-4-21.
 * 手机防盗引导页基类，如果不需要展示就不需要在清单文件中注册
 */

public abstract class BaseSetupActivity extends AppCompatActivity {
    public static final float DECONFIRMLENGTH = 100f;
    public static final float LOWSPEED = 100f;
    public static final float CONFIRMLENGTH = 200f;
    public SharedPreferences sharedPreferences;
    public GestureDetector gestureDetector;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences(SharedPreferenceItemConfig.SharedPreferenceFileName, MODE_PRIVATE);
        /**
         * 手势监测器
         * */
        /**
         * 监听手势滑动；e1：表示起始点；e2：表示滑动的终点:velocityX：水平速度；velocityY：表示垂直速度
         * getRawX：是基于整个屏幕的坐标；getX()：是基于控件的坐标
         * */ /**
         * 向右滑，应该显示上一页
         * */
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            /**
             * 监听手势滑动；e1：表示起始点；e2：表示滑动的终点:velocityX：水平速度；velocityY：表示垂直速度
             * getRawX：是基于整个屏幕的坐标；getX()：是基于控件的坐标
             * */
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (Math.abs(e2.getRawY() - e1.getRawY()) > DECONFIRMLENGTH) {
                    return true;
                }
                if (Math.abs(velocityX) < LOWSPEED) {
                    return true;
                }
                /**
                 * 向右滑，应该显示上一页
                 * */
                if ((e2.getRawX() - e1.getRawX()) > CONFIRMLENGTH) {
                    showPreviousPage();
                    return true;
                } else if ((e1.getRawX() - e2.getRawX()) > CONFIRMLENGTH) {
                    showNextPage();
                    return true;
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }

    protected abstract void showPreviousPage();

    protected abstract void showNextPage();

    public void next(View view) {
        showNextPage();
    }

    public void previous(View view) {
        showPreviousPage();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /**
         * 委托手势识别器处理touch事件
         * */
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}
