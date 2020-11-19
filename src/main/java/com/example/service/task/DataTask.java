package com.example.service.task;

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
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.demo.GuPiao;
import com.example.model.GuPiaoDo;
import com.example.model.HistoryStockDo;
import com.example.model.StockDo;
import com.example.model.SubscriptionDo;
import com.example.service.GuPiaoService;
import com.example.uitls.DateUtils;
import com.example.uitls.DingTalkRobotHTTPUtil;
import com.example.uitls.ReadUrl;
import com.example.uitls.RedisKeyUtil;
import com.example.uitls.RedisUtil;

@Service
public class DataTask  implements InitializingBean {
	ThreadPoolExecutor  pool = new ThreadPoolExecutor(20, 100, 1,TimeUnit.SECONDS,
														new LinkedBlockingDeque<Runnable>(1000), 
														Executors.defaultThreadFactory(), 
														new ThreadPoolExecutor.CallerRunsPolicy());
	
	private static Logger logger = LoggerFactory.getLogger("task_log");
	
	@Autowired
	private GuPiaoService guPiaoService;
	@Resource
	private RedisUtil redisUtil;
	
	
	@Override
	public void afterPropertiesSet() throws Exception {
		String robotbuy = MessageFormat.format("【初始化股票池】"
											   + "\n 初始化股票池数量："
											   + init(),new Object[] {});
        DingTalkRobotHTTPUtil.sendMsg(DingTalkRobotHTTPUtil.APP_TEST_SECRET, robotbuy, null, false);
        showBoduan();
	}
	
	/**
	 * 所有到的股票池
	 */
	@Scheduled(cron = "0 10 15 * * *")
	private void updateAllGuPiao() {
		logger.info("开始更新股票池总数");
		pool.execute(new Runnable() {
			@Override
			public void run() {
				logger.info("==>开始更新股票");
				for(int i=0;i<=99999;i++) {
					GuPiao date=ReadUrl.readUrl(i, "sz0",false);
					if(date !=null) {
						GuPiaoDo model=new GuPiaoDo();
						BeanUtils.copyProperties(date, model);
						guPiaoService.updateStock(model.getNumber(),model.getName(), 2) ;
					}
					date=ReadUrl.readUrl(i, "sz3",false);
					if(date !=null) {
						GuPiaoDo model=new GuPiaoDo();
						BeanUtils.copyProperties(date, model);
						guPiaoService.updateStock(model.getNumber(),model.getName(), 3) ;
					}
					date=ReadUrl.readUrl(i, "sh6",false);
					if(date !=null) {
						GuPiaoDo model=new GuPiaoDo();
						BeanUtils.copyProperties(date, model);
						guPiaoService.updateStock(model.getNumber(),model.getName(), 1) ;
					}
				}
				logger.info("==>更新完毕");
				init();
			}
		});
	}
	
	@Scheduled(cron = "0 10 9 * * *")
	private void  updateHistory() {
		//获取所有股票的历史60分钟数据
		logger.info("开始复盘昨天的数据");
		List<StockDo> stockList = guPiaoService.getAllStock();
		stockList.forEach(stock->{
			String key="fp_"+stock.getNumber()+"_"+DateUtils.getToday();
			if(!redisUtil.hasKey(key)) {
				guPiaoService.updateHistoryStock(stock.getNumber());
				guPiaoService.timeInterval(stock.getNumber());
				redisUtil.set(key, true);
			}
        });
	}
	
	// 从数据库获取股票池初始化map
	private int init() {
		List<StockDo> stockList = guPiaoService.getAllStock();
		stockList.forEach(stock->{
			if(StringUtils.containsIgnoreCase(stock.getName(), "ST") || StringUtils.containsIgnoreCase(stock.getName(), "债") ) {
			}else {
				redisUtil.set(RedisKeyUtil.getStockName(stock.getNumber()), stock.getName());
			}
        });
		return stockList.size();
	}
	
	@Scheduled(cron = "0 35 9 * * *")
	private void showBoduan() {
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
					Boolean isNotifyByMock=(Boolean)redisUtil.get(RedisKeyUtil.getBoduanNotify(number, appSecret));
					//通知开关
					if(isNotifyByMock == null || isNotifyByMock) {
						isNotifyByMock=true;
					}
					
					if(isNotifyByMock) {
						List<HistoryStockDo> list= guPiaoService.getLastHistoryStock(number,3);
						String msg="GS========测试个股波段分析=========GS"
								+ "\n 股票编号："+number
								+ "\n 股票名称："+(String)redisUtil.get(RedisKeyUtil.getStockName(number))
								+ "\n";
						for(HistoryStockDo stock:list) {
							msg=msg+stock.getRemark();
						}
						
						DingTalkRobotHTTPUtil.sendMsg(appSecret, msg, null, false);
						isNotifyByMock=false;
					}
					redisUtil.set(RedisKeyUtil.getBoduanNotify(number, appSecret),isNotifyByMock,86400L);
				} catch (Exception e) {
					logger.error("异常个股波段分析:"+"number:"+number+"-->"+e.getMessage(),e);
				}
			}
		});
	}
	
}
