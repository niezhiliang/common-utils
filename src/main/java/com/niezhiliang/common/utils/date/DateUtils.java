package com.niezhiliang.common.utils.date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * @Author NieZhiLiang
 * @Email nzlsgg@163.com
 * @Date 2018/11/26 下午8:36
 */
public class DateUtils {
    private final static Logger logger = LoggerFactory.getLogger(DateUtils.class);
    /**
     * 时间格式化
     * @param date
     * @param formate
     * @return
     */
    public static String formate(Date date,String formate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formate);
        return simpleDateFormat.format(date);
    }

    /**
     *  时间格式化 yyyy-MM-dd HH:mm:ss
     * @param date
     * @return
     */
    public static String formate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }

    /**
     * 计算两个时间相差多少天
     * @param oldDate
     * @param newDate
     * @return
     */
    public static int differentDays(Date oldDate,Date newDate) {
        float days = (newDate.getTime() - oldDate.getTime()) / (1000*3600*24);
        return (int) Math.ceil(days);
    }

    /**
     * 计算两个时间相差多少小时
     * @param oldDate
     * @param newDate
     * @return
     */
    public static int differentHours(Date oldDate,Date newDate) {
        float hours = (newDate.getTime() - oldDate.getTime()) / (1000*3600);
        return (int) Math.ceil(hours);
    }

    /**
     * 计算两个时间相差多少分钟
     * @param oldDate
     * @param newDate
     * @return
     */
    public static int differentMinute(Date oldDate,Date newDate) {
        float minute = (newDate.getTime() - oldDate.getTime()) / (1000*60);
        return (int) Math.ceil(minute);
    }

    /**
     * 计算两个时间相差多少秒
     * @param oldDate
     * @param newDate
     * @return
     */
    public static int differentSecond(Date oldDate,Date newDate) {
        float second = (newDate.getTime() - oldDate.getTime()) / (1000);
        return (int) Math.ceil(second);
    }

    /**
     * 判断两个时间大小
     * @param oldDate
     * @param newDate
     * @return
     */
    public static boolean compareTime(Date oldDate,Date newDate) {
        return oldDate.getTime() > newDate.getTime();
    }

    /**
     * 格式化时间字符串转换成Date
     * @param formateDate
     * @return
     */
    public static Date formateToDate(String formateDate) throws ParseException {
         SimpleDateFormat dateFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return dateFormat.parse(formateDate);//将字符串转换成时间
    }

    /**
     * 格式化时间字符串转换成时间戳
     * @param formateDate
     * @return
     */
    public static long formateToTimeMillis(String formateDate) throws ParseException {
        return formateToDate(formateDate).getTime();
    }
}
