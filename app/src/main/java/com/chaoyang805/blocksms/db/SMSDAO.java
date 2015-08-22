package com.chaoyang805.blocksms.db;

import com.chaoyang805.blocksms.bean.BlockedPhoneNum;
import com.chaoyang805.blocksms.bean.Keyword;
import com.chaoyang805.blocksms.bean.SMS;

import java.util.List;

/**
 * 操作数据库的接口
 * Created by chaoyang805 on 2015/8/21.
 */
public interface SMSDAO {

    /**
     * 插入新的号码到黑名单
     * @param phoneNum
     */
    public void insertPhoneNum(BlockedPhoneNum phoneNum);

    /**
     * 插入新的关键字
     * @param keyword
     */
    public void insertKeyword(Keyword keyword);

    /**
     * 插入新的拦截信息
     * @param blockedSMS
     */
    public void insertBlockSMS(SMS blockedSMS);


    /**
     * 删除黑名单里的号码
     * @param phoneNum
     */
    public void deletePhoneNum(BlockedPhoneNum phoneNum);

    /**
     * 删除黑名单里的关键字
     * @param keyword
     */
    public void deleteKeyword(Keyword keyword);

    /**
     * 删除信息
     * @param sms
     */
    public void deleteSMS(SMS sms);

    /**
     * 根据旧的号码更改号码
     * @param oldNum
     * @param newNum
     */
    public void updatePhoneNum(String oldNum, String newNum);

    /**
     * 根据旧的关键字更改关键字
     * @param oldWord
     * @param newWord
     */
    public void updateKeyword(String oldWord, String newWord);

    /**
     * 获取黑名单里的所有号码
     * @return
     */
    public List<BlockedPhoneNum> getAllPhoneNums();

    /**
     * 获得所有的关键字
     * @return
     */
    public List<Keyword> getAllKeywords();

    /**
     * 获得所有的短信
     * @return
     */
    public List<SMS> getAllSMS();

    /**
     * 查询号码是否已经在黑名单里
     * @param phoneNum
     * @return
     */
    public boolean isPhoneNumExists(BlockedPhoneNum phoneNum);

    /**
     * 查询关键字是否已经在黑名单里
     * @param keyword
     * @return
     */
    public boolean isKeywordExists(Keyword keyword);
}
