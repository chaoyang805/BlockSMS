package com.chaoyang805.blocksms.bean;

/**
 * Created by chaoyang805 on 2015/8/21.
 * 黑名单里的号码实体类，对应数据库中的blocked_num表
 */
public class BlockedPhoneNum {

    private int mId;
    private String mPhoneNum;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getPhoneNumStr() {
        return mPhoneNum;
    }

    public void setPhoneNumStr(String phoneNum) {
        mPhoneNum = phoneNum;
    }

    @Override
    public String toString() {
        return getPhoneNumStr();
    }
}
