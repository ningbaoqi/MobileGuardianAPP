package ningbaoqi.com.rocket;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

/**
 * Created by root on 18-4-24.
 */

public class BackgroundActivity extends Activity{
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bg);
        ImageView smoke1 = (ImageView) findViewById(R.id.smoke_1);
        ImageView smoke2 = (ImageView) findViewById(R.id.smoke_2);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0 , 1);
        alphaAnimation.setDuration(1000);
        alphaAnimation.setFillAfter(true);
        smoke1.startAnimation(alphaAnimation);
        smoke2.startAnimation(alphaAnimation);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 1000);
    }
}
