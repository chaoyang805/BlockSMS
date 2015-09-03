package com.chaoyang805.blocksms.app;

import android.app.Application;

import com.chaoyang805.blocksms.db.SMSDAOImpl;
import com.chaoyang805.blocksms.utils.LogHelper;

/**
 * Created by chaoyang805 on 2015/8/22.
 * 在application中初始化操作数据库的对象
 */
public class BlockSMSApp extends Application {

    private SMSDAOImpl mSMSDaoImpl;
    private static final String TAG = LogHelper.makeLogTag(BlockSMSApp.class);
    @Override
    public void onCreate() {
        super.onCreate();
        LogHelper.isShouldUseLog = false;
        mSMSDaoImpl = new SMSDAOImpl(this);
        LogHelper.d(TAG,"mSMSDaoImpl Initialized");
    }

    public SMSDAOImpl getSMSDaoImpl(){
        return mSMSDaoImpl;
    }
}
