package com.example.uitls;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RedisKeyUtil {

	private final static SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMdd");
	
	public static String getStockName(String number) {
		return number;
	}
	
	//实时通知降噪
	public static String getRealTimeNotify(String number) {
		return "realtime_"+dateformat.format(new Date())+"_"+number;
	}
	
	//个股波段分析
	public static String getBoduanNotify(String number,String appId) {
		return "boduan_"+dateformat.format(new Date())+"_"+number+"_"+appId;
	}
	
	//上一个趋势指标
	public static String getLastHistoryPrice(String number,String today) {
		return "last_history_price_"+today+"_"+number;
	}

	//实时状态判断
	public static String getRealTimeStatus(String number) {
		return "realtime_status_"+dateformat.format(new Date())+"_"+number;
	}
}
