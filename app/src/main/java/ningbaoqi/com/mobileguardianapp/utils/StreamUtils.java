package ningbaoqi.com.mobileguardianapp.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by root on 18-4-20.读取流工具
 *
 * @author ningbaoqi
 */

public class StreamUtils {
    /**
     * 将输入流读取成String后返回
     */
    public static String readFromStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int length;
        byte[] buffer = new byte[1024];
        while ((length = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer , 0 , length);
        }
        String result = outputStream.toString();
        inputStream.close();
        outputStream.close();
        return result;
    }
}
