package com.chaoyang805.blocksms.bean;

/**
 * Created by chaoyang805 on 2015/8/21.
 * 短信实体类，对应数据库中的blocked_sms表
 */
public class SMS {

    private int mId;
    private long mReceivedTime;
    private String mSMSInfo;
    private String mPhoneNum;
    public SMS(){

    }

    public SMS( long receivedTime, String SMSInfo, String phoneNum){
        mReceivedTime = receivedTime;
        mSMSInfo = SMSInfo;
        mPhoneNum = phoneNum;
    }
    public SMS(int id, long receivedTime, String SMSInfo, String phoneNum) {
        mId = id;
        mReceivedTime = receivedTime;
        mSMSInfo = SMSInfo;
        mPhoneNum = phoneNum;
    }


    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public long getReceivedTime() {
        return mReceivedTime;
    }

    public void setReceivedTime(long receivedTime) {
        mReceivedTime = receivedTime;
    }

    public String getSMSInfo() {
        return mSMSInfo;
    }

    public void setSMSInfo(String SMSInfo) {
        mSMSInfo = SMSInfo;
    }

    public String getPhoneNum() {
        return mPhoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        mPhoneNum = phoneNum;
    }
}
