package ningbaoqi.com.rocket;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

/**
 * Created by root on 18-4-24.
 */

public class RocketService extends Service {
    private int startX;
    private int startY;
    private WindowManager windowManager;
    private WindowManager.LayoutParams params;
    private int windowWidth;
    private int windowHeight;
    private View rocketView;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //可以在其他第三方app中弹出自己的浮窗
        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        windowWidth = windowManager.getDefaultDisplay().getWidth();
        windowHeight = windowManager.getDefaultDisplay().getHeight();
        //设置配置属性
        params = new WindowManager.LayoutParams();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        params.format = PixelFormat.TRANSLUCENT;
        params.type = WindowManager.LayoutParams.TYPE_PHONE;//需要权限
        params.gravity = Gravity.LEFT + Gravity.TOP;//将重心位置设置成左上方也就是(0,0)从左上方开始
        params.setTitle("Toast");
        rocketView = View.inflate(this, R.layout.rocket_layout, null);
        ImageView rocket = rocketView.findViewById(R.id.iv_rocket);
        rocket.setBackgroundResource(R.drawable.rocket_anim);
        AnimationDrawable animationDrawable = (AnimationDrawable) rocket.getBackground();
        animationDrawable.start();
        windowManager.addView(rocketView, params);
        rocketView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int endX = (int) event.getRawX();
                        int endY = (int) event.getRawY();
                        int dx = endX - startX;
                        int dy = endY - startY;
                        params.x += dx;
                        params.y += dy;
                        if (params.x < 0) {
                            params.x = 0;
                        }
                        if (params.y < 0) {
                            params.y = 0;
                        }
                        if (params.x > windowWidth - rocketView.getWidth()) {
                            params.x = windowWidth - rocketView.getWidth();
                        }
                        if (params.y > windowHeight - rocketView.getHeight()) {
                            params.y = windowHeight - rocketView.getHeight();
                        }
                        windowManager.updateViewLayout(rocketView, params);//更新浮窗
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        Log.d("nbq--", params.x + "" + ";" + params.y);
                        break;
                    case MotionEvent.ACTION_UP:
                        if (params.x > 280 && params.x < 650 && params.y > 1200) {
                            Log.d("nbq", params.x + "" + ";" + params.y);
                            sendSocket();
                            startActivity(new Intent(RocketService.this , BackgroundActivity.class));
                        }
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 发射火箭
     */
    private void sendSocket() {
        params.x = windowWidth / 2 - rocketView.getWidth() / 2;
        new Thread(new Runnable() {
            @Override
            public void run() {
                int position = 1200;
                for (int i = 0; i <= 10; i++) {
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    int y = position - 120 * i;
                    handler.sendEmptyMessage(y);
                }
            }
        }).start();

    }
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            params.y = msg.what;
            windowManager.updateViewLayout(rocketView, params);
        }
    };
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (windowManager != null && rocketView != null) {
            windowManager.removeView(rocketView);
        }
    }
}
