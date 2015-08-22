package com.chaoyang805.blocksms.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.chaoyang805.blocksms.bean.BlockedPhoneNum;
import com.chaoyang805.blocksms.bean.Keyword;
import com.chaoyang805.blocksms.bean.SMS;
import com.chaoyang805.blocksms.utils.LogHelper;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by chaoyang805 on 2015/8/21.
 */
public class SMSDAOImpl implements SMSDAO {

    private BlockedSMSDBHelper mHelper;

    private SQLiteDatabase mDbWrite;
    private static final String TAG = LogHelper.makeLogTag(SMSDAOImpl.class);

    public SMSDAOImpl(Context context) {
        mHelper = BlockedSMSDBHelper.getInstance(context);
    }

    @Override
    public void insertPhoneNum(BlockedPhoneNum phoneNum) {
        mDbWrite = mHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(BlockedSMSDBHelper.COLUMN_NAME_PHONE_NUM, phoneNum.getPhoneNum());
        mDbWrite.insert(BlockedSMSDBHelper.TABLE_NAME_BLOCKED_NUM, null, cv);
        mDbWrite.close();
    }

    @Override
    public void insertKeyword(Keyword keyword) {
        mDbWrite = mHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(BlockedSMSDBHelper.COLUMN_NAME_KEYWORD, keyword.getKeyword());
        mDbWrite.insert(BlockedSMSDBHelper.TABLE_NAME_KEYWORDS, null, cv);
        mDbWrite.close();
    }

    @Override
    public void insertBlockSMS(SMS blockedSMS) {
        mDbWrite = mHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(BlockedSMSDBHelper.COLUMN_NAME_MSG, blockedSMS.getSMSInfo());
        cv.put(BlockedSMSDBHelper.COLUMN_NAME_RECEIVED_TIME, blockedSMS.getReceivedTime());
        cv.put(BlockedSMSDBHelper.COLUMN_NAME_PHONE_NUM, blockedSMS.getPhoneNum());
        long result = mDbWrite.insert(BlockedSMSDBHelper.TABLE_NAME_BLOCKED_SMS, null, cv);
        LogHelper.d(TAG, "insert into sms " + result);
        mDbWrite.close();
    }

    @Override
    public void deletePhoneNum(BlockedPhoneNum phoneNum) {
        mDbWrite = mHelper.getWritableDatabase();
        mDbWrite.delete(BlockedSMSDBHelper.TABLE_NAME_BLOCKED_NUM,
                BlockedSMSDBHelper.COLUMN_NAME_PHONE_NUM + " = ?", new String[]{phoneNum.getPhoneNum()});
        mDbWrite.close();
    }

    @Override
    public void deleteKeyword(Keyword keyword) {
        mDbWrite = mHelper.getWritableDatabase();
        mDbWrite.delete(BlockedSMSDBHelper.TABLE_NAME_KEYWORDS,
                BlockedSMSDBHelper.COLUMN_NAME_KEYWORD + " = ?", new String[]{keyword.getKeyword()});
        mDbWrite.close();
    }

    @Override
    public void deleteSMS(SMS sms) {
        mDbWrite = mHelper.getWritableDatabase();
        //根据短信收到的时间删除短信
        mDbWrite.delete(BlockedSMSDBHelper.TABLE_NAME_BLOCKED_SMS,
                BlockedSMSDBHelper.COLUMN_NAME_RECEIVED_TIME + " = ?", new String[]{sms.getReceivedTime() + ""});
        mDbWrite.close();
    }

    @Override
    public void updatePhoneNum(String oldNum, String newNum) {
        mDbWrite = mHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(BlockedSMSDBHelper.COLUMN_NAME_PHONE_NUM, newNum);
        mDbWrite.update(BlockedSMSDBHelper.TABLE_NAME_BLOCKED_NUM, cv,
                BlockedSMSDBHelper.COLUMN_NAME_PHONE_NUM + " =?", new String[]{oldNum});
        mDbWrite.close();
    }

