package com.example.uitls;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

public class ReadUrl {
	private static Map<String, String> map = new ConcurrentHashMap<>();
	private static Logger logger = LoggerFactory.getLogger("real_time_monitor");
	
	private static ConcurrentHashMap<String, BigDecimal> maxPriceMap=new ConcurrentHashMap<String, BigDecimal>();
	private static ConcurrentHashMap<String, BigDecimal> minPriceMap=new ConcurrentHashMap<String, BigDecimal>();
	
	public static void main(String[] args) {
		List<HistoryPriceDo> list=ReadUrl.readUrl("sz300073",60);
		logger.info(JSON.toJSONString(list));
	}
	
	
	public static HistoryPriceDo getLastMa20(String title,int type) {
		List<HistoryPriceDo> list=ReadUrl.readUrl(title,type);
		if(list != null && !list.isEmpty()) {
			return list.get(list.size()-1);
		}
		return null;
	}
	
	//http://api.finance.ifeng.com/akmin?scode=sz300073&type=60
	public static List<HistoryPriceDo> readUrl(String title,int type) {
		String url = "http://api.finance.ifeng.com/akmin?scode=" + title+"&type="+type;
		String code = HttpClientUtil.doGet(url);
		if (code == null) {
			logger.warn("http请求数据为空");
			return null;
		}
		List<HistoryPriceDo> list=new ArrayList<HistoryPriceDo>();
		JSONArray  priceList=(JSONArray)JSON.parseObject(code).get("record");
		int chuangeValue=0;
		int powerValue=0;
		int buyCount=0;
		int sellCount=0;
		int num=0;
		double total=10000;
		for(int i=0;i<priceList.size();i++) {
			 List<Object> priceObjList=(List<Object>)priceList.get(i);
			 HistoryPriceDo price=new HistoryPriceDo();
			 try {
				 	price.setNumber(title);
				    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
		            BigDecimal max=price.getZuigaojia();
		            BigDecimal tempMax=maxPriceMap.get(title);
		            BigDecimal min=price.getZuidijia();
		            BigDecimal tempMin=minPriceMap.get(title);
		            if(null == tempMax || tempMax.compareTo(max)< 1) {
		            	maxPriceMap.put(title, max);
		            }
		            if(null == tempMin || tempMin.compareTo(min)> -1) {
		            	minPriceMap.put(title, min);
		            }
		            DecimalFormat df = new DecimalFormat("0.00");
		            //收盘价比MA20高
		            if(price.getShoupanjia().compareTo(price.getMa20()) > -1) {
		            	buyCount++;
		            	sellCount=0;
		            	if(buyCount == 1) {
		            		num=(int) Math.floor(total/(price.getShoupanjia().doubleValue()*100));
		            		total=total-price.getShoupanjia().doubleValue()*100*num;
		            		logger.info("编号："+price.getNumber()+" 买入点!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+sdf.format(price.getDateime())+" 价格："+price.getShoupanjia()+" 数量："+(num*100) +" 余额:"+df.format(total));
		            	}
		            	powerValue++;
		            	//logger.info(sdf.format(price.getDateime())+":"+price.getNumber()+" "+price.getShoupanjia()+">="+price.getMa20()+"=趋势上升   "+"当前能量值："+powerValue+" 偏移量："+price.getPianlizhi());
		            	price.setUp(true);
		            }else {
		            	sellCount++;
		            	buyCount=0;
		            	if(sellCount == 1) {
		            		total=total+price.getShoupanjia().doubleValue()*100*num;
		            		double win=total-10000;
		            		num=0;
		            		logger.info("编号："+price.getNumber()+" 卖出点!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+sdf.format(price.getDateime())+" 价格："+df.format(price.getShoupanjia())+" 余额:"+df.format(total)+" 盈利："+df.format(win)+" 盈利率："+df.format(win/100)+"%");
		            		total=10000;
		            		
		            	}
		            	powerValue--;
		            	//logger.info(sdf.format(price.getDateime())+":"+price.getNumber()+" "+price.getShoupanjia()+"<"+price.getMa20()+"=趋势下降  "+"当前能量值："+powerValue+" 偏移量："+price.getPianlizhi());
		            }
		            price.setPowerValue(powerValue);
		            chuangeValue=powerValue;
		            list.add(price);
		        } catch (ParseException e) {
		        	logger.error(e.getMessage(),e);
		        }finally {
				}
		}
		for(HistoryPriceDo price:list) {
			BigDecimal tempMax=maxPriceMap.get(title);
			if(tempMax!=null) {
				price.setYaliwei(tempMax);
			}
			BigDecimal tempMin=minPriceMap.get(title);
			if(tempMin!=null) {
				price.setZhichengwei(tempMin);
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
	
	
	public static GuPiao readUrl(String title, boolean isTemp) {
		String url = "http://hq.sinajs.cn/list=" + title;
		String code = HttpClientUtil.doGet(url);
		if (code == null) {
			logger.warn("http请求数据为空");
			return null;
		}

		if (!isTemp) {
			return hanldeData(title, code);
		}

		String temp = "";
		if (map.containsKey(title)) {
			temp = map.get(title);
		}

		if (StringUtils.equalsIgnoreCase(temp, code)) {
			logger.info("数据相同：" + title);
			return null;
		}
		map.put(title, code);
		return hanldeData(title, code);
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
