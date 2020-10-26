package com.example.service.task;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.ai.MockDeal;
import com.example.demo.GuPiao;
import com.example.model.GuPiaoDo;
import com.example.model.HistoryPriceDo;
import com.example.uitls.DingTalkRobotHTTPUtil;
import com.example.uitls.ReadUrl;

@Service
public class MonitorRiskTask {
	private static Logger logger = LoggerFactory.getLogger(MonitorRiskTask.class);
	ThreadPoolExecutor  pool = new ThreadPoolExecutor(20, 100, 1,TimeUnit.SECONDS,
			new LinkedBlockingDeque<Runnable>(1000), 
			Executors.defaultThreadFactory(), 
			new ThreadPoolExecutor.CallerRunsPolicy());

	
	
	//目前是否弱势
	private static ConcurrentHashMap<String, Boolean> lossMap=new ConcurrentHashMap<String, Boolean>();
	//目前是否通知
	private static ConcurrentHashMap<String, Boolean> notifyMap=new ConcurrentHashMap<String, Boolean>();
	//AI操盘通知   key—— yyyymmdd_number
	private static ConcurrentHashMap<String, Boolean> mockAiMap=new ConcurrentHashMap<String, Boolean>();
	
	
	@Scheduled(cron = "0/30 * * * * *")
	private void  monitorAll() throws Exception {
		//叶琳
		excuteRunListen("sz300026",DingTalkRobotHTTPUtil.yelin);
		
		//朱斌
		excuteRunListen("sh603986",DingTalkRobotHTTPUtil.zhubin);
		excuteRunListen("sz002594",DingTalkRobotHTTPUtil.zhubin);
		excuteRunListen("sz002241",DingTalkRobotHTTPUtil.zhubin);
		
		
		List<String>numberList= new ArrayList<>();
		numberList.add("sh603881");
		numberList.add("sz300073");
		numberList.add("sz002201");
		numberList.add("sh600438");
		numberList.add("sz300232");
		numberList.add("sz300092");
		numberList.add("sz300014");
		numberList.forEach(number->{
            System.out.println(number);
            excuteRunListen(number,DingTalkRobotHTTPUtil.APP_SECRET);
        });
		
	}


