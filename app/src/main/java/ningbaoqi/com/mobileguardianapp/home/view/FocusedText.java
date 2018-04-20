package ningbaoqi.com.mobileguardianapp.home.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by ningbaoqi on 18-4-20.
 * 自动获取焦点的 TextView
 */

public class FocusedText extends TextView {

    /**
     * 用代码去 new 的时候走此方法
     */
    public FocusedText(Context context) {
        super(context);
    }

    /**
     * 有属性时走此方法
     */
    public FocusedText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 有style样式时会走此方法
     */
    public FocusedText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 表示有没有获取焦点，跑马灯要运行，首先会调用此函数判断是否有焦点，是 true 表示跑马灯获取焦点，所以强制获取焦点
     */
    @Override
    public boolean isFocused() {
        return true;
    }
}
