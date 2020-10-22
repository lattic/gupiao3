package com.example.ai;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.example.model.HistoryPriceDo;
import com.example.model.MockLog;
import com.example.uitls.DingTalkRobotHTTPUtil;
import com.example.uitls.ReadUrl;

public class MockDeal {
	private static Logger logger = LoggerFactory.getLogger("real_time_monitor");
	private static ConcurrentHashMap<String, Boolean> buyPorintMap = new ConcurrentHashMap<String, Boolean>();
	private static ConcurrentHashMap<String, BigDecimal> maxPriceMap = new ConcurrentHashMap<String, BigDecimal>();
	private static ConcurrentHashMap<String, BigDecimal> minPriceMap = new ConcurrentHashMap<String, BigDecimal>();
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	public static void main(String[] args) {
		List<String>list=new ArrayList<String>();
		list.add("sh603881");
		list.add("sz300073");
		list.add("sz002201");
		list.add("sh600438");
		list.add("sz300232");
		list.add("sz300092");
		list.add("sz300005");
		list.add("sz300014");
		list.add("sz300026");
		sendMsg(list);
	}

	private static void sendMsg(List<String>listTest) {
		for(String number:listTest) {
			List<HistoryPriceDo> list = ReadUrl.readUrl(number, 60);
			MockLog mockLog=mockDeal(list, "2020-09-01");
			DecimalFormat df = new DecimalFormat("0.00");
			System.out.println(JSON.toJSONString(mockLog));
			String context=mockLog.getLogs();
			if(StringUtils.isBlank(context)) {
				context="该走势还没找到买入点，请回溯更长的时间。";
			}
			 try {
				context = "GS=========测试AI操盘=================="
						 +"\n 股票编码："+ mockLog.getNumber()
						 +"\n 股票名称："+mockLog.getName()
						 +"\n 回测数据："+sdf.format(mockLog.getBeginTime())+"~"+sdf.format(mockLog.getEndTime())
						 +"\n 成功次数："+mockLog.getSuccess()
						 +"\n 失败次数："+mockLog.getFail()
						 +"\n 总盈利："+df.format(mockLog.getWin())
						 +"\n 总盈利率："+df.format(mockLog.getWinRate())+"%"
						 +"\n ======操作记录===================="
						 +"\n"+context;
				DingTalkRobotHTTPUtil.sendMsg(DingTalkRobotHTTPUtil.APP_SECRET, context, null, false);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	public static MockLog mockDeal(List<HistoryPriceDo> stortList, String beginDate) {
		if (stortList == null || stortList.isEmpty()) {
			logger.warn("http请求数据为空");
			return new MockLog();
		}
		MockLog mockLog=new MockLog();
		
		
		int powerValue = 0;
		int buyCount = 0;
		int sellCount = 0;
		int num = 0;
		int init = 100000;
		double keepPrice=0;
		double ma20_5 = 0;
		double allwin = 0;
		double total = init;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		for (HistoryPriceDo price : stortList) {
			if(StringUtils.isBlank(mockLog.getName())) {
				mockLog.setName(price.getName());
			}
			if(StringUtils.isBlank(mockLog.getNumber())) {
				mockLog.setNumber(price.getNumber());
			}
			
			try {
				Date date = price.getDateime();
				if (!StringUtils.isBlank(beginDate)) {
					Date bgtime = sdf1.parse(beginDate);
					if (bgtime.compareTo(date) >= 1) {
						continue;
					}
					if(mockLog.getBeginTime() == null) {
						mockLog.setBeginTime(bgtime);
					}
					mockLog.setEndTime(date);
				}
				
				String buyKey = sdf1.format(price.getDateime()) + "_" + price.getNumber();
				Boolean isBuy = buyPorintMap.get(buyKey);
				if (isBuy == null) {
					isBuy = true;
					buyPorintMap.put(buyKey, isBuy);
				}

				BigDecimal max = price.getZuigaojia();
				BigDecimal tempMax = maxPriceMap.get(price.getNumber());
				BigDecimal min = price.getZuidijia();
				BigDecimal tempMin = minPriceMap.get(price.getNumber());
				if (ma20_5 <= 0) {
					ma20_5 = price.getMa20().doubleValue();
				}

				if (null == tempMax || tempMax.compareTo(max) < 1) {
					maxPriceMap.put(price.getNumber(), max);
				}
				if (null == tempMin || tempMin.compareTo(min) > -1) {
					minPriceMap.put(price.getNumber(), min);
				}
				DecimalFormat df = new DecimalFormat("0.00");
				// 收盘价比MA20高
				if (price.getShoupanjia().compareTo(price.getMa20()) > -1) {
					buyCount++;
					sellCount = 0;
					if (num > 0) {
						double tempWin = (price.getShoupanjia().doubleValue()-keepPrice) * num * 100  ;
						String log="编号：" + price.getNumber() 
						+" "+price.getName()
						+ " 持有==> "
						+ sdf.format(price.getDateime()) 
						+ " MA20:" + price.getMa20().doubleValue()
						+ " 当前能量值：" + powerValue 
						+ " 当前价格：" + price.getShoupanjia() 
						+ " 净利价：" + df.format(price.getShoupanjia().doubleValue()-keepPrice)
						+ " 数量：" + (num * 100)
						+ " 动态盈利:" + df.format(tempWin);
						logger.info(log);
						//mockLog.setLogs(mockLog.getLogs()+log+"\n");
					}
					if (buyCount == 1 && num <= 0 && powerValue >= 0 && buyPorintMap.get(buyKey)) {
						buyPorintMap.put(buyKey, false);
						keepPrice = price.getMa20().doubleValue();
						ma20_5 = price.getMa20().doubleValue();
						num = (int) Math.floor(total / (price.getMa20().doubleValue() * 100));
						total = total - price.getMa20().doubleValue() * 100 * num;
						String log=" 买入点==> "
								+ sdf.format(price.getDateime()) 
								+ " 能量值：" + powerValue 
								+ " 价格：" + price.getMa20() 
								+ " 数量：" + (num * 100)
								+ " 余额：" + df.format(total);
						logger.info(log);
						mockLog.setLogs(mockLog.getLogs()+log+"\n");
					}
					powerValue++;
					// logger.info(sdf.format(price.getDateime())+":"+price.getNumber()+"
					// "+price.getShoupanjia()+">="+price.getMa20()+"=趋势上升 "+"当前能量值："+powerValue+"
					// 偏移量："+price.getPianlizhi());
					price.setUp(true);
				} else {
					sellCount++;
					buyCount = 0;
					// 止损逻辑
					double totaltemp = total + price.getShoupanjia().doubleValue() * 100 * num;

					if (totaltemp < init && buyPorintMap.get(buyKey)) {
						total = total + price.getShoupanjia().doubleValue() * 100 * num;
						double win = total - init;
						allwin = allwin + win;
						num = 0;
						keepPrice=0;
						String log=" 止损卖出==> "
								+ sdf.format(price.getDateime()) 
								+ " 能量值：" + powerValue 
								+ " 价格：" + df.format(price.getShoupanjia()) 
								+ " 盈利：" + df.format(win)
								+ " 盈利率：" + df.format((win / init) * 100) 
								+ "% 累计:" + df.format(allwin) 
								+ " 累计盈利率:" + df.format((allwin / init) * 100) + "%\n";
						logger.info(log);
						mockLog.setFail(mockLog.getFail()+1);
						mockLog.setLogs(mockLog.getLogs()+log+"\n");
						total = init;
					}

					if (sellCount == 1 && num > 0 && buyPorintMap.get(buyKey)) {
						buyPorintMap.put(buyKey, false);
						total = total + price.getMa20().doubleValue() * 100 * num;
						double win = total - init;
						allwin = allwin + win;
						num = 0;
						String log=" 卖出点==> "
						+ sdf.format(price.getDateime()) 
						+ " 能量值：" + powerValue 
						+ " 价格：" + df.format(price.getShoupanjia()) 
						+ " 净利价：" + df.format(price.getShoupanjia().doubleValue()-keepPrice)
						+ " 盈利：" + df.format(win)
						+ " 盈利率：" + df.format((win / init) * 100) 
						+ "% 累计:" + df.format(allwin) 
						+ " 累计盈利率:" + df.format((allwin / init) * 100) + "%\n";
						logger.info(log);
						mockLog.setSuccess(mockLog.getSuccess()+1);
						mockLog.setLogs(mockLog.getLogs()+log+"\n");
						total = init;
						keepPrice=0;
					}
					powerValue--;
					// logger.info(sdf.format(price.getDateime())+":"+price.getNumber()+"
					// "+price.getShoupanjia()+"<"+price.getMa20()+"=趋势下降 "+"当前能量值："+powerValue+"
					// 偏移量："+price.getPianlizhi());
				}
				price.setPowerValue(powerValue);
				mockLog.setPowerValue(powerValue);
				mockLog.setWin(allwin);
				mockLog.setWinRate((allwin / init) * 100);
				mockLog.setPrice(price);
			} catch (ParseException e) {
				logger.error(e.getMessage(), e);
			}  
		}
		return mockLog;
	}
	
}
