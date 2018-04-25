package ningbaoqi.com.popwindowdemo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void click(View v) {
        TextView textView = new TextView(this);
        textView.setText("haha");
        textView.setBackgroundColor(Color.BLACK);
        PopupWindow popupWindow = new PopupWindow(textView, 100, 100);
        popupWindow.showAtLocation(findViewById(R.id.button) , Gravity.LEFT + Gravity.TOP , 100 , 100);
    }
}
