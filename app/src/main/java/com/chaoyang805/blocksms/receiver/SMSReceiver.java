package com.chaoyang805.blocksms.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsMessage;

import com.chaoyang805.blocksms.BuildConfig;
import com.chaoyang805.blocksms.MainActivity;
import com.chaoyang805.blocksms.R;
import com.chaoyang805.blocksms.bean.Keyword;
import com.chaoyang805.blocksms.bean.SMS;
import com.chaoyang805.blocksms.db.SMSDAOImpl;
import com.chaoyang805.blocksms.utils.Constants;
import com.chaoyang805.blocksms.utils.LogHelper;

import java.util.List;


public class SMSReceiver extends BroadcastReceiver {

    public static final String ACTION_SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    public static final String ACTION_SMS_TEST = "com.chaoyang805.blocksms.SMSReceiver.TEST";

    private static final String TAG = "SMSReceiver";
    private SMSDAOImpl mSMSDaoImpl;


    public SMSReceiver() {
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        //通过传入的context实例化数据库访问的对象
        mSMSDaoImpl = new SMSDAOImpl(context);
        //是否为收到短信的广播
        if (intent.getAction().equals(ACTION_SMS_RECEIVED)) {
            //从intent中解析短信内容
            SmsMessage[] msgs = getSMSFromIntent(intent);

            String phoneNum;
            StringBuilder msg = new StringBuilder();
            long receivedTime;
            //遍历msgs数组得到短信的内容
            for (SmsMessage message : msgs) {
                msg.append(message.getDisplayMessageBody());
            }
            //发短信的号码
            phoneNum = msgs[0].getOriginatingAddress();
            //收到的时间
            receivedTime = msgs[0].getTimestampMillis();
            //构造SMS对象
            SMS sms = new SMS(receivedTime, msg.toString(), phoneNum);
            //是否该拦截短信
            if (isShouldBlock(sms)) {
                //将短信存入数据库中
                blockSms(sms);
                //终止广播
                abortBroadcast();
                //显示拦截通知
                showNotification(sms, context);
            }
            //模拟发送短信的测试代码，使用sendbroadcast项目来发送广播
        } else if (intent.getAction() == ACTION_SMS_TEST && BuildConfig.DEBUG) {
            String content = intent.getStringExtra("extra_content");
            String phoneNum = intent.getStringExtra("extra_phone_num");
            long time = intent.getLongExtra("extra_time", 0);
            SMS testSMS = new SMS(time, content, phoneNum);
            if (isShouldBlock(testSMS)) {
                //将短信存入数据库中
                blockSms(testSMS);
                //终止广播
                abortBroadcast();
                //显示拦截通知
                showNotification(testSMS, context);
            }
        }
    }

    /**
     * 通过查询数据库判断是否该拦截短信
     *
     * @param sms
     * @return
     */
    private boolean isShouldBlock(SMS sms) {
        //如果短信号码在黑名单里，进行拦截
        if (mSMSDaoImpl.isPhoneNumExists(sms.getPhoneNum())) {
            return true;

        } else {
            List<Keyword> allKeywords = mSMSDaoImpl.getAllKeywords();
            String keywordStr;
            for (Keyword keyword : allKeywords) {
                keywordStr = keyword.getKeywordStr();
                boolean exists = sms.getSMSInfo().contains(keywordStr);
                if (exists) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 将拦截到的短信保存到数据库
     *
     * @param sms
     */
    private void blockSms(SMS sms) {
        mSMSDaoImpl.insertBlockSMS(sms);
        LogHelper.d(TAG, "sms was blocked " + sms.getPhoneNum() + " " + sms.getSMSInfo());
    }

    /**
     * 从intent中解析短信内容
     *
     * @param intent
     * @return
     */
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

    /**
     * 拦截到短信后进行通知
     *
     * @param sms
     * @param context
     */
    private void showNotification(SMS sms, Context context) {

        NotificationManager notificationManager = (NotificationManager) context.
                getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(context.getString(R.string.new_sms_was_blocked, sms.getPhoneNum()))
                .setContentText(context.getString(R.string.notification_text_content, sms.getPhoneNum()))
                .setContentIntent(pendingIntent);
        Notification notification = builder.build();
        notification.defaults = Notification.DEFAULT_ALL;
        notification.tickerText = context.getString(R.string.notification_text_content, sms.getPhoneNum());

        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(Constants.NOTIFICATION_ID, notification);
    }
}
