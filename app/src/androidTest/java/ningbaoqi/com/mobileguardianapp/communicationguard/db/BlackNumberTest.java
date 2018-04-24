package ningbaoqi.com.mobileguardianapp.communicationguard.db;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import org.junit.Test;

import java.util.List;
import java.util.Random;

import ningbaoqi.com.mobileguardianapp.communicationguard.bean.BlackNumberInfo;

import static junit.framework.Assert.assertEquals;

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
public class BlackNumberTest {


    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context context = InstrumentationRegistry.getTargetContext();
    }

    @Test
    public void add() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        BlackNumber blackNumber = new BlackNumber(context);
        Random random = new Random();
        for (int i = 0; i < 200; i++) {
            Long number = 12345678000L + i;
            blackNumber.add(number + "", String.valueOf(random.nextInt(3) + 1));
        }
    }

    @Test
    public void delete() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        BlackNumber blackNumber = new BlackNumber(context);
        boolean delete = blackNumber.delete("12345678000");
        assertEquals(true , delete);
    }

    @Test
    public void changeNumberMode() throws Exception {

    }

    @Test
    public void findNumber() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        BlackNumber blackNumber = new BlackNumber(context);
        String mode = blackNumber.findNumber("12345678001");
    }

    @Test
    public void findAll() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        BlackNumber blackNumber = new BlackNumber(context);
        List<BlackNumberInfo> list = blackNumber.findAll();
        for (BlackNumberInfo info: list) {
            Log.d("nbq", info.getMode() + "------" + info.getNumber());
        }
    }
}