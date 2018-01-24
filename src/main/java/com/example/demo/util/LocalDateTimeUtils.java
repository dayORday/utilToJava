package com.example.demo.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalUnit;
import java.util.Date;

/**
 * 
 * 时间工具类  
 * 
 * @version 1.0 
 * @since JDK1.7 
 * @author zhangjingtao 
 * @company 洛阳艾克科技有限公司 
 * @copyright (c) 2018 LuoYang ARC Co'Ltd Inc. All rights reserved. 
 * @date 2018年1月24日 下午2:11:59
 */

public class LocalDateTimeUtils {

	//获取当前时间的LocalDateTime对象
    //LocalDateTime.now();

    //根据年月日构建LocalDateTime
    //LocalDateTime.of();年 月 日 时 分 秒 纳秒

    //比较日期先后
    //LocalDateTime.now().isBefore(),
    //LocalDateTime.now().isAfter(),

    /**
     * Date转换为LocalDateTime
     * @param date
     * @return 
     * @author zhangjingtao 
     * @date 2018年1月24日 下午2:14:39
     */
    public static LocalDateTime convertDateToLDT(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
     * LocalDateTime转换为Date
     * @param time
     * @return 
     * 
     * @author zhangjingtao 
     * @date 2018年1月24日 下午2:15:09
     */
    public static Date convertLDTToDate(LocalDateTime time) {
        return Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
    }


    /**
     * 获取指定日期的毫秒
     * @param time
     * @return 
     * 
     * @author zhangjingtao 
     * @date 2018年1月24日 下午2:15:20
     */
    public static Long getMilliByTime(LocalDateTime time) {
        return time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    //获取指定日期的秒
    public static Long getSecondsByTime(LocalDateTime time) {
        return time.atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
    }

    /**
     * 获取指定时间的指定格式
     * @param time
     * @param pattern  yyyy-MM-dd HH:mm:ss
     * @return 
     * 
     * @author zhangjingtao 
     * @date 2018年1月24日 下午2:15:36
     */
    public static String formatTime(LocalDateTime time,String pattern) {
        return time.format(DateTimeFormatter.ofPattern(pattern));
    }

	  /**
	   * 获取当前时间的指定格式
	   * @param pattern
	   * @return 
	   * 
	   * @author zhangjingtao 
	   * @date 2018年1月24日 下午2:16:25
	   */
    public static String formatNow(String pattern) {
        return  formatTime(LocalDateTime.now(), pattern);
    }

    /**
     * 日期加上一个数,根据field不同加不同值,field为ChronoUnit.*
     * @param time
     * @param number
     * @param field
     * @return 
     * 
     * @author zhangjingtao 
     * @date 2018年1月24日 下午2:16:39
     */
    public static LocalDateTime plus(LocalDateTime time, long number, TemporalUnit field) {
        return time.plus(number, field);
    }

    /**
     * 日期减去一个数,根据field不同减不同值,field参数为ChronoUnit.*
     * 
     * @param time
     * @param number
     * @param field
     * @return 
     * @author zhangjingtao 
     * @date 2018年1月24日 下午2:18:13
     */
    public static LocalDateTime minu(LocalDateTime time, long number, TemporalUnit field){
        return time.minus(number,field);
    }

    /**
     * 获取两个日期的差  field参数为ChronoUnit.*
     * @param startTime
     * @param endTime
     * @param field  单位(年月日时分秒)
     * @return
     */
    public static long betweenTwoTime(LocalDateTime startTime, LocalDateTime endTime, ChronoUnit field) {
        Period period = Period.between(LocalDate.from(startTime), LocalDate.from(endTime));
        if (field == ChronoUnit.YEARS) {
        	return period.getYears();
        }
        if (field == ChronoUnit.MONTHS) {
        	return period.getYears() * 12 + period.getMonths();
        }
        return field.between(startTime, endTime);
    }

    /**
     * 获取一天的开始时间，2017,7,22 00:00
     * 
     * @param time
     * @return 
     * 
     * @author zhangjingtao 
     * @date 2018年1月24日 下午2:27:17
     */
    public static LocalDateTime getDayStart(LocalDateTime time) {
        return time.withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
    }

      /**
       * 获取一天的结束时间，2017,7,22 23:59:59.999999999
       * @param time
       * @return 
       * 
       * @author zhangjingtao 
       * @date 2018年1月24日 下午2:27:45
       */
    public static LocalDateTime getDayEnd(LocalDateTime time) {
        return time.withHour(23)
                .withMinute(59)
                .withSecond(59)
                .withNano(999999999);
    }
    /**
     * 
     * 毫秒值转localDateTime
     * @return 
     * @author zhangjingtao 
     * @date 2018年1月24日 下午2:32:33
     */
    public static LocalDateTime converLDTTONano(Long value) {
    	LocalDateTime time = LocalDateTime.ofInstant(Instant.ofEpochMilli(value), ZoneId.systemDefault());  
    	return time;
    }
    
    /**
     * 获取17年的1月的第一个周一
     * 
     * @return 
     * 
     * @author zhangjingtao 
     * @date 2018年1月24日 下午2:42:54
     */
    public static LocalDate getMon() {
    	LocalDate firstMondayOf2018 = LocalDate.parse("2018-01-01").with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));
    	return firstMondayOf2018;
    }
    
    public static void main(String[] args) {
    	LocalDateTime past = LocalDateTime.of(2015, 6, 8, 12, 30,30);
    	long l= betweenTwoTime(past,LocalDateTime.now(),ChronoUnit.MILLIS);
    	System.out.println(961*24*60*60*1000);
    	System.out.println(l);
    	System.out.println(getDayEnd(past).format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
    	System.out.println(converLDTTONano(83037812730L).toString());
    	System.out.println("-----------------------------------------");
    	System.out.println(getMon());
    }
}

