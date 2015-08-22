package com.chaoyang805.blocksms.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.chaoyang805.blocksms.bean.SMS;
import com.chaoyang805.blocksms.db.SMSDAOImpl;
import com.chaoyang805.blocksms.utils.LogHelper;

public class SMSReceiver extends BroadcastReceiver {

    public static final String ACTION_SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private static final String TAG = "SMSReceiver";
    private Context mContext;
    private SMSDAOImpl mSMSDAO;
    /**
     * OriginatingAddress 10010 :
     * DisplayOriginatingAddress 10010 :
     * DisplayMessageBody 尊敬的用户，请回复以下编码办理业务：
     * 1505：已订业务查询
     * 1503：积分查询
     * 1071：手机上网流量查询
     * 15 :
     * getTimestampMillis() 1440080508000
     * 10010 :
     * 10010 :
     * 01：套餐余量查询
     * 5083：流量半年包余量查询
     * 【买4G就上 m.10010.com】 :
     * 1440080512000
     */
    public SMSReceiver(){

    }

    public SMSReceiver(Context context,SMSDAOImpl smsdao) {
        mContext = context;
        mSMSDAO = smsdao;
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION_SMS_RECEIVED)) {
            SmsMessage[] msgs = getSMSFromIntent(intent);
            String phoneNum;
            StringBuilder msg = new StringBuilder();
            long receivedTime;
            for (SmsMessage message : msgs) {
//                Log.i(TAG, message.getOriginatingAddress() + " : " +
//                        message.getDisplayOriginatingAddress() + " : " +
//                        message.getDisplayMessageBody() + " : " +
//                        message.getTimestampMillis());
                msg.append(message.getDisplayMessageBody());

            }
            phoneNum = msgs[0].getOriginatingAddress();
            receivedTime = msgs[0].getTimestampMillis();
            SMS sms = new SMS(receivedTime, msg.toString(), phoneNum);
            mSMSDAO.insertBlockSMS(sms);
            LogHelper.d(TAG, "SMS was blocked:" + msg);
            Toast.makeText(mContext,"拦截到短信",Toast.LENGTH_SHORT).show();
            abortBroadcast();
            Log.d(TAG, "broadcast was aborted");
        }
    }

    public SmsMessage[] getSMSFromIntent(Intent intent) {
        Object[] messages = (Object[]) intent.getSerializableExtra("pdus");
        byte[][] pduObjs = new byte[messages.length][];
        for (int i = 0; i < messages.length; i++) {
            pduObjs[i] = (byte[]) messages[i];
        }
        byte[][] pdus = new byte[pduObjs.length][];
        int pduCount = pdus.length;
        SmsMessage[] msgs = new SmsMessage[pduCount];
        for (int i = 0; i < pduCount; i++) {
            pdus[i] = pduObjs[i];
            msgs[i] = SmsMessage.createFromPdu(pdus[i]);
        }
        return msgs;
    }
}
