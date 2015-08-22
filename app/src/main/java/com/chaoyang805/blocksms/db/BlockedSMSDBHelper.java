package com.chaoyang805.blocksms.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by chaoyang805 on 2015/8/20.
 */
public class BlockedSMSDBHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME_BLOCKED_NUM = "blocked_num";
    public static final String TABLE_NAME_KEYWORDS = "keywords";
    public static final String TABLE_NAME_BLOCKED_SMS = "blocked_sms";

    public static final String COLUMN_NAME_ID = "_id";
    public static final String COLUMN_NAME_PHONE_NUM = "phone_num";
    public static final String COLUMN_NAME_KEYWORD = "keyword";
    public static final String COLUMN_NAME_MSG = "msg";
    public static final String COLUMN_NAME_RECEIVED_TIME = "received_time";



    private static final String CREATE_TABLE_BLOCKED_NUM = "CREATE TABLE blocked_num (" +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "phone_num TEXT NOT NULL)";
    private static final String CREATE_TABLE_KEYWORDS = "CREATE TABLE keywords (" +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "keyword TEXT NOT NULL)";
    private static final String CREATE_TABLE_BLOCKED_SMS = "CREATE TABLE blocked_sms (" +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "phone_num TEXT," +
            "msg TEXT," +
            "received_time TEXT)";
    private static final String DATABASE_NAME = "blocksms.db";
    private static final int DB_VERSION = 1;

    private static BlockedSMSDBHelper sHelper = null;
    private BlockedSMSDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }


    public static BlockedSMSDBHelper getInstance(Context context) {
        if (sHelper == null) {
            sHelper = new BlockedSMSDBHelper(context);
        }
        return sHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_BLOCKED_NUM);
        db.execSQL(CREATE_TABLE_KEYWORDS);
        db.execSQL(CREATE_TABLE_BLOCKED_SMS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
