package ningbaoqi.com.mobileguardianapp.communicationguard.activity;

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

/**
 * =========================================
 * <p/>
 * 版    权: ningxiansheng
 * <p/>
 * 作    者: 宁宝琪
 * <p/>
 * 版    本:1.0
 * <p/>
 * 创建 时间:18-4-25
 * <p/>
 * 描    述: 实现分批加载
 * <p/>
 * <p/>
 * 修订 历史:
 * <p/>
 * =========================================
 */
public class ComminicationGuardActivity2 extends AppCompatActivity {

    private ListView listView;
    private List<BlackNumberInfo> list;
    private int mCurrentPageNumber = 0;//当前是哪一页
    private int mPageSize = 20;//每页多少条数据
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    llProgressBar.setVisibility(View.INVISIBLE);
                    tvPageNumber.setText(mCurrentPageNumber + "/" + totalPage);
                    adapter = new MyBlackNumberAdapter(list, ComminicationGuardActivity2.this);
                    listView.setAdapter(adapter);
                    break;
            }
        }
    };
    private LinearLayout llProgressBar;
    private TextView tvPageNumber;
    private BlackNumber blackNumber;
    private int totalPage;//一共有多少页
    private EditText etPageNumber;
    private MyBlackNumberAdapter adapter;

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
        tvPageNumber = (TextView) findViewById(R.id.tv_page_number);
        etPageNumber = (EditText) findViewById(R.id.et_page_number);
    }

    private void initData() {
        new Thread() {
            @Override
            public void run() {
                //通过总的记录数来动态实现
                blackNumber = new BlackNumber(ComminicationGuardActivity2.this);
                totalPage = blackNumber.getTotalNumber() / mPageSize;
//                list = blackNumber.findAll();
                list = blackNumber.findPar(mCurrentPageNumber, mPageSize);
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
                convertView = View.inflate(ComminicationGuardActivity2.this, R.layout.item_black_number, null);
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
                    boolean result = blackNumber.delete(number);
                    if (result) {
                        Toast.makeText(ComminicationGuardActivity2.this, "删除成功", Toast.LENGTH_LONG).show();
                        list.remove(info);
                        adapter.notifyDataSetChanged();//通知数据改变刷新界面
                    } else {
                        Toast.makeText(ComminicationGuardActivity2.this , "删除失败" , Toast.LENGTH_LONG).show();
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
     * 上一页
     *
     * @param view
     */
    public void prePage(View view) {
        if (mCurrentPageNumber <= 0) {
            Toast.makeText(this, "到顶了", Toast.LENGTH_LONG).show();
            return;
        }
        mCurrentPageNumber--;
        initData();
    }

    /**
     * 下一页
     *
     * @param view
     */
    public void nextPage(View view) {
        if (mCurrentPageNumber > (totalPage - 1)) {
            Toast.makeText(this, "到底了", Toast.LENGTH_LONG).show();
            return;
        }
        mCurrentPageNumber++;
        initData();
    }

    /**
     * 跳转到哪一页
     *
     * @param view
     */
    public void jumpToPage(View view) {
        String strPageNumber = etPageNumber.getText().toString().trim();
        if (!TextUtils.isEmpty(strPageNumber)) {
            int number = Integer.parseInt(strPageNumber);
            if (number >= 0 && number <= totalPage) {
                mCurrentPageNumber = number;
                initData();
            } else {
                Toast.makeText(this, "please enter page", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "please enter page", Toast.LENGTH_LONG).show();
        }

    }
}
