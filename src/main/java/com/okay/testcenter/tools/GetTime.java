package com.okay.testcenter.tools;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;

public class GetTime {

    private static final Logger logger = LoggerFactory.getLogger(GetTime.class);


    public static String getTime() {

        String time = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(new Date());

        return time;
    }

    public static String getTime(Long addTime) {

        String time = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(System.currentTimeMillis() + addTime);

        return time;
    }

    /**
     * @return 获取年月日
     */
    public static String getYmd() {

        String time = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new Date());

        return time;
    }


    /**
     * @return 获取年月日
     */
    public static Date getYmdForDate() {

        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, 1);
        return c.getTime();
    }

    public static String getTimeFormat() {

        String time = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        return time;
    }


    //获取时间差方法
    public static String getTotalTime(Date currentTime, Date firstTime) {
        long diff = currentTime.getTime() - firstTime.getTime();//这样得到的差值是微秒级别
        Calendar currentTimes = dataToCalendar(currentTime);//当前系统时间转Calendar类型
        Calendar firstTimes = dataToCalendar(firstTime);//查询的数据时间转Calendar类型
        int year = currentTimes.get(Calendar.YEAR) - firstTimes.get(Calendar.YEAR);//获取年
        int month = currentTimes.get(Calendar.MONTH) - firstTimes.get(Calendar.MONTH);
        int day = currentTimes.get(Calendar.DAY_OF_MONTH) - firstTimes.get(Calendar.DAY_OF_MONTH);
        if (day < 0) {
            month -= 1;
            currentTimes.add(Calendar.MONTH, -1);
            day = day + currentTimes.getActualMaximum(Calendar.DAY_OF_MONTH);//获取日
        }
        if (month < 0) {
            month = (month + 12) % 12;//获取月
            year--;
        }
        long days = diff / (1000 * 60 * 60 * 24);
        long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60); //获取时
        long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);  //获取分钟
        long s = (diff / 1000 - days * 24 * 60 * 60 - hours * 60 * 60 - minutes * 60);//获取秒
        String CountTime =  days + "天" + hours + "小时" + minutes + "分" + s + "秒";
        return CountTime;
    }

    //Date类型转Calendar类型
    public static Calendar dataToCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }


}
