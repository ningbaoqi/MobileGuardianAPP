package ningbaoqi.com.mobileguardianapp.communicationguard.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ningbaoqi.com.mobileguardianapp.R;
import ningbaoqi.com.mobileguardianapp.communicationguard.adapter.BaseListViewAdapter;
import ningbaoqi.com.mobileguardianapp.communicationguard.bean.BlackNumberInfo;
import ningbaoqi.com.mobileguardianapp.communicationguard.db.BlackNumber;

import static android.view.View.inflate;

/**
 * Created by ningbaoqi on 18-4-24.
 */

public class ComminicationGuardActivity extends AppCompatActivity {

    private ListView listView;
    private List<BlackNumberInfo> list;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    llProgressBar.setVisibility(View.INVISIBLE);
                    if (adapter == null) {
                        adapter = new MyBlackNumberAdapter(list, ComminicationGuardActivity.this);
                        listView.setAdapter(adapter);
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    };
    private LinearLayout llProgressBar;
    private MyBlackNumberAdapter adapter;
    private int startIndex = 0;//开始位置
    private int maxCount = 20;//每页20条数据
    private BlackNumber blackNumber1;
    private int totalNumber;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.communication_layout_2);
        initUi();
        initData();
    }

    private void initUi() {
        llProgressBar = (LinearLayout) findViewById(R.id.ll_progress_bar);
        llProgressBar.setVisibility(View.VISIBLE);
        listView = (ListView) findViewById(R.id.list_view);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            /**
             * 滚动状态改变时回调
             * */
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                Log.d("nbq", "onScrollStateChanged");
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        /**
                         * 获取最后一条显示的数据位置
                         * */
                        int lastPosition = listView.getLastVisiblePosition();
                        Log.d("nbq", "---" + lastPosition);
                        if (lastPosition == list.size() - 1) {
                            startIndex += maxCount;
                            if (startIndex > totalNumber) {
                                Toast.makeText(ComminicationGuardActivity.this, "已经没有数据了", Toast.LENGTH_LONG).show();
                                return;
                            }
                            initData();
                        }
                        break;
                }
            }

            /**
             * 滚动时调用该方法
             * */
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                Log.d("nbq", "onScroll");
            }
        });
    }

    private void initData() {
        blackNumber1 = new BlackNumber(ComminicationGuardActivity.this);
        totalNumber = blackNumber1.getTotalNumber();
        new Thread() {
            @Override
            public void run() {
                if (list == null) {
                    list = blackNumber1.findPar2(startIndex, maxCount);
                } else {
                    list.addAll(blackNumber1.findPar2(startIndex, maxCount));
                }
                handler.sendEmptyMessage(0);
            }
        }.start();
    }

    private class MyBlackNumberAdapter extends BaseListViewAdapter<BlackNumberInfo> {

        public MyBlackNumberAdapter(List lists, Context context) {
            super(lists, context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = inflate(ComminicationGuardActivity.this, R.layout.item_black_number, null);
                viewHolder = new ViewHolder();
                viewHolder.tv_number = convertView.findViewById(R.id.tv_number);
                viewHolder.tv_mode = convertView.findViewById(R.id.tv_mode);
                viewHolder.iv_delete = convertView.findViewById(R.id.delete);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tv_number.setText(lists.get(position).getNumber());
            Log.d("nbq", lists.get(position).getNumber());
            String mode = lists.get(position).getMode();
            String modeString = "";
            if (mode.equals("1")) {
                modeString = getResources().getString(R.string.all_interrupt);
            } else if (mode.equals("2")) {
                modeString = getResources().getString(R.string.sms_interrupt);
            } else if (mode.equals("3")) {
                modeString = getResources().getString(R.string.phone_intercept);
            }
            final BlackNumberInfo info = list.get(position);
            viewHolder.iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String number = info.getNumber();
                    String mode = info.getMode();
                    boolean result = blackNumber1.delete(number);
                    if (result) {
                        Toast.makeText(ComminicationGuardActivity.this, "删除成功", Toast.LENGTH_LONG).show();
                        list.remove(info);
                        adapter.notifyDataSetChanged();//通知数据改变刷新界面
                    } else {
                        Toast.makeText(ComminicationGuardActivity.this, "删除失败", Toast.LENGTH_LONG).show();
                    }
                }
            });
            viewHolder.tv_mode.setText(modeString);
            return convertView;
        }
    }

    static class ViewHolder {
        TextView tv_number;
        TextView tv_mode;
        ImageView iv_delete;
    }

    /**
     * 添加黑名单
     *
     * @param view
     */
    public void addBlackNumber(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View blackDialog = View.inflate(this, R.layout.black_number_dialog, null);
        final EditText et_number = blackDialog.findViewById(R.id.et_phone_number);
        Button comfirm = blackDialog.findViewById(R.id.comfirm);
        Button cancel = blackDialog.findViewById(R.id.cancel);
        final CheckBox cb_phone = blackDialog.findViewById(R.id.phone_interrupt);
        final CheckBox cb_sms = blackDialog.findViewById(R.id.sms_interrupt);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = et_number.getText().toString().trim();
                if (TextUtils.isEmpty(number)) {
                    Toast.makeText(ComminicationGuardActivity.this, "please input black number", Toast.LENGTH_LONG).show();
                    return;
                }
                String mode = "";
                if (cb_phone.isChecked() && cb_sms.isChecked()) {
                    mode = "1";
//                    mode = getResources().getString(R.string.all_interrupt);
                } else if (cb_phone.isChecked()) {
                    mode = "2";
//                    mode = getResources().getString(R.string.phone_intercept);
                } else if (cb_sms.isChecked()) {
                    mode = "3";
//                    mode = getResources().getString(R.string.sms_interrupt);
                } else {
                    Toast.makeText(ComminicationGuardActivity.this, "please choose interrupt mode", Toast.LENGTH_LONG).show();
                    return;
                }
                blackNumber1.add(number, mode);
                BlackNumberInfo info = new BlackNumberInfo();
                info.setMode(mode);
                info.setNumber(number);
                list.add(0, info);
                if (adapter == null) {
                    MyBlackNumberAdapter adapter = new MyBlackNumberAdapter(list, ComminicationGuardActivity.this);
                    listView.setAdapter(adapter);
                } else {
                    adapter.notifyDataSetChanged();
                }
                dialog.dismiss();
            }
        });
        dialog.setView(blackDialog);
        dialog.show();
    }
}
