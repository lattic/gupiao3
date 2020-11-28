package com.example.service.task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.ai.MockDeal;
import com.example.model.MockLog;
import com.example.model.StockDo;
import com.example.model.SubscriptionDo;
import com.example.service.GuPiaoService;
import com.example.uitls.DateUtils;
import com.example.uitls.DingTalkRobotHTTPUtil;
import com.example.uitls.RedisKeyUtil;
import com.example.uitls.RedisUtil;

@Service
public class MonitorTask  {
	private static Logger logger = LoggerFactory.getLogger("mock_log");
	ThreadPoolExecutor  pool = new ThreadPoolExecutor(20, 100, 1,TimeUnit.SECONDS,
			new LinkedBlockingDeque<Runnable>(1000), 
			Executors.defaultThreadFactory(), 
			new ThreadPoolExecutor.CallerRunsPolicy());
	
	static boolean updateAll=true;
	static boolean updateReal=true;
	@Autowired
	private GuPiaoService guPiaoService;
	
	@Autowired
	private MockDeal mockDeal;
	@Resource
	private RedisUtil redisUtil;
	
	
	@Scheduled(cron = "0 30 9 * * *")
	private void followTask1() {
		followTask();
	}
	
	private void followTask() {
		
		if(!DateUtils.traceTime(guPiaoService.getHolidayList())) {
			return ;
		}
		
		List<String>list=new ArrayList<String>();
		List<SubscriptionDo> subscriptionList=guPiaoService.listMemberAll();
		for(SubscriptionDo realTime:subscriptionList) {
			if(!StringUtils.equals(realTime.getNumber(), "0")) {
				list.add(realTime.getNumber());
			}
		}
		pool.execute(new Runnable() {
			@Override
			public void run() {
				mockDeal.sendMsgByList(list,"2020-09-24",DingTalkRobotHTTPUtil.APP_TEST_SECRET);
			}
		});
	}
	
	
	
		
	
	
	
	
	//初始化map
	@Scheduled(cron = "0 35 9 * * *")
	public void AiBuyIn() {
		List<SubscriptionDo> subscriptionList=guPiaoService.listMemberAll();
		int max=0;
		int min=0;
		double total=0;
		MockLog maxprice=new MockLog();
		MockLog minprice=new MockLog();
		String winLog="";
		String lossLog="";
		for(StockDo stock : guPiaoService.getAllStock()){
            Calendar calendar = Calendar.getInstance();  
			calendar.add(Calendar.MONTH, -1);
			SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
			MockLog log=mockDeal.mockDeal(stock.getNumber(), dateformat.format(calendar.getTime()),DingTalkRobotHTTPUtil.APP_TEST_SECRET,false);
			if(log == null ) {
				continue;
			}
			//初始化
			if(maxprice.getWin() == null ) {
				maxprice=log;
			}
			if(minprice.getWin() == null ) {
				minprice=log;
			}
			//替换最大值
			if(log.getWin()!=null && log.getWin() >= maxprice.getWin() ) {
				maxprice=log;
			}
			//替换最小值
			if(log.getWin()!=null && log.getWin() <= minprice.getWin()) {
				minprice=log;
				if(log.getWinRate().doubleValue()<=-3) {
					//DingTalkRobotHTTPUtil.sendMsg(DingTalkRobotHTTPUtil.APP_TEST_SECRET, log.getLogs(), null, false);
				}
			}
			
			if(log.getWinRate() != null && log.getWinRate()>0) {
				winLog+=log.getNumber()+"  "+log.getName()+" "+log.getWinRate()+"\n";
				max++;
			}
			if(log.getWinRate() != null &&  log.getWinRate()<0) { 
				min++;
				lossLog+=log.getNumber()+"  "+log.getName()+" "+log.getWinRate()+"\n";
			}
			//统计金额
			if(log.getWin() != null) {
				total+=log.getWin();
			}
			
			//近5天出现买入点,推荐
			Calendar before = Calendar.getInstance();  
			before.add(Calendar.DATE, -3);
			if(log.getIsBuyin() && log.getLastBuyin()!= null && log.getLastBuyin().after(before.getTime()) ) {
				if(log.getWinRate().doubleValue()>=3 && log.getWinRate().doubleValue()<=40) {
					log.setLogs(log.getLogs().replace("测试AI操盘", "AI个股推荐"));
					for(SubscriptionDo realTime:subscriptionList) {
						String key=RedisKeyUtil.getStockSellNotify(realTime.getNumber(), realTime.getDingtalkId());
						Boolean isNotifyByMock=(Boolean)redisUtil.get(key);
						//通知开关
						if(isNotifyByMock == null || isNotifyByMock) {
							isNotifyByMock=true;
						}
						
						if(StringUtils.equals(realTime.getNumber(), "0") && isNotifyByMock) {
							isNotifyByMock=false;
							DingTalkRobotHTTPUtil.sendMsg(realTime.getDingtalkId(), log.getLogs(), null, false);
						}
						redisUtil.set(key,isNotifyByMock,86400L);
					}
				}
			}
		}
		try {
			String  context = "所有机器人总收益:"+total
					+"\n 赚钱的机器人:"+max
					//+"\n 赚钱股票："+winLog
					+"\n 亏钱的机器人:"+min;
					//+"\n 亏钱股票："+lossLog;
					//+"\n 单个机器人最大盈利："
					// + maxprice.getLogs()
					//+"\n 单个机器人最大亏损："
					//+minprice.getLogs();
			DingTalkRobotHTTPUtil.sendMsg(DingTalkRobotHTTPUtil.APP_TEST_SECRET, context, null, false);
			logger.info(context);
		} catch (Exception e) {
		}
		
	}
	
	
	
	
	
	
	

	
	
	
}
