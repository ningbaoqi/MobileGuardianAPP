package ningbaoqi.com.mobileguardianapp.communicationguard.db;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.Test;

/**
 * =========================================
 * <p/>
 * 版    权: ningxiansheng
 * <p/>
 * 作    者: 宁宝琪
 * <p/>
 * 版    本:1.0
 * <p/>
 * 创建 时间:18-4-24
 * <p/>
 * 描    述:
 * <p/>
 * <p/>
 * 修订 历史:
 * <p/>
 * =========================================
 */
public class BlackNumberOpenHelperTest {
    @Test
    public void onCreate() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        BlackNumberOpenHelper helper = new BlackNumberOpenHelper(context);
        helper.getReadableDatabase();
    }

    @Test
    public void onUpgrade() throws Exception {

    }

}