    @Override
    public void updateKeyword(String oldWord, String newWord) {
        mDbWrite = mHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(BlockedSMSDBHelper.COLUMN_NAME_KEYWORD, newWord);
        mDbWrite.update(BlockedSMSDBHelper.TABLE_NAME_KEYWORDS, cv,
                BlockedSMSDBHelper.COLUMN_NAME_KEYWORD + " =?", new String[]{oldWord});
        mDbWrite.close();
    }

    @Override
    public List<BlockedPhoneNum> getAllPhoneNums() {
        mDbWrite = mHelper.getWritableDatabase();
        Cursor c = mDbWrite.query(BlockedSMSDBHelper.TABLE_NAME_BLOCKED_NUM, null, null, null, null, null, null);
        List<BlockedPhoneNum> phoneNums = new ArrayList<>();
        while (c.moveToNext()) {
            BlockedPhoneNum phoneNum = new BlockedPhoneNum();
            phoneNum.setId(c.getInt(c.getColumnIndex(BlockedSMSDBHelper.COLUMN_NAME_ID)));
            phoneNum.setPhoneNum(c.getString(c.getColumnIndex(BlockedSMSDBHelper.COLUMN_NAME_PHONE_NUM)));
            phoneNums.add(phoneNum);
        }
        c.close();
        mDbWrite.close();
        return phoneNums;
    }

    @Override
    public List<Keyword> getAllKeywords() {
        mDbWrite = mHelper.getWritableDatabase();
        Cursor c = mDbWrite.query(BlockedSMSDBHelper.TABLE_NAME_KEYWORDS, null, null, null, null, null, null);
        List<Keyword> keywords = new ArrayList<>();
        while (c.moveToNext()) {
            Keyword keyword = new Keyword();
            keyword.setId(c.getInt(c.getColumnIndex(BlockedSMSDBHelper.COLUMN_NAME_ID)));
            keyword.setKeyword(c.getString(c.getColumnIndex(BlockedSMSDBHelper.COLUMN_NAME_KEYWORD)));
            keywords.add(keyword);
        }
        c.close();
        mDbWrite.close();
        return keywords;
    }

    @Override
    public List<SMS> getAllSMS() {
        mDbWrite = mHelper.getWritableDatabase();
        Cursor c = mDbWrite.query(BlockedSMSDBHelper.TABLE_NAME_BLOCKED_SMS,null, null, null, null, null, null);
        List<SMS> smses = new ArrayList<>();
        while (c.moveToNext()) {
            SMS sms = new SMS();
            sms.setId(c.getInt(c.getColumnIndex(BlockedSMSDBHelper.COLUMN_NAME_ID)));
            sms.setPhoneNum(c.getString(c.getColumnIndex(BlockedSMSDBHelper.COLUMN_NAME_PHONE_NUM)));
            sms.setReceivedTime(c.getLong(c.getColumnIndex(BlockedSMSDBHelper.COLUMN_NAME_RECEIVED_TIME)));
            sms.setSMSInfo(c.getString(c.getColumnIndex(BlockedSMSDBHelper.COLUMN_NAME_MSG)));
            smses.add(sms);
        }
        c.close();
        mDbWrite.close();
        return smses;
    }

    @Override
    public boolean isPhoneNumExists(BlockedPhoneNum phoneNum) {
        mDbWrite = mHelper.getWritableDatabase();
        Cursor c = mDbWrite.query(BlockedSMSDBHelper.TABLE_NAME_BLOCKED_NUM, null,
                BlockedSMSDBHelper.COLUMN_NAME_PHONE_NUM + " = ?", new String[]{phoneNum.getPhoneNum()}, null, null, null);
        boolean exists = c.moveToNext();
        c.close();
        mDbWrite.close();
        return exists;
    }

    @Override
    public boolean isKeywordExists(Keyword keyword) {
        mDbWrite = mHelper.getWritableDatabase();
        Cursor c = mDbWrite.query(BlockedSMSDBHelper.TABLE_NAME_KEYWORDS, null,
                BlockedSMSDBHelper.COLUMN_NAME_KEYWORD + " = ?", new String[]{keyword.getKeyword()}, null, null, null);
        boolean exists = c.moveToNext();
        c.close();
        mDbWrite.close();
        return exists;
    }
}
