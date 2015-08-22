package com.chaoyang805.blocksms.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.chaoyang805.blocksms.bean.BlockedPhoneNum;
import com.chaoyang805.blocksms.bean.Keyword;
import com.chaoyang805.blocksms.bean.SMS;
import com.chaoyang805.blocksms.utils.Constants;
import com.chaoyang805.blocksms.utils.LogHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 操作数据库的实现类，实现数据库的增删改查
 * Created by chaoyang805 on 2015/8/21.
 */
public class SMSDAOImpl implements SMSDAO {
    /**
     * 数据库帮助类对象
     */
    private BlockedSMSDBHelper mHelper;
    /**
     * 对数据库执行读写操作的对象
     */
    private SQLiteDatabase mDbWrite;
    /**
     * log标签
     */
    private static final String TAG = LogHelper.makeLogTag(SMSDAOImpl.class);

    /**
     * 构造方法
     * @param context
     */
    public SMSDAOImpl(Context context) {
        mHelper = BlockedSMSDBHelper.getInstance(context);
    }

    /**
     * 插入新的号码到blocked_num表
     * @param phoneNum
     */
    @Override
    public void insertPhoneNum(BlockedPhoneNum phoneNum) {
        mDbWrite = mHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Constants.COLUMN_NAME_PHONE_NUM, phoneNum.getPhoneNumStr());
        mDbWrite.insert(Constants.TABLE_NAME_BLOCKED_NUM, null, cv);
        mDbWrite.close();
    }

    /**
     * 插入新的关键字到keyword表
     * @param keyword
     */
    @Override
    public void insertKeyword(Keyword keyword) {
        mDbWrite = mHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Constants.COLUMN_NAME_KEYWORD, keyword.getKeywordStr());
        long column = mDbWrite.insert(Constants.TABLE_NAME_KEYWORDS, null, cv);
        LogHelper.d(TAG, "New Keyword inserted: " + column);
        mDbWrite.close();
    }

    /**
     * 插入新的短信到blocked_sms表
     * @param blockedSMS
     */
    @Override
    public void insertBlockSMS(SMS blockedSMS) {
        mDbWrite = mHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Constants.COLUMN_NAME_MSG, blockedSMS.getSMSInfo());
        cv.put(Constants.COLUMN_NAME_RECEIVED_TIME, blockedSMS.getReceivedTime());
        cv.put(Constants.COLUMN_NAME_PHONE_NUM, blockedSMS.getPhoneNum());
        long result = mDbWrite.insert(Constants.TABLE_NAME_BLOCKED_SMS, null, cv);
        LogHelper.d(TAG, "insert into sms " + result);
        mDbWrite.close();
    }

    /**
     * 从数据库中删除指定的号码
     * @param phoneNum
     */
    @Override
    public void deletePhoneNum(BlockedPhoneNum phoneNum) {
        mDbWrite = mHelper.getWritableDatabase();
        int count = mDbWrite.delete(Constants.TABLE_NAME_BLOCKED_NUM,
                Constants.COLUMN_NAME_ID + " = ?", new String[]{phoneNum.getId() + ""});
        LogHelper.d(TAG, "phone num deleted:" + count);
        mDbWrite.close();
    }

    /**
     * 从数据库中删除指定的关键字
     * @param keyword
     */
    @Override
    public void deleteKeyword(Keyword keyword) {
        mDbWrite = mHelper.getWritableDatabase();
        int deleteCount = mDbWrite.delete(Constants.TABLE_NAME_KEYWORDS,
                Constants.COLUMN_NAME_ID + " = ?", new String[]{keyword.getId()+""});
        LogHelper.d(TAG, "keyword deleted:" + deleteCount);
        mDbWrite.close();
    }

    /**
     * 从数据库中删除指定的短信
     * @param sms
     */
    @Override
    public void deleteSMS(SMS sms) {
        mDbWrite = mHelper.getWritableDatabase();
        //根据短信id删除短信
        mDbWrite.delete(Constants.TABLE_NAME_BLOCKED_SMS,
                Constants.COLUMN_NAME_ID + " = ?", new String[]{sms.getId() + ""});
        mDbWrite.close();
    }

    /**
     * 修改数据库中指定的号码
     * @param oldNum
     * @param newNum
     */
    @Override
    public void updatePhoneNum(String oldNum, String newNum) {
        mDbWrite = mHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Constants.COLUMN_NAME_PHONE_NUM, newNum);
        mDbWrite.update(Constants.TABLE_NAME_BLOCKED_NUM, cv,
                Constants.COLUMN_NAME_PHONE_NUM + " =?", new String[]{oldNum});
        mDbWrite.close();
    }

    /**
     * 修改数据库中指定的关键字
     * @param oldWord
     * @param newWord
     */
    @Override
    public void updateKeyword(String oldWord, String newWord) {
        mDbWrite = mHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Constants.COLUMN_NAME_KEYWORD, newWord);
        int affectedCount = mDbWrite.update(Constants.TABLE_NAME_KEYWORDS, cv,
                Constants.COLUMN_NAME_KEYWORD + " =?", new String[]{oldWord});
        LogHelper.d(TAG, "Keywords updated:" + affectedCount);
        mDbWrite.close();
    }

    /**、
     * 查询出所有的号码
     * @return
     */
    @Override
    public List<BlockedPhoneNum> getAllPhoneNums() {
        mDbWrite = mHelper.getWritableDatabase();
        Cursor c = mDbWrite.query(Constants.TABLE_NAME_BLOCKED_NUM, null, null, null, null, null, null);
        List<BlockedPhoneNum> phoneNums = new ArrayList<>();
        while (c.moveToNext()) {
            BlockedPhoneNum phoneNum = new BlockedPhoneNum();
            phoneNum.setId(c.getInt(c.getColumnIndex(Constants.COLUMN_NAME_ID)));
            phoneNum.setPhoneNumStr(c.getString(c.getColumnIndex(Constants.COLUMN_NAME_PHONE_NUM)));
            phoneNums.add(phoneNum);
        }
        c.close();
        mDbWrite.close();
        return phoneNums;
    }

    /**
     * 查询出所有的关键字
     * @return
     */
    @Override
    public List<Keyword> getAllKeywords() {
        mDbWrite = mHelper.getWritableDatabase();
        Cursor c = mDbWrite.query(Constants.TABLE_NAME_KEYWORDS, null, null, null, null, null, null);
        List<Keyword> keywords = new ArrayList<>();
        while (c.moveToNext()) {
            Keyword keyword = new Keyword();
            keyword.setId(c.getInt(c.getColumnIndex(Constants.COLUMN_NAME_ID)));
            keyword.setKeywordStr(c.getString(c.getColumnIndex(Constants.COLUMN_NAME_KEYWORD)));
            keywords.add(keyword);
        }
        c.close();
        mDbWrite.close();
        return keywords;
    }

    /**
     * 查询出所有的短信
     * @return
     */
    @Override
    public List<SMS> getAllSMS() {
        mDbWrite = mHelper.getWritableDatabase();
        Cursor c = mDbWrite.query(Constants.TABLE_NAME_BLOCKED_SMS,null, null, null, null, null, null);
        List<SMS> smses = new ArrayList<>();
        while (c.moveToNext()) {
            SMS sms = new SMS();
            sms.setId(c.getInt(c.getColumnIndex(Constants.COLUMN_NAME_ID)));
            sms.setPhoneNum(c.getString(c.getColumnIndex(Constants.COLUMN_NAME_PHONE_NUM)));
            sms.setReceivedTime(c.getLong(c.getColumnIndex(Constants.COLUMN_NAME_RECEIVED_TIME)));
            sms.setSMSInfo(c.getString(c.getColumnIndex(Constants.COLUMN_NAME_MSG)));
            smses.add(sms);
        }
        c.close();
        mDbWrite.close();
        return smses;
    }

    /**
     * 通过id获得指定的短信
     * @param smsId
     * @return
     */
    @Override
    public SMS getSMSById(int smsId) {
        mDbWrite = mHelper.getWritableDatabase();
        Cursor c = mDbWrite.query(Constants.TABLE_NAME_BLOCKED_SMS, null,
                Constants.COLUMN_NAME_ID + " = ?", new String[]{smsId + ""}, null, null, null);
        SMS sms = new SMS();
        c.moveToFirst();
        sms.setId(c.getInt(c.getColumnIndex(Constants.COLUMN_NAME_ID)));
        sms.setSMSInfo(c.getString(c.getColumnIndex(Constants.COLUMN_NAME_MSG)));
        sms.setReceivedTime(c.getLong(c.getColumnIndex(Constants.COLUMN_NAME_RECEIVED_TIME)));
        sms.setPhoneNum(c.getString(c.getColumnIndex(Constants.COLUMN_NAME_PHONE_NUM)));
        LogHelper.d(TAG,"receivedTime = "+c.getLong(c.getColumnIndex(Constants.COLUMN_NAME_RECEIVED_TIME)));
        c.close();
        mDbWrite.close();
        return sms;
    }

    /**
     * 判断数据库中是否存在指定的号码
     * @param phoneNumStr
     * @return
     */
    @Override
    public boolean isPhoneNumExists(String phoneNumStr) {
        mDbWrite = mHelper.getWritableDatabase();
        Cursor c = mDbWrite.query(Constants.TABLE_NAME_BLOCKED_NUM, null,
                Constants.COLUMN_NAME_PHONE_NUM + " = ?", new String[]{phoneNumStr}, null, null, null);
        boolean exists = c.moveToNext();
        c.close();
        mDbWrite.close();
        return exists;
    }

    /**
     * 判断数据库中是否存在指定的关键字
     * @param keywordStr
     * @return
     */
    @Override
    public boolean isKeywordExists(String keywordStr) {
        mDbWrite = mHelper.getWritableDatabase();
        Cursor c = mDbWrite.query(Constants.TABLE_NAME_KEYWORDS, null,
                Constants.COLUMN_NAME_KEYWORD + " = ?", new String[]{keywordStr}, null, null, null);
        boolean exists = c.moveToNext();
        c.close();
        mDbWrite.close();
        return exists;
    }
}
