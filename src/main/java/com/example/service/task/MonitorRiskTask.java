package com.example.service.task;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
	
	@Scheduled(cron = "0/5 * * * * *")
	private void  monitorAll() throws Exception {
		listenRealTime("sh603881");
		listenRealTime("sz300073");
		listenRealTime("sz002201");
		listenRealTime("sh600438");
		listenRealTime("sz300232");
		listenRealTime("sz300092");
		listenRealTime("sz300005");
		listenRealTime("sz300014");
		listenRealTime("sz300026");
	}

	private void listenRealTime(String key) throws Exception {
		DecimalFormat    df   = new DecimalFormat("######0.00");  
		Date now=new Date();
    	SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		GuPiao date=ReadUrl.readUrl(key,false);
		if(date !=null) {
			GuPiaoDo model=new GuPiaoDo();
			BeanUtils.copyProperties(date, model);
			if(model.getDangqianjiage()<=0) {
				return;
			}
			
			HistoryPriceDo riskPrice=ReadUrl.getLastMa20(key, 60);
			Boolean status=lossMap.get(key);
			Boolean isNotify=notifyMap.get(key);
			if(isNotify == null) {
				 notifyMap.put(key,true);
			}
			//止损 当前价格低于历史支撑位
			if(model.getDangqianjiage()<=riskPrice.getZhichengwei().doubleValue() ) {
				String content = MessageFormat.format("GS【止损预警提示】"+dateformat.format(now)
		        +"\n------------------------------------ \n股票代码：{0}\n股票名称：{1}\n压力位置:{2}\n当前价格:{3}\n策略规则:{4}", 
		        		                 new Object[] {date.getNumber(), 
		        		        		 date.getName(), 
		        		        		 riskPrice.getMa20().doubleValue(),
		        		        		 df.format(model.getDangqianjiage()), 
		        		        		 "股价已经破位，请及时止损！！"});
				logger.info(content);
				DingTalkRobotHTTPUtil.sendMsg(DingTalkRobotHTTPUtil.APP_SECRET, content, null, false);
				notifyMap.put(key,false);
			}
			
			//弱势 当前价格小于20天线
			if(model.getDangqianjiage()<riskPrice.getMa20().doubleValue()) {
				if(status == null) {
					String content = MessageFormat.format("GS【初始化状态--2】"+dateformat.format(now)
			        +"\n------------------------------------ \n股票代码：{0}\n股票名称：{1}\n当前能量值:{2}\n压力位置:{3}\n当前价格:{4}\n支撑位置:{5}\n当前趋势:{6}", 
			        		                 new Object[] {date.getNumber(), 
			        		        		 date.getName(), 
			        		        		 riskPrice.getPowerValue(),
			        		        		 riskPrice.getMa20().doubleValue(),
			        		        		 df.format(model.getDangqianjiage()), 
			        		        		 riskPrice.getZhichengwei().doubleValue(),
			        		        		 "目前属于下滑趋势"});
					logger.info(content);
					//DingTalkRobotHTTPUtil.sendMsg(DingTalkRobotHTTPUtil.APP_SECRET, content, null, false);
					lossMap.put(key,true);
					return ;
				}
				
				if(!status) {
					String content = MessageFormat.format("GS【卖出信号提示】"+dateformat.format(now)
			        +"\n------------------------------------ \n股票代码：{0}\n股票名称：{1}\n条件价格:{2}\n当前价格:{3}\n策略规则:{4}", 
			        		                 new Object[] {date.getNumber(), 
			        		        		 date.getName(), 
			        		        		 riskPrice.getMa20().doubleValue(),
			        		        		 df.format(model.getDangqianjiage()), 
			        		        		 "当前股票从强转弱趋势，请及时止盈或止损"});
					logger.info(content);
					if(isNotify) {
						DingTalkRobotHTTPUtil.sendMsg(DingTalkRobotHTTPUtil.APP_SECRET, content, null, false);
						notifyMap.put(key,false);
					}
					lossMap.put(key,true);
				}
			}
			
			//强势 当前价格大于20天线
			if(model.getDangqianjiage()>=riskPrice.getMa20().doubleValue()){
				if(status == null) {
					String content = MessageFormat.format("GS【初始化状态--2】"+dateformat.format(now)
					  +"\n------------------------------------ \n股票代码：{0}\n股票名称：{1}\n当前能量值:{2}\n压力位置:{3}\n当前价格:{4}\n支撑位置:{5}\n当前趋势:{6}", 
			        		                 new Object[] {date.getNumber(), 
			        		        		 date.getName(), 
			        		        		 riskPrice.getPowerValue(),
			        		        		 riskPrice.getYaliwei(),
			        		        		 df.format(model.getDangqianjiage()), 
			        		        		 riskPrice.getMa20().doubleValue(),
			        		        		 "目前属于上升趋势"});
					logger.info(content);
					//DingTalkRobotHTTPUtil.sendMsg(DingTalkRobotHTTPUtil.APP_SECRET, content, null, false);
					lossMap.put(key,false);
					return ;
				}
				if(status) {
					String content = MessageFormat.format("GS【买入信号提示】"+dateformat.format(now)
			        +"\n------------------------------------ \n股票代码：{0}\n股票名称：{1}\n条件价格:{2}\n当前价格:{3}\n策略规则:{4}", 
			        		                 new Object[] {date.getNumber(), 
			        		        		 date.getName(), 
			        		        		 riskPrice.getMa20().doubleValue(),
			        		        		 df.format(model.getDangqianjiage()), 
			        		        		 "当前股价从弱转强，请配合趋势买入股票"});
					logger.info(content);
					if(isNotify) {
						DingTalkRobotHTTPUtil.sendMsg(DingTalkRobotHTTPUtil.APP_SECRET, content, null, false);
						notifyMap.put(key,false);
					}
					lossMap.put(key,false);
				}
			}
		}
	}
}
