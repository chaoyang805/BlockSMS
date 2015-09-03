package com.chaoyang805.blocksms.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by chaoyang805 on 2015/8/22.
 * 将字符串进行指定格式化的工具类
 */
public class StringFormatUtil {

    private static final String TAG = LogHelper.makeLogTag(StringFormatUtil.class);

    /**
     * 将毫秒的时间格式化
     *
     * @param milliseconds
     * @return
     */
    public static String formatMilliseconds(long milliseconds) {
        Date date = new Date(milliseconds);
        SimpleDateFormat dateFormat;
        Calendar currentCalendar = Calendar.getInstance();
        LogHelper.d(TAG, "date>>>>YEAR:" + date.getYear() + "MONTH:" + date.getMonth() +
                "DAY_OF_MONTH:" + date.getDay());
        LogHelper.d(TAG, "Calendar>>>>YEAR:" + currentCalendar.get(Calendar.YEAR) +
                "MONTH:" + currentCalendar.get(Calendar.MONTH) +
                "DAY_OF_MONTH:" + currentCalendar.get(Calendar.DAY_OF_MONTH));
        //如果是今年的话，则不显示年
        if (date.getYear() + 1900 == currentCalendar.get(Calendar.YEAR)) {
            dateFormat = new SimpleDateFormat("MM月dd日 HH:mm");
            //如果是当天的话，则不显示天
            if (date.getMonth() == currentCalendar.get(Calendar.MONTH) &&
                    date.getDate() == currentCalendar.get(Calendar.DAY_OF_MONTH)) {
                dateFormat = new SimpleDateFormat("HH:mm");
            }
        } else {
            dateFormat = new SimpleDateFormat("yy年MM月dd日 HH:mm");
        }
        return dateFormat.format(new Date(milliseconds));
    }
}
