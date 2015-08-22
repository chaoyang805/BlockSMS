package com.chaoyang805.blocksms.bean;

/**
 * 关键词实体类，对应数据库中的keywords表
 * Created by chaoyang805 on 2015/8/21.
 */
public class Keyword {

    private String mKeyword;
    private int mId;

    public String getKeyword() {
        return mKeyword;
    }

    public void setKeyword(String keyword) {
        mKeyword = keyword;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }
}
