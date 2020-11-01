package com.example.uitls;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

	
	public static boolean traceTime() {
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
			
			if(belongCalendar(now, beginTime1, endTime1)||belongCalendar(now, beginTime2, endTime2)) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
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
}
