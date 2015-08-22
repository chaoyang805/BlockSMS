package com.chaoyang805.blocksms;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.chaoyang805.blocksms.adapter.SMSAdapter;
import com.chaoyang805.blocksms.bean.SMS;
import com.chaoyang805.blocksms.db.SMSDAOImpl;
import com.chaoyang805.blocksms.receiver.SMSReceiver;
import com.chaoyang805.blocksms.utils.LogHelper;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {

    private ListView mLvBlockedSMS;
    private TextView mTvNoBlockedSMS;
    private Button mBtnShowBlackList, mBtnShowKeywords;
    private SMSDAOImpl mSmsDao;
    private SMSAdapter mAdapter;
    private List<SMS> mList;
    private static final String TAG = LogHelper.makeLogTag(MainActivity.class);

    private SMSReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LogHelper.isShouldUseLog = true;
        initViews();
        initDatas();
        registerSMSReceiver();
    }

    private void registerSMSReceiver() {
        mReceiver = new SMSReceiver(this, mSmsDao);
        IntentFilter filter = new IntentFilter();
        filter.addAction(SMSReceiver.ACTION_SMS_RECEIVED);
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        registerReceiver(mReceiver, filter);
        LogHelper.d(TAG, "Broadcast Registered >>>>");
    }

    private void initDatas() {
        mSmsDao = new SMSDAOImpl(this);
        mList = mSmsDao.getAllSMS();
        if (mList.size() > 0 && mList != null) {
            mTvNoBlockedSMS.setVisibility(View.GONE);
            Log.d(TAG, "TextView GONE");
            mLvBlockedSMS.setVisibility(View.VISIBLE);
            mAdapter = new SMSAdapter(this, mList);
            mLvBlockedSMS.setAdapter(mAdapter);
        } else {
            mLvBlockedSMS.setVisibility(View.GONE);
            Log.d(TAG, "ListView GONE");
            mTvNoBlockedSMS.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 初始化视图控件
     */
    private void initViews() {
        mLvBlockedSMS = (ListView) findViewById(R.id.lv_blocked_sms);
        mBtnShowBlackList = (Button) findViewById(R.id.btn_show_blacklist);
        mBtnShowKeywords = (Button) findViewById(R.id.btn_show_key_words);
        mTvNoBlockedSMS = (TextView) findViewById(R.id.tv_no_blocked_sms);
        mBtnShowBlackList.setOnClickListener(this);
        mBtnShowKeywords.setOnClickListener(this);
        mLvBlockedSMS.setOnItemLongClickListener(this);
        mLvBlockedSMS.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //TODO 实现添加黑名单和关键字
        switch (v.getId()) {

        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        showAlertDialog(position);
        return true;
    }

    private void showAlertDialog(final int position) {
        AlertDialog.Builder deleteDialogBuilder = new AlertDialog.Builder(this);
        deleteDialogBuilder.setTitle("删除短信")
                .setMessage("确定要删除？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mSmsDao.deleteSMS(mAdapter.getItem(position));
                        mList = mSmsDao.getAllSMS();
                        if (mList.size() <= 0) {
                            mLvBlockedSMS.setVisibility(View.GONE);
                            mTvNoBlockedSMS.setVisibility(View.VISIBLE);
                        } else {
                            mAdapter.updateList(mList);
                        }
                    }
                })
                .setNegativeButton("取消", null).show();
    }

    /**
     * Activity销毁时取消广播
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //TODO 实现详情界面
    }
}
