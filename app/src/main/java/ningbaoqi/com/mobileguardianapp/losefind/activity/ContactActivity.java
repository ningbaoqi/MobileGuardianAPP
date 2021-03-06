package ningbaoqi.com.mobileguardianapp.losefind.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import ningbaoqi.com.mobileguardianapp.R;

/**
 * Created by ningbaoqi on 18-4-21.
 */

public class ContactActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_activity);
        ListView listView = (ListView) findViewById(R.id.contact_list);
        final ArrayList<HashMap<String, String>> contacts = readContacts();
        listView.setAdapter(new SimpleAdapter(this, contacts, R.layout.contact_list_item, new String[]{"name", "phone"}, new int[]{R.id.contact_name, R.id.contact_phone}));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String phone = contacts.get(position).get("phone");
                Intent intent = new Intent();
                intent.putExtra("phone", phone);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

    private ArrayList<HashMap<String, String>> readContacts() {
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
        return list;
    }
}