	private void excuteRunListen(final String number,final String appSecret) {
		pool.execute(new Runnable() {
			@Override
			public void run() {
				try {
					listenRealTime(number,appSecret);
					SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
					String key=dateformat.format(new Date())+"_"+number;
					Boolean isNotifyByMock=mockAiMap.get(key);
					//通知开关
					if(isNotifyByMock == null || isNotifyByMock) {
						isNotifyByMock=true;
					}
					
					if(isNotifyByMock) {
						Calendar calendar = Calendar.getInstance();  
						calendar.add(Calendar.MONTH, -1);
						MockDeal.mockDeal(number, dateformat.format(calendar.getTime()),appSecret,true);
						isNotifyByMock=false;
					}
					mockAiMap.put(number,isNotifyByMock);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	

	private void listenRealTime(final String key,final String appSecret) throws Exception {
		DecimalFormat    df   = new DecimalFormat("######0.00");  
		Date now=new Date();
    	SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	GuPiao date=ReadUrl.readUrl(key,false);
		if(date !=null) {
			GuPiaoDo nowPrice=new GuPiaoDo();
			BeanUtils.copyProperties(date, nowPrice);
			if(nowPrice.getDangqianjiage()<=0) {
				return;
			}
			
			//获取走势
			HistoryPriceDo riskPrice=ReadUrl.getLastMa20(key, 60);
			Boolean status=lossMap.get(key);
			Boolean isNotify=notifyMap.get(key);
			//通知开关
			if(isNotify == null) {
				 notifyMap.put(key,true);
			}
			
			//止损 当前价格低于历史支撑位
			if(nowPrice.getDangqianjiage()<=riskPrice.getZhichengwei().doubleValue() ) {
				String content = MessageFormat.format("GS【止损预警提示】"+dateformat.format(now)
		        +"\n------------------------------------ \n股票代码：{0}\n股票名称：{1}\n压力位置:{2}\n当前价格:{3}\n策略规则:{4}", 
		        		                 new Object[] {date.getNumber(), 
		        		        		 date.getName(), 
		        		        		 riskPrice.getMa20().doubleValue(),
		        		        		 df.format(nowPrice.getDangqianjiage()), 
		        		        		 "股价已经破位，请及时止损！！"});
				logger.info(content);
				if(isNotify) {
					DingTalkRobotHTTPUtil.sendMsg(appSecret, content, null, false);
					notifyMap.put(key,false);
				}
			}
			
			//弱势 当前价格小于20天线
			if(nowPrice.getDangqianjiage()<riskPrice.getMa20().doubleValue()) {
				if(status == null) {
					String content = MessageFormat.format("GS【开始监听】"+dateformat.format(now)
			        +"\n------------------------------------ \n股票代码：{0}\n股票名称：{1}\n当前能量值:{2}\n压力位置:{3}\n当前价格:{4}\n支撑位置:{5}\n当前趋势:{6}", 
			        		                 new Object[] {date.getNumber(), 
			        		        		 date.getName(), 
			        		        		 riskPrice.getPowerValue(),
			        		        		 riskPrice.getMa20().doubleValue(),
			        		        		 df.format(nowPrice.getDangqianjiage()), 
			        		        		 riskPrice.getZhichengwei().doubleValue(),
			        		        		 "目前属于下滑趋势"});
					logger.info(content);
					DingTalkRobotHTTPUtil.sendMsg(appSecret, content, null, false);
					lossMap.put(key,true);
					return;
				}
				
				if(!status) {
					String content = MessageFormat.format("GS【卖出信号提示】"+dateformat.format(now)
			        +"\n------------------------------------ \n股票代码：{0}\n股票名称：{1}\n条件价格:{2}\n当前价格:{3}\n策略规则:{4}", 
			        		                 new Object[] {date.getNumber(), 
			        		        		 date.getName(), 
			        		        		 riskPrice.getMa20().doubleValue(),
			        		        		 df.format(nowPrice.getDangqianjiage()), 
			        		        		 "当前股票从强转弱趋势，请及时止盈或止损"});
					logger.info(content);
					if(isNotify) {
						DingTalkRobotHTTPUtil.sendMsg(appSecret, content, null, false);
						notifyMap.put(key,false);
					}
					lossMap.put(key,true);
				}
			}
			
			//强势 当前价格大于20天线
			if(nowPrice.getDangqianjiage()>=riskPrice.getMa20().doubleValue()){
				if(status == null) {
					String content = MessageFormat.format("GS【开始监听】"+dateformat.format(now)
					  +"\n------------------------------------ \n股票代码：{0}\n股票名称：{1}\n当前能量值:{2}\n压力位置:{3}\n当前价格:{4}\n支撑位置:{5}\n当前趋势:{6}", 
			        		                 new Object[] {date.getNumber(), 
			        		        		 date.getName(), 
			        		        		 riskPrice.getPowerValue(),
			        		        		 riskPrice.getYaliwei(),
			        		        		 df.format(nowPrice.getDangqianjiage()), 
			        		        		 riskPrice.getMa20().doubleValue(),
			        		        		 "目前属于上升趋势"});
					logger.info(content);
					DingTalkRobotHTTPUtil.sendMsg(appSecret, content, null, false);
					lossMap.put(key,false);
					return;
				}
				if(status) {
					String content = MessageFormat.format("GS【买入信号提示】"+dateformat.format(now)
			        +"\n------------------------------------ \n股票代码：{0}\n股票名称：{1}\n条件价格:{2}\n当前价格:{3}\n策略规则:{4}", 
			        		                 new Object[] {date.getNumber(), 
			        		        		 date.getName(), 
			        		        		 riskPrice.getMa20().doubleValue(),
			        		        		 df.format(nowPrice.getDangqianjiage()), 
			        		        		 "当前股价从弱转强，请配合趋势买入股票"});
					logger.info(content);
					if(isNotify) {
						DingTalkRobotHTTPUtil.sendMsg(appSecret, content, null, false);
						notifyMap.put(key,false);
					}
					lossMap.put(key,false);
				}
			}
		}
	}
}
