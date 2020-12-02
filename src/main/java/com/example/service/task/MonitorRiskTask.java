package com.example.service.task;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.example.demo.GuPiao;
import com.example.model.GuPiaoDo;
import com.example.model.HistoryPriceDo;
import com.example.model.SubscriptionDo;
import com.example.service.GuPiaoService;
import com.example.uitls.DateUtils;
import com.example.uitls.DingTalkRobotHTTPUtil;
import com.example.uitls.ReadApiUrl;
import com.example.uitls.RedisKeyUtil;
import com.example.uitls.RedisUtil;

@Service
public class MonitorRiskTask {
	private static Logger logger = LoggerFactory.getLogger("task_log");
	ThreadPoolExecutor  pool = new ThreadPoolExecutor(20, 100, 1,TimeUnit.SECONDS,
			new LinkedBlockingDeque<Runnable>(1000), 
			Executors.defaultThreadFactory(), 
			new ThreadPoolExecutor.CallerRunsPolicy());

	@Autowired
	private GuPiaoService guPiaoService;
	@Autowired
	private ReadApiUrl apiUrl;
	@Resource
	private RedisUtil redisUtil;
	
	
	@Scheduled(cron = "0/30 * 9-15 * * MON-FRI")
	private void  monitorAll() throws Exception {
		if(!DateUtils.traceTime(guPiaoService.getHolidayList())) {
			return ;
		}
		List<SubscriptionDo> list=guPiaoService.listMemberAll();
		for(SubscriptionDo realTime:list) {
			if(!StringUtils.equals(realTime.getNumber(), "0")) {
				excuteRunListen(realTime.getNumber(),realTime.getDingtalkId(),realTime.getBegintime());
			}
		}
	}


	private void excuteRunListen(final String number,final String appSecret,final String beginTime) {
		pool.execute(new Runnable() {
			@Override
			public void run() {
				try {
					listenRealTime(number,appSecret);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void setRealTimeStatus(String number,Boolean isNotify) {
		redisUtil.set(RedisKeyUtil.getRealTimeStatus(number), isNotify,86500L);
	}
	private Boolean getRealTimeStatus(String number) {
		return (Boolean)redisUtil.get(RedisKeyUtil.getRealTimeStatus(number));
	}
	private void setNotify(String number,Boolean isNotify) {
		redisUtil.set(RedisKeyUtil.getRealTimeNotify(number), isNotify,1800L);
	}
	private Boolean getNotify(String number) {
		return (Boolean)redisUtil.get(RedisKeyUtil.getRealTimeNotify(number));
	}

	private void listenRealTime(final String number,final String appSecret) throws Exception {
		DecimalFormat    df   = new DecimalFormat("######0.00");  
		Date now=new Date();
    	SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	GuPiao date= apiUrl.readRealTimeUrl(number);
    	if(date == null) {
    		pool.execute(new UpdateRealTimeTask(guPiaoService,number,apiUrl,redisUtil));
    		return;
    	}
		GuPiaoDo nowPrice=new GuPiaoDo();
		BeanUtils.copyProperties(date, nowPrice);
		if(nowPrice.getDangqianjiage()<=0) {
			return;
		}
		
		//获取走势
		HistoryPriceDo riskPrice=guPiaoService.getLastZhichengwei(number);
		if(riskPrice == null || riskPrice.getZhichengwei() == null) {
			System.out.println("找不到最近一次指标："+number);
			return;
		}
		Boolean status= getRealTimeStatus(number);
		Boolean isNotify = getNotify(number);
		//通知开关
		if(isNotify == null) {
			isNotify=true;
			setNotify(number,isNotify);
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
			riskPrice.setZhichengwei(new BigDecimal(nowPrice.getDangqianjiage()).setScale(2));
			String key=RedisKeyUtil.getLastHistoryPrice(number, DateUtils.getToday());
			redisUtil.set(key, JSON.toJSONString(riskPrice),86400L);
			logger.info(content);
			if(isNotify) {
				DingTalkRobotHTTPUtil.sendMsg(appSecret, content, null, false);
				setNotify(number,false);
			}
		}
		
		//弱势 当前价格小于20天线
		if(nowPrice.getDangqianjiage()<riskPrice.getMa20().doubleValue()) {
			if(status == null) {
				String content = MessageFormat.format("GS【诊断股票走势】"+dateformat.format(now)
		        +"\n------------------------------------ \n股票代码：{0}\n股票名称：{1}\n当前能量值:{2}\n压力位置:{3}\n当前价格:{4}\n支撑位置:{5}\n当前趋势:{6}", 
		        		                 new Object[] {date.getNumber(), 
		        		        		 date.getName(), 
		        		        		 riskPrice.getPowerValue(),
		        		        		 riskPrice.getMa20().doubleValue(),
		        		        		 df.format(nowPrice.getDangqianjiage()), 
		        		        		 riskPrice.getZhichengwei().doubleValue(),
		        		        		 "目前属于下滑趋势"});
				logger.info(content);
				if(isNotify) {
					DingTalkRobotHTTPUtil.sendMsg(appSecret, content, null, false);
				}
				setRealTimeStatus(number,true);
				return;
			}
			
//				if(!status) {
//					String content = MessageFormat.format("GS【卖出信号提示】"+dateformat.format(now)
//			        +"\n------------------------------------ \n股票代码：{0}\n股票名称：{1}\n条件价格:{2}\n当前价格:{3}\n策略规则:{4}", 
//			        		                 new Object[] {date.getNumber(), 
//			        		        		 date.getName(), 
//			        		        		 riskPrice.getMa20().doubleValue(),
//			        		        		 df.format(nowPrice.getDangqianjiage()), 
//			        		        		 "当前股票从强转弱趋势，请及时止盈或止损"});
//					logger.info(content);
//					if(isNotify) {
//						DingTalkRobotHTTPUtil.sendMsg(appSecret, content, null, false);
//						setNotify(number,false);
//					}
//					setRealTimeStatus(number,true);
//				}
		}
		
		//强势 当前价格大于20天线
		if(nowPrice.getDangqianjiage()>=riskPrice.getMa20().doubleValue()){
			if(status == null) {
				String content = MessageFormat.format("GS【诊断股票走势】"+dateformat.format(now)
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
				setRealTimeStatus(number,false);
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
					setNotify(number,false);
				}
				setRealTimeStatus(number,false);
			}
		}
	}
}
