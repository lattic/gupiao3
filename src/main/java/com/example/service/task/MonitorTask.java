package com.example.service.task;

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

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.ai.MockDeal;
import com.example.demo.GuPiao;
import com.example.model.GuPiaoDo;
import com.example.model.MockLog;
import com.example.model.StockDo;
import com.example.model.SubscriptionDo;
import com.example.service.GuPiaoService;
import com.example.uitls.DateUtils;
import com.example.uitls.DingTalkRobotHTTPUtil;
import com.example.uitls.ReadUrl;

@Service
public class MonitorTask implements InitializingBean {
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
	
	//股票名称
	public static ConcurrentHashMap<String, StockDo> stockMap=new ConcurrentHashMap<String, StockDo>();
	@Scheduled(cron = "0 30 9 * * *")
	private void followTask1() {
		followTask();
	}
	@Scheduled(cron = "0 30 10 * * *")
	private void followTask2() {
		followTask();
	}
	@Scheduled(cron = "0 30 11 * * *")
	private void followTask3() {
		followTask();
	}
	@Scheduled(cron = "0 0 14 * * *")
	private void followTask4() {
		followTask();
	}
	 
	@Scheduled(cron = "0 0 15 * * *")
	private void followTask5() {
		 followTask();
	}
	private void followTask() {
		if(stockMap == null ||stockMap.isEmpty()) {
			init();
		}
		
		if(!DateUtils.traceTime()) {
			System.out.println("还没开盘");
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
	
	@Scheduled(cron = "0 0 12 * * *")
	private void updateAllGuPiao() {
		pool.execute(new Runnable() {
			@Override
			public void run() {
				getAllGuPiao();
			}
		});
	}
	
	// 从数据库获取股票池初始化map
	private int init() {
		List<StockDo> stockList = guPiaoService.getAllStock();
		stockList.forEach(stock->{
			if(StringUtils.containsIgnoreCase(stock.getName(), "ST") || StringUtils.containsIgnoreCase(stock.getName(), "债") ) {
				System.out.println(stock.getName());
			} 
        });
		return stockList.size();
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
		for(StockDo stock : stockMap.values()){
            Calendar calendar = Calendar.getInstance();  
			calendar.add(Calendar.MONTH, -1);
			calendar.add(Calendar.DATE, -15);
			SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
			MockLog log=mockDeal.mockDeal(stock.getNumber(), dateformat.format(calendar.getTime()),DingTalkRobotHTTPUtil.APP_TEST_SECRET,false);
			if(log != null && log.getIsBuyin() ) {
				//近4天出现买入点
				Calendar before = Calendar.getInstance();  
				before.add(Calendar.DATE, -5);
				if(log.getLastBuyin().after(before.getTime()) ) {
					if(maxprice.getWin() == null ) {
						maxprice=log;
					}
					
					if(log.getWin()!=null && log.getWin()> maxprice.getWin()) {
						if(log.getWinRate().doubleValue()>=3 && log.getWinRate().doubleValue()<=40) {
							log.setLogs(log.getLogs().replace("测试AI操盘", "AI个股推荐"));
							for(SubscriptionDo realTime:subscriptionList) {
								if(StringUtils.equals(realTime.getNumber(), "0")) {
									DingTalkRobotHTTPUtil.sendMsg(realTime.getDingtalkId(), log.getLogs(), null, false);
								}
							}
						}
						maxprice=log;
					}
					if(minprice.getWin() == null ) {
						minprice=log;
					}
					
					if(log.getWin()!=null && log.getWin()< minprice.getWin()) {
						minprice=log;
						if(log.getWinRate().doubleValue()<=-3) {
							DingTalkRobotHTTPUtil.sendMsg(DingTalkRobotHTTPUtil.APP_TEST_SECRET, log.getLogs(), null, false);
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
					if(log.getWin() != null) {
						total+=log.getWin();
					}
				}
			}
        }
		try {
			String  context = "所有机器人总收益："+total
					+"\n 赚钱的机器人："+max
					+"\n 赚钱股票："+winLog
					+"\n 亏钱的机器人:"+min
					+"\n 亏钱股票："+lossLog;
					//+"\n 单个机器人最大盈利："
					// + maxprice.getLogs()
					//+"\n 单个机器人最大亏损："
					//+minprice.getLogs();
			DingTalkRobotHTTPUtil.sendMsg(DingTalkRobotHTTPUtil.APP_TEST_SECRET, context, null, false);
			logger.info(context);
		} catch (Exception e) {
		}
		
	}
	
	
	//更新股票池
	private void  getAllGuPiao() {
		System.out.println(new Date()+" ==>开始更新股票");
		for(int i=0;i<=99999;i++) {
			GuPiao date=ReadUrl.readUrl(i, "sz0",false);
			if(date !=null) {
				GuPiaoDo model=new GuPiaoDo();
				BeanUtils.copyProperties(date, model);
				//System.out.println(new Date()+" ==>"+model.getNumber()+" "+model.getName());
				guPiaoService.updateStock(model.getNumber(),model.getName(), 2) ;
				StockDo stock=stockMap.get(model.getNumber());
		    	if(stock != null) {
		    		stockMap.put(model.getNumber(), stock);
		    	}
			}
			date=ReadUrl.readUrl(i, "sz3",false);
			if(date !=null) {
				GuPiaoDo model=new GuPiaoDo();
				BeanUtils.copyProperties(date, model);
				//System.out.println(new Date()+" ==>"+model.getNumber()+" "+model.getName());
				guPiaoService.updateStock(model.getNumber(),model.getName(), 3) ;
				StockDo stock=stockMap.get(model.getNumber());
		    	if(stock != null) {
		    		stockMap.put(model.getNumber(), stock);
		    	}
			}
			date=ReadUrl.readUrl(i, "sh6",false);
			if(date !=null) {
				GuPiaoDo model=new GuPiaoDo();
				BeanUtils.copyProperties(date, model);
				//System.out.println(new Date()+" ==>"+model.getNumber()+" "+model.getName());
				guPiaoService.updateStock(model.getNumber(),model.getName(), 1) ;
				StockDo stock=stockMap.get(model.getNumber());
		    	if(stock != null) {
		    		stockMap.put(model.getNumber(), stock);
		    	}
			}
		}
		//获取所有股票的历史60分钟数据
		List<StockDo> stockList = guPiaoService.getAllStock();
		stockList.forEach(stock->{
			guPiaoService.updateHistoryStock(stock.getNumber());
        });
		
	}
	
	
	

	
	@Override
	public void afterPropertiesSet() throws Exception {
		Date now=new Date();
    	SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String robotbuy = MessageFormat.format("【同步股票池】"+dateformat.format(now)
											   + "\n 初始化股票池数量："
											   + init(),new Object[] {});
        DingTalkRobotHTTPUtil.sendMsg(DingTalkRobotHTTPUtil.APP_TEST_SECRET, robotbuy, null, false);
	}
	
	
}
