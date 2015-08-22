package com.chaoyang805.blocksms;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_detail);
        initViews();
        //从application类中取得数据库操作对象
        mSMSDaoImpl = ((BlockSMSApp)getApplication()).getSMSDaoImpl();
        Intent intent = getIntent();
        //获得intent中的id信息，默认为-1。
        int relatedId = intent.getIntExtra(Constants.EXTRA_ID, -1);
        if (relatedId < 0) {
            LogHelper.d(TAG, "relatedId = " + relatedId + "< 0,return!");
            return;
        }
        //从数据库查询短信
        SMS sms = mSMSDaoImpl.getSMSById(relatedId);
        showSMSDetail(sms);

    }

    /**
     * 为视图控件绑定id
     */
    private void initViews() {
        mTvSMSDetail = (TextView) findViewById(R.id.tv_sms_detail);
        mTvRelatedNum = (TextView) findViewById(R.id.tv_related_num);
        mTvRelatedTime = (TextView) findViewById(R.id.tv_related_time);
    }

    /**
     * 将查询出来的结果显示在控件上
     * @param sms
     */
    private void showSMSDetail(SMS sms) {
        LogHelper.d(TAG, "showSMSDetail");
        mTvSMSDetail.setText(sms.getSMSInfo());
        mTvRelatedNum.setText(sms.getPhoneNum());
        mTvRelatedTime.setText(StringFormatUtil.formatMilliseconds(sms.getReceivedTime()));
    }

}

