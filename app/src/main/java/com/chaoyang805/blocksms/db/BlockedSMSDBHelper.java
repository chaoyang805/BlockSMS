package com.chaoyang805.blocksms.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by chaoyang805 on 2015/8/20.
 * 创建数据库的帮助类
 */
public class BlockedSMSDBHelper extends SQLiteOpenHelper {


    /**
     * 创建拦截号码表的语句
     */
    private static final String CREATE_TABLE_BLOCKED_NUM = "CREATE TABLE blocked_num (" +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "phone_num TEXT NOT NULL)";
    /**
     * 创建关键词表的建表语句
     */
    private static final String CREATE_TABLE_KEYWORDS = "CREATE TABLE keywords (" +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "keyword TEXT NOT NULL)";
    /**
     * 创建拦截到的短信的建表语句
     */
    private static final String CREATE_TABLE_BLOCKED_SMS = "CREATE TABLE blocked_sms (" +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "phone_num TEXT," +
            "msg TEXT," +
            "received_time TEXT)";
    /**
     * 删除blocked_num表的语句
     */
    private static final String DROP_TABLE_BLOCKED_NUM = "DROP TABLE IF EXISTS blocked_num";
    /**
     * 删除keywords表的语句
     */
    private static final String DROP_TABLE_KEYWORDS = "DROP TABLE IF EXISTS keywords";
    /**
     * 删除blocked_sms表的语句
     */
    private static final String DROP_TABLE_BLOCKED_SMS = "DROP TABLE IF EXISTS blocked_sms";
    /**
     * 数据库的文件名
     */
    private static final String DATABASE_NAME = "blocksms.db";
    /**
     * 数据库版本
     */
    private static final int DB_VERSION = 1;
    /**
     * 该类的静态实例
     */
    private static BlockedSMSDBHelper sHelper = null;

    /**
     * 私有构造方法
     * @param context
     */
    private BlockedSMSDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }

    /**
     * 获得该类静态实例的方法
     * @param context
     * @return
     */
    public static BlockedSMSDBHelper getInstance(Context context) {
        if (sHelper == null) {
            sHelper = new BlockedSMSDBHelper(context);
        }
        return sHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建三张表
        db.execSQL(CREATE_TABLE_BLOCKED_NUM);
        db.execSQL(CREATE_TABLE_KEYWORDS);
        db.execSQL(CREATE_TABLE_BLOCKED_SMS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //为避免数据库冲突，先删掉表再新建，实际开发时根据相应数据库的具体变化进行操作
        db.execSQL(DROP_TABLE_BLOCKED_NUM);
        db.execSQL(DROP_TABLE_BLOCKED_SMS);
        db.execSQL(DROP_TABLE_KEYWORDS);

        db.execSQL(CREATE_TABLE_BLOCKED_NUM);
        db.execSQL(CREATE_TABLE_KEYWORDS);
        db.execSQL(CREATE_TABLE_BLOCKED_SMS);

    }
}
