package com.example.service.task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.model.StockDo;
import com.example.service.GuPiaoService;
import com.example.uitls.DateUtils;
import com.example.uitls.ReadApiUrl;
import com.example.uitls.RedisKeyUtil;
import com.example.uitls.RedisUtil;

@Service
public class RealTimeTask implements InitializingBean {
	private static Logger logger = LoggerFactory.getLogger("real_time");
	@Autowired
	private GuPiaoService guPiaoService;
	@Autowired
	private ReadApiUrl apiUrl;
	@Resource
	private RedisUtil redisUtil;
	private static String today=DateUtils.getToday();
	
	ThreadPoolExecutor  pool1 = new ThreadPoolExecutor(20, 300, 5,TimeUnit.SECONDS,
			new LinkedBlockingDeque<Runnable>(2000), 
			Executors.defaultThreadFactory(), 
			new ThreadPoolExecutor.CallerRunsPolicy());
	ThreadPoolExecutor  pool2 = new ThreadPoolExecutor(20, 300, 5,TimeUnit.SECONDS,
			new LinkedBlockingDeque<Runnable>(2000), 
			Executors.defaultThreadFactory(), 
			new ThreadPoolExecutor.CallerRunsPolicy());
	ThreadPoolExecutor  pool3 = new ThreadPoolExecutor(20, 300, 5,TimeUnit.SECONDS,
			new LinkedBlockingDeque<Runnable>(2000), 
			Executors.defaultThreadFactory(), 
			new ThreadPoolExecutor.CallerRunsPolicy());
	ThreadPoolExecutor  pool4 = new ThreadPoolExecutor(20, 300, 5,TimeUnit.SECONDS,
			new LinkedBlockingDeque<Runnable>(2000), 
			Executors.defaultThreadFactory(), 
			new ThreadPoolExecutor.CallerRunsPolicy());
	ThreadPoolExecutor  pool5 = new ThreadPoolExecutor(20, 300, 5,TimeUnit.SECONDS,
			new LinkedBlockingDeque<Runnable>(2000), 
			Executors.defaultThreadFactory(), 
			new ThreadPoolExecutor.CallerRunsPolicy());
	
	private static List<StockDo> list1= new ArrayList<StockDo>();
	private static List<StockDo> list2= new ArrayList<StockDo>();
	private static List<StockDo> list3= new ArrayList<StockDo>();
	private static List<StockDo> list4= new ArrayList<StockDo>();
	private static List<StockDo> list5= new ArrayList<StockDo>();
	
	@Override
	public void afterPropertiesSet() throws Exception {
		try {
			List<StockDo> list=guPiaoService.getAllStock();
			int k=list.size()/5;
			list1=list.subList(0, k);
			list2=list.subList(1*k, 2*k);
			list3=list.subList(2*k, 3*k);
			list4=list.subList(3*k, 4*k);
			list5=list.subList(4*k, list.size());
			today=DateUtils.getToday();
		} catch (Exception e) {
			logger.warn("init Exception:"+e.getMessage(),e);
		}
	}
	
	@Scheduled(cron = "0 0 * * * *")
	public void  todayTask() throws Exception {
		today=DateUtils.getToday();
	}
	
	@Scheduled(cron = "0/30 * 9-15 * * MON-FRI")
	public void  task1() throws Exception {
		updateCache(list1,pool1);
	}
	@Scheduled(cron = "0/30 * 9-15 * * MON-FRI")
	public void  task2() throws Exception {
		updateCache(list2,pool2);
	}
	@Scheduled(cron = "0/30 * 9-15 * * MON-FRI")
	public void  task3() throws Exception {
		updateCache(list3,pool3);
	}
	@Scheduled(cron = "0/30 * 9-15 * * MON-FRI")
	public void  task4() throws Exception {
		updateCache(list4,pool4);
	}
	@Scheduled(cron = "0/30 * 9-15 * * MON-FRI")
	public void  task5() throws Exception {
		updateCache(list5,pool5);
	}
	
	//实时更新价格到cache
	private void updateCache(List<StockDo> stockList,ThreadPoolExecutor  pool) {
		if(!DateUtils.traceTime(guPiaoService.getHolidayList())) {
			return ;
		}
		for(StockDo stock:stockList) {
			try {
				pool.execute(new UpdateRealTimeTask(guPiaoService,stock.getNumber(),apiUrl,redisUtil));
			}catch (Exception e) {
				logger.warn("pool5 Exception:"+stock.getNumber()+":"+e.getMessage(),e);
			}
		}
	}
	
	
	
	@Scheduled(cron = "0 40 8,15,23 * * MON-FRI")
	public void  updateHistoryTask1() {
		//获取所有股票的历史60分钟数据
		for(StockDo stock:list1) {
			updateHistoryStock(stock,today,pool1);
		}
		for(StockDo stock:list2) {
			updateHistoryStock(stock,today,pool2);
		}
		for(StockDo stock:list3) {
			updateHistoryStock(stock,today,pool3);
		}
		for(StockDo stock:list4) {
			updateHistoryStock(stock,today,pool4);
		}
		for(StockDo stock:list5) {
			updateHistoryStock(stock,today,pool5);
		}
	}
	
	private void updateHistoryStock(final StockDo stock,final String today,final ThreadPoolExecutor  pool) {
		pool.execute(new Runnable() {
			@Override
			public void run() {
				String key=RedisKeyUtil.getRecheckStock(stock.getNumber());
				if(!redisUtil.hasKey(key)) {
					logger.info("更新数据(补60分钟线)--->"+stock.getNumber()+" "+redisUtil.get(RedisKeyUtil.getStockName(stock.getNumber()))+" "+today);
					guPiaoService.updateHistoryStock(stock.getNumber());
					guPiaoService.timeInterval(stock.getNumber());
					redisUtil.set(key, true,3500);
				}
			}
		});
	}

	
}
