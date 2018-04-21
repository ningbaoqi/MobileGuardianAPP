package ningbaoqi.com.mobileguardianapp.home.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import ningbaoqi.com.mobileguardianapp.R;
import ningbaoqi.com.mobileguardianapp.settings.activity.SettingsActivity;
import ningbaoqi.com.mobileguardianapp.utils.MD5Utils;
import ningbaoqi.com.mobileguardianapp.utils.SharedPreferenceItemConfig;

/**
 * Created by ningbaoqi on 18-4-20.
 * 主界面
 */

public class HomeActivity extends AppCompatActivity {
    private static final int PHONE_PREVENT_STEAL = 0;
    private static final int SETTING_CENTER = 8;
    private SharedPreferences sharedPreferences;
    private String[] stringItems;
    private int[] imageIds;
    private GridView mGridView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);
        sharedPreferences = getSharedPreferences(SharedPreferenceItemConfig.SharedPreferenceFileName, MODE_PRIVATE);
        stringItems = getResources().getStringArray(R.array.home_item_string_array);
        TypedArray imageArray = getResources().obtainTypedArray(R.array.home_all_image_array);
        imageIds = new int[imageArray.length()];
        for (int i = 0; i < imageArray.length(); i++) {
            imageIds[i] = imageArray.getResourceId(i, 0);
        }
        imageArray.recycle();
        mGridView = (GridView) findViewById(R.id.home_gridview);
        mGridView.setAdapter(new HomeAdapter());
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case PHONE_PREVENT_STEAL:
                        showPassworDialog();
                        break;
                    case SETTING_CENTER:
                        startActivity(new Intent(HomeActivity.this, SettingsActivity.class));
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void showPassworDialog() {
        String savedPassword = sharedPreferences.getString(SharedPreferenceItemConfig.SharedPreferencePassword, null);
        if (!TextUtils.isEmpty(savedPassword)) {
            showPasswordInputDialog();
        } else {
            showPasswordSetDialog();
        }
    }

    private void showPasswordInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(this , R.layout.dialog_input_password , null);
        dialog.setView(view);
        Button btn_OK = view.findViewById(R.id.yes);
        Button btn_CANCEL = view.findViewById(R.id.cancel);
        final EditText etPassword = view.findViewById(R.id.et_password);
        btn_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputPassword = etPassword.getText().toString();
                String savedPassword = sharedPreferences.getString(SharedPreferenceItemConfig.SharedPreferencePassword , null);
                if (!TextUtils.isEmpty(inputPassword) && MD5Utils.encode(inputPassword).equals(savedPassword)) {
                    dialog.dismiss();
//                    startActivity(new Intent(HomeActivity.this, LosedFindActivity.class));
                } else {
                    Toast.makeText(HomeActivity.this , "输入密码错误" , Toast.LENGTH_LONG).show();
                    etPassword.setText("");
                }
            }
        });
        btn_CANCEL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void showPasswordSetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(this , R.layout.dialog_set_password , null);
        dialog.setView(view);
        //dialog.setView(view , 0 , 0 , 0 , 0 );//兼容2.3以下的版本
        Button btn_OK = view.findViewById(R.id.yes);
        Button btn_CANCEL = view.findViewById(R.id.cancel);
        final EditText et_password = view.findViewById(R.id.et_password);
        final EditText et_confirm = view.findViewById(R.id.et_confirm);
        btn_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = et_password.getText().toString();
                String confirm = et_confirm.getText().toString();
                if (!TextUtils.isEmpty(password) && password.equals(confirm)) {
                    sharedPreferences.edit().putString(SharedPreferenceItemConfig.SharedPreferencePassword, MD5Utils.encode(password)).commit();
                    dialog.dismiss();
//                    startActivity(new Intent(HomeActivity.this, LosedFindActivity.class));
                } else {
                    Toast.makeText(HomeActivity.this , "输入的密码不正确" , Toast.LENGTH_LONG).show();
                }
            }
        });
        btn_CANCEL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    class HomeAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return stringItems.length;
        }

        @Override
        public Object getItem(int position) {
            return stringItems[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(HomeActivity.this, R.layout.home_item_layout, null);
            ImageView imageView = view.findViewById(R.id.home_item_imageview);
            TextView textView = view.findViewById(R.id.home_item_textview);
            textView.setText(stringItems[position]);
            imageView.setImageResource(imageIds[position]);
            return view;
        }
    }
}
