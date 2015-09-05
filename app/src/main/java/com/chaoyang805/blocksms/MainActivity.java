package com.chaoyang805.blocksms;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.chaoyang805.blocksms.adapter.SMSAdapter;
import com.chaoyang805.blocksms.app.BlockSMSApp;
import com.chaoyang805.blocksms.bean.SMS;
import com.chaoyang805.blocksms.db.SMSDAOImpl;
import com.chaoyang805.blocksms.fragment.DrawerFragment;
import com.chaoyang805.blocksms.fragment.SMSFragment;
import com.chaoyang805.blocksms.utils.Constants;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {
    /**
     * 显示拦截到的短信的listview
     */
    private ListView mLvBlockedSMS;
    /**
     * 查看黑名单的button
     */
    private Button mBtnShowBlackList;
    /**
     * 查看关键词的button
     */
    private Button mBtnShowKeywords;
    /**
     * 数据库访问对象
     */
    private SMSDAOImpl mSMSDaoImpl;
    /**
     * listview对应的adapter
     */
    private SMSAdapter mAdapter;
    /**
     * 保存从数据库中查询出来的短信
     */
    private List<SMS> mList;
    /**
     * notificationmanager用来取消所有的通知
     */
    private NotificationManager mNotificationManager;

    private Toolbar mToolbar;

    private DrawerLayout mDrawerLayout;

    private DrawerFragment mDrawerFragment;

    private SMSFragment mSMSFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initDrawer();
        initDatas();
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        initSMSFragment();
    }

    private void initSMSFragment() {
        mSMSFragment = new SMSFragment();

        getFragmentManager().beginTransaction().replace(R.id.container, mSMSFragment, "sms_fragment").commit();
    }

    /**
     * 初始化drawer
     */
    private void initDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerFragment = (DrawerFragment) getFragmentManager().findFragmentById(R.id.drawer_fragment);

        //设置drawer上的statusbar颜色
//        TypedValue t = new TypedValue();
//        getTheme().resolveAttribute(R.attr.colorPrimaryDark, t, true);
//        int color = t.data;
//        Log.e("TAG", "color = " + color);
//        mDrawerLayout.setStatusBarBackgroundColor(color);
        //设置toolbar
        setSupportActionBar(mToolbar);
        mDrawerFragment.setupDrawer(R.id.drawer_fragment_container,mDrawerLayout,mToolbar);
    }

    /**
     * 初始化视图控件
     */
    private void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
//        mLvBlockedSMS = (ListView) findViewById(R.id.lv_blocked_sms);
//        mBtnShowBlackList = (Button) findViewById(R.id.btn_show_blacklist);
//        mBtnShowKeywords = (Button) findViewById(R.id.btn_show_key_words);
//        mBtnShowBlackList.setOnClickListener(this);
//        mBtnShowKeywords.setOnClickListener(this);
//        mLvBlockedSMS.setOnItemLongClickListener(this);
//        mLvBlockedSMS.setOnItemClickListener(this);
    }

    /**
     * 初始化数据
     */
    private void initDatas() {
        mSMSDaoImpl = ((BlockSMSApp) getApplication()).getSMSDaoImpl();
        mList = mSMSDaoImpl.getAllSMS();
        if (mList.size() > 0) {
            getSupportActionBar().setTitle(R.string.blocked_sms);
        } else {
            getSupportActionBar().setTitle(R.string.no_sms_was_blocked);
        }
//        mAdapter = new SMSAdapter(this, mList);
//        mLvBlockedSMS.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //从暂停中恢复后通过数据库更新界面
//        updateUI();
        //取消掉拦截通知
        mNotificationManager.cancel(Constants.NOTIFICATION_ID);
    }

    /**
     * button的点击监听
     * @param v
     */
    @Override
    public void onClick(View v) {
//        Intent intent;
//        switch (v.getId()) {
//            case R.id.btn_show_key_words:
//                intent = new Intent(this, KeywordsActivity.class);
//                startActivity(intent);
//                break;
//            case R.id.btn_show_blacklist:
//                intent = new Intent(this, BlockedPhoneActivity.class);
//                startActivity(intent);
//                break;
//
//        }
    }

    /**
     * 长按listview的item时，提示删除
     * @param parent
     * @param view
     * @param position
     * @param id
     * @return
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        //显示删除的dialog
        showDeleteDialog(position);
        return true;
    }

    /**
     * 显示删除的dialog
     * @param position
     */
    private void showDeleteDialog(final int position) {
        AlertDialog.Builder deleteDialogBuilder = new AlertDialog.Builder(this);
        deleteDialogBuilder
                .setTitle(R.string.delete_sms)
                .setMessage(R.string.are_you_sure_you_want_delete_this_sms)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //将按下的item的对应信息从数据库删除
                        mSMSDaoImpl.deleteSMS(mAdapter.getItem(position));
                        //更新界面的数据
                        updateUI();

                    }
                })
                .setNegativeButton("取消", null).show();
    }

    /**
     * 更新界面
     */
    private void updateUI() {
        mList = mSMSDaoImpl.getAllSMS();
        if (mList.size() > 0) {
            getSupportActionBar().setTitle(R.string.blocked_sms);
        } else {
            getSupportActionBar().setTitle(R.string.no_sms_was_blocked);
        }
        mAdapter.updateList(mList);
    }

    /**
     * 点击item时，将显示详情界面
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //将sms对象对应的id通过intent传递到详情activity
        int currentPosId = mAdapter.getItem(position).getId();
        Intent intent = new Intent(this, SMSDetailActivity.class);
        intent.putExtra(Constants.EXTRA_ID, currentPosId);
        startActivity(intent);
    }
}
