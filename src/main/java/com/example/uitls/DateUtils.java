package com.example.uitls;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.example.model.HolidayDo;

public class DateUtils {
	private static final SimpleDateFormat DF_YYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
	private static final SimpleDateFormat DF_YYYYMMDD = new SimpleDateFormat("yyyyMMdd");// 设置日期格式
	private static final SimpleDateFormat DF_YYYYMMDDHHmm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
	private static final SimpleDateFormat DF_YYYYMMDDHHMM_NUMBER = new SimpleDateFormat("yyyyMMddHHmm");// 设置日期格式
	private static final SimpleDateFormat DF_YYYYMMDD_NUMBER = new SimpleDateFormat("yyyyMMdd");// 设置日期格式
	
	public static String getToday() {
		return DF_YYYYMMDD.format(new Date());
	}
	
	
	public static boolean traceTime(List<HolidayDo> list) {
		SimpleDateFormat df = new SimpleDateFormat("HH:mm");// 设置日期格式
		Date now = null;
		Date beginTime1 = null;
		Date endTime1 = null;
		Date beginTime2 = null;
		Date endTime2 = null;
		try {
			now = df.parse(df.format(new Date()));
			beginTime1 = df.parse("09:15");
			endTime1 = df.parse("11:31");
			
			beginTime2 = df.parse("13:00");
			endTime2 = df.parse("15:01");
			
			Calendar today = Calendar.getInstance();
			if(today.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY ||  today.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){
				return false;
			}
			for(HolidayDo holiday:list) {
				if(isSameDay(today.getTime(),holiday.getHoliday())) {
					return false;
				}
			}
			
			if(belongCalendar(now, beginTime1, endTime1)||belongCalendar(now, beginTime2, endTime2)) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean isSameDay(Date unKownDate) {
		String temp=DF_YYYYMMDD.format(unKownDate);
		String nowtemp=DF_YYYYMMDD.format(new Date());
		if (StringUtils.equalsIgnoreCase(nowtemp, temp)) {
			return true;
		} else {
			return false;
		}
	}
	public static boolean isSameDay(Date day1,Date day2) {
		String temp=DF_YYYYMMDD.format(day1);
		String nowtemp=DF_YYYYMMDD.format(day2);
		if (StringUtils.equalsIgnoreCase(nowtemp, temp)) {
			return true;
		} else {
			return false;
		}
	}
	
	public static Date  getDateForString(String day,String time) {
		try {
			return DF_YYYYMMDDHHmm.parse(day+" "+time);
		}catch (Exception e) {
			return new Date();
		}
	}
	
	
	public static Date getDateForYYYYMMDDHHMM_NUMBER(String day) {
		try {
			return DF_YYYYMMDDHHMM_NUMBER.parse(day);
		}catch (Exception e) {
			return new Date();
		}
	}
	public static Date getDateForYYYYMMDDHHMM(String day) {
		try {
			return DF_YYYYMMDDHHmm.parse(day);
		}catch (Exception e) {
			return new Date();
		}
	}
	public static Date getDateForYYYYMMDD(String day) {
		try {
			return DF_YYYY_MM_DD.parse(day);
		}catch (Exception e) {
			return new Date();
		}
	}
	public static String getDateForYYYYMMDDByDate(Date day) {
		try {
			return DF_YYYYMMDD_NUMBER.format(day);
		}catch (Exception e) {
		}
		return "";
	}
	
	public static String getDateForYYYYMMDDHHMMByDate(Date day) {
		try {
			return DF_YYYYMMDDHHMM_NUMBER.format(day);
		}catch (Exception e) {
		}
		return "";
	}
	
	public static Long getDefDays(Date beginTime, Date endTime,List<HolidayDo> list) {
		Calendar day1 = Calendar.getInstance();
		Calendar day2 = Calendar.getInstance();
		try {
			day1.setTime(DF_YYYYMMDD.parse(DF_YYYYMMDD.format(beginTime)));
			day2.setTime(DF_YYYYMMDD.parse(DF_YYYYMMDD.format(endTime)));
			long temp=(day2.getTimeInMillis()-day1.getTimeInMillis())/(1000 * 60 * 60 *24);
			long day=0;
			if(list==null) {
				list=new ArrayList<HolidayDo>();
			}
			for(long i=0;i<temp;i++) {
				day1.add(Calendar.DAY_OF_WEEK, 1);
				if(day1.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY ||  day1.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){
					day++;
					continue;
				}
				for(HolidayDo holiday:list) {
					if(isSameDay(day1.getTime(),holiday.getHoliday())) {
						day++;
						continue;
					}
				}
			}
			if(temp-day<0) {
				return 1L;
			}
			return temp-day;
		} catch (ParseException e) {
			System.out.println(JSON.toJSONString(beginTime));
			System.out.println(JSON.toJSONString(endTime));
			System.out.println(JSON.toJSONString(list));
			e.printStackTrace();
		}
		return 0L;
	}
	
	
	public static boolean belongCalendar(Date nowTime, Date beginTime, Date endTime) {
		Calendar date = Calendar.getInstance();
		date.setTime(nowTime);
		int hour=date.get(Calendar.DAY_OF_WEEK);
		
		if(hour == Calendar.SATURDAY || hour == Calendar.SUNDAY) {
			return false;
		}
 
		Calendar begin = Calendar.getInstance();
		begin.setTime(beginTime);
 
		Calendar end = Calendar.getInstance();
		end.setTime(endTime);
 
		if (date.after(begin) && date.before(end)) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean belongCalendar2(Date nowTime, Date beginTime, Date endTime) {
		Calendar date = Calendar.getInstance();
		date.setTime(nowTime);
		int hour=date.get(Calendar.DAY_OF_WEEK);
		
		if(hour == Calendar.SATURDAY || hour == Calendar.SUNDAY) {
			return false;
		}
 
		Calendar begin = Calendar.getInstance();
		begin.setTime(beginTime);
 
		Calendar end = Calendar.getInstance();
		end.setTime(endTime);
 
		if(date.getTimeInMillis() >= begin.getTimeInMillis() && date.getTimeInMillis()<= end.getTimeInMillis()) {
			return true;
		}
			return false;
	}
}
