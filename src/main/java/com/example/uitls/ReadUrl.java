package com.example.uitls;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.example.demo.GuPiao;
import com.example.model.HistoryPriceDo;
import com.example.service.task.MonitorTask;

public class ReadUrl {
	private static Map<String, String> map = new ConcurrentHashMap<>();
	private static Logger logger = LoggerFactory.getLogger("real_time_monitor");
	
	
	
	public static HistoryPriceDo getLastMa20(String title,int type) {
		List<HistoryPriceDo> list=ReadUrl.readUrl(title,type);
		if(list != null && !list.isEmpty()) {
			return list.get(list.size()-1);
		}
		return null;
	}
	
	//http://api.finance.ifeng.com/akmin?scode=sz300073&type=60
	public static List<HistoryPriceDo> readUrl(String number,int type) {
		String url = "http://api.finance.ifeng.com/akmin?scode=" + number+"&type="+type;
		String code = HttpClientUtil.doGet(url);
		if (code == null) {
			logger.warn("http请求数据为空");
			return null;
		}
		List<HistoryPriceDo> list=new ArrayList<HistoryPriceDo>();
		if(JSON.parseObject(code)==null) {
			return null;
		}
		JSONArray  priceList=(JSONArray)JSON.parseObject(code).get("record");
		double ma20_5=0;
		Calendar calendar=Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY,-24);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String name="";
		if(MonitorTask.stockMap.containsKey(number)) {
			name=MonitorTask.stockMap.get(number).getName();
		}
		
		for(int i=0;i<priceList.size();i++) {
			 List<Object> priceObjList=(List<Object>)priceList.get(i);
			 HistoryPriceDo price=new HistoryPriceDo();
			 try {
				 	price.setNumber(number);
				 	price.setName(name);
		            Date date = sdf.parse((String)priceObjList.get(0));
		            price.setDateime(date);
		            price.setKaipanjia(getBigDecimal(priceObjList.get(1)));
		            price.setZuigaojia(getBigDecimal(priceObjList.get(2)));
		            price.setShoupanjia(getBigDecimal(priceObjList.get(3)));
		            price.setZuidijia(getBigDecimal(priceObjList.get(4)));
		            price.setChengjiaoliang(getBigDecimal(priceObjList.get(5)));
		            price.setJiagebiandong(getBigDecimal(priceObjList.get(6)));
		            price.setZhangdiefu(getBigDecimal(priceObjList.get(7)));
		            price.setMa5(getBigDecimal((String)priceObjList.get(8)));
		            price.setMa10(getBigDecimal(priceObjList.get(9)));
		            price.setMa20(getBigDecimal(priceObjList.get(10)));
		            price.setMa5number(getBigDecimal(priceObjList.get(11)));
		            price.setMa10number(getBigDecimal(priceObjList.get(12)));
		            price.setMa20number(getBigDecimal(priceObjList.get(13)));
		            price.setHuanshoulv(getBigDecimal(priceObjList.get(14)));
		            price.setPianlizhi(price.getMa20().divide(price.getShoupanjia(),BigDecimal.ROUND_HALF_UP));
		            if(ma20_5 <=0) {
		            	ma20_5=price.getMa20().doubleValue();
		            }
		            //收盘价比MA20高
		            if(price.getShoupanjia().compareTo(price.getMa20()) > -1) {
		            	if(price.getMa20().doubleValue() - ma20_5>0) {
		            		price.setUp(true);
	            		}
		            }
		            list.add(price);
		        } catch (ParseException e) {
		        	logger.error(e.getMessage(),e);
		        }
		}
		return list;
	}
	public static BigDecimal getBigDecimal(Object obj){
		if(obj instanceof String) {
			return new BigDecimal((String)obj);
		}
		String temp=JSON.toJSONString(obj);
		return new BigDecimal(temp);
	}
	
	/**
	 * 
	 * @param number
	 * @param isTemp   false 获取实时数据
	 * @return
	 */
	public static GuPiao readUrl(String number, boolean isTemp) {
		String url = "http://hq.sinajs.cn/list=" + number;
		String code = HttpClientUtil.doGet(url);
		if (code == null) {
			logger.warn("http请求数据为空");
			return null;
		}

		if (!isTemp) {
			return hanldeData(number, code);
		}

		String temp = "";
		if (map.containsKey(number)) {
			temp = map.get(number);
		}

		if (StringUtils.equalsIgnoreCase(temp, code)) {
			logger.info("数据相同：" + number);
			return null;
		}
		map.put(number, code);
		return hanldeData(number, code);
	}

	public static GuPiao readUrl(final int i, String title, boolean isTemp) {
		String number = String.format("%05d", i);
		String url = "http://hq.sinajs.cn/list=" + title + number;
		String code = HttpClientUtil.doGet(url);
		if (!isTemp) {
			return hanldeData(title + number, code);
		}
		String temp = "";
		if (map.containsKey(title + number)) {
			temp = map.get(title + number);
		}
		if (code != null && !code.equalsIgnoreCase(temp)) {
			map.put(title + number, code);
			return hanldeData(title + number, code);
		}
		return null;
	}

	private static GuPiao hanldeData(String number, String code) {
		if (code.length() > 30) {
			String value = code.split("=")[1];
			value = value.replace("\"", "");
//			System.out.println(value);
			String[] date = value.split(",");
			if (date.length < 32) {
				return null;
			}
			GuPiao gp = new GuPiao(number, date[0], date[1], date[2], date[3], date[4], date[5], date[6], date[7],
					date[8], date[9], date[10], date[11], date[12], date[13], date[14], date[15], date[16], date[17],
					date[18], date[19], date[20], date[21], date[22], date[23], date[24], date[25], date[26], date[27],
					date[28], date[29], date[30], date[31]);
			return gp;
		}
		return null;
	}
}
