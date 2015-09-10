package com.chaoyang805.blocksms;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.chaoyang805.blocksms.app.BlockSMSApp;
import com.chaoyang805.blocksms.bean.SMS;
import com.chaoyang805.blocksms.db.SMSDAOImpl;
import com.chaoyang805.blocksms.utils.Constants;
import com.chaoyang805.blocksms.utils.LogHelper;
import com.chaoyang805.blocksms.utils.StringFormatUtil;

/**
 * Created by chaoyang805 on 2015/8/22.
 */
public class SMSDetailActivity extends AppCompatActivity {
    private static final String TAG = LogHelper.makeLogTag(SMSDetailActivity.class);
    /**
     * 显示短信的详细内容
     */
    private TextView mTvSMSDetail;
    /**
     * 显示和短信相关号码的textview
     */
    private TextView mTvRelatedNum;
    /**
     * 显示和短息相关的时间的textview
     */
    private TextView mTvRelatedTime;
    /**
     * 数据库操作的对象
     */
    private SMSDAOImpl mSMSDaoImpl;

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_detail);
        initViews();
        //从application类中取得数据库操作对象
        mSMSDaoImpl = ((BlockSMSApp) getApplication()).getSMSDaoImpl();
        Intent intent = getIntent();
        //获得intent中的id信息，默认为-1。
        int relatedId = intent.getIntExtra(Constants.EXTRA_ID, -1);
        if (relatedId < 0) {
            Toast.makeText(SMSDetailActivity.this, "查询出错", Toast.LENGTH_SHORT).show();
            return;
        } else {
            //从数据库查询短信
            SMS sms = mSMSDaoImpl.getSMSById(relatedId);
            showSMSDetail(sms);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //点击左上角向左箭头实现退出Activity
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * 为视图控件绑定id
     */
    private void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTvSMSDetail = (TextView) findViewById(R.id.tv_sms_detail);
        mTvRelatedNum = (TextView) findViewById(R.id.tv_related_num);
        mTvRelatedTime = (TextView) findViewById(R.id.tv_related_time);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    /**
     * 将查询出来的结果显示在控件上
     *
     * @param sms
     */
    private void showSMSDetail(SMS sms) {
        LogHelper.d(TAG, "showSMSDetail");
        mTvSMSDetail.setText(sms.getSMSInfo());
        mTvRelatedNum.setText(getString(R.string.from, sms.getPhoneNum()));
        mTvRelatedTime.setText(StringFormatUtil.formatMilliseconds(sms.getReceivedTime()));
    }

}

