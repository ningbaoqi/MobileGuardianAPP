package ningbaoqi.com.contactsdemo;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
/**
 * 获取联系人电话和名字
 * */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Uri rawCOntactsUri = Uri.parse("content://com.android.contacts/raw_contacts");
        Uri dataUri = Uri.parse("content://com.android.contacts/data");
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        Cursor rawContactsCursor = getContentResolver().query(rawCOntactsUri, new String[]{"contact_id"}, null, null, null);
        if (rawContactsCursor != null) {
            while (rawContactsCursor.moveToNext()) {
                String contactID = rawContactsCursor.getString(0);
                Cursor dataCursor = getContentResolver().query(dataUri, new String[]{"data1", "mimetype"}, "raw_contact_id = ?", new String[]{contactID}, null);
                if (dataCursor != null) {
                    HashMap<String, String> map = new HashMap<>();
                    while (dataCursor.moveToNext()) {
                        String data1 = dataCursor.getString(0);
                        String mimetype = dataCursor.getString(1);
                        if ("vnd.android.cursor.item/phone_v2".equals(mimetype)) {
                            map.put("phone", data1);
                        } else if ("vnd.android.cursor.item/name".equals(mimetype)) {
                            map.put("name", data1);
                        }
                    }
                    list.add(map);
                    Log.d("nbq", list.toString());
                    dataCursor.close();
                }
            }
            rawContactsCursor.close();
        }
    }
}
