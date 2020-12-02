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
		if(!DateUtils.traceTime(guPiaoService.getHolidayList())) {
			return ;
		}
		for(StockDo stock:list1) {
			try {
				pool1.execute(new UpdateRealTimeTask(guPiaoService,stock.getNumber(),apiUrl,redisUtil));
			}catch (Exception e) {
				logger.warn("pool1 Exception:"+stock.getNumber()+":"+e.getMessage(),e);
			}
		}
	}
	@Scheduled(cron = "0/30 * 9-15 * * MON-FRI")
	public void  task2() throws Exception {
		if(!DateUtils.traceTime(guPiaoService.getHolidayList())) {
			return ;
		}
		for(StockDo stock:list2) {
			try {
				pool2.execute(new UpdateRealTimeTask(guPiaoService,stock.getNumber(),apiUrl,redisUtil));
			}catch (Exception e) {
				logger.warn("pool2 Exception:"+stock.getNumber()+":"+e.getMessage(),e);
			}
		}
	}
	@Scheduled(cron = "0/30 * 9-15 * * MON-FRI")
	public void  task3() throws Exception {
		if(!DateUtils.traceTime(guPiaoService.getHolidayList())) {
			return ;
		}
		for(StockDo stock:list3) {
			try {
				pool3.execute(new UpdateRealTimeTask(guPiaoService,stock.getNumber(),apiUrl,redisUtil));
			}catch (Exception e) {
				logger.warn("pool3 Exception:"+stock.getNumber()+":"+e.getMessage(),e);
			}
		}
	}
	@Scheduled(cron = "0/30 * 9-15 * * MON-FRI")
	public void  task4() throws Exception {
		if(!DateUtils.traceTime(guPiaoService.getHolidayList())) {
			return ;
		}
		for(StockDo stock:list4) {
			try {
				pool4.execute(new UpdateRealTimeTask(guPiaoService,stock.getNumber(),apiUrl,redisUtil));
			}catch (Exception e) {
				logger.warn("pool4 Exception:"+stock.getNumber()+":"+e.getMessage(),e);
			}
		}
	}
	@Scheduled(cron = "0/30 * 9-15 * * MON-FRI")
	public void  task5() throws Exception {
		if(!DateUtils.traceTime(guPiaoService.getHolidayList())) {
			return ;
		}
		for(StockDo stock:list5) {
			try {
				pool5.execute(new UpdateRealTimeTask(guPiaoService,stock.getNumber(),apiUrl,redisUtil));
			}catch (Exception e) {
				logger.warn("pool5 Exception:"+stock.getNumber()+":"+e.getMessage(),e);
			}
		}
	}
	
	private void updateHistoryStock(StockDo stock,String today,ThreadPoolExecutor  pool) {
		pool.execute(new Runnable() {
			@Override
			public void run() {
				String key=RedisKeyUtil.getRecheckStock(stock.getNumber());
				if(!redisUtil.hasKey(key)) {
					logger.info("更新数据(补60分钟线)--->"+stock.getNumber()+" "+redisUtil.get(RedisKeyUtil.getStockName(stock.getNumber()))+" "+today);
					guPiaoService.updateHistoryStock(stock.getNumber());
					guPiaoService.timeInterval(stock.getNumber());
					redisUtil.set(key, true);
				}
			}
		});
	}
	
	@Scheduled(cron = "0 50 8,12 * * MON-FRI")
	public void  updateHistoryTask1() {
		today=DateUtils.getToday();
		//获取所有股票的历史60分钟数据
		for(StockDo stock:list1) {
			updateHistoryStock(stock,today,pool1);
		}
	}
	
	
	@Scheduled(cron = "5 50 8,12 * * MON-FRI")
	public void  updateHistoryTask2() {
		today=DateUtils.getToday();
		//获取所有股票的历史60分钟数据
		for(StockDo stock:list2) {
			updateHistoryStock(stock,today,pool2);
		}
	}
	
	@Scheduled(cron = "10 50 8,12 * * MON-FRI")
	public void  updateHistoryTask3() {
		today=DateUtils.getToday();
		//获取所有股票的历史60分钟数据
		for(StockDo stock:list3) {
			updateHistoryStock(stock,today,pool3);
		}
	}
	
	@Scheduled(cron = "15 50 8,12 * * MON-FRI")
	public void  updateHistoryTask4() {
		today=DateUtils.getToday();
		//获取所有股票的历史60分钟数据
		for(StockDo stock:list4) {
			updateHistoryStock(stock,today,pool4);
		}
	}
	
	@Scheduled(cron = "20 50 8,12 * * MON-FRI")
	public void  updateHistoryTask5() {
		today=DateUtils.getToday();
		//获取所有股票的历史60分钟数据
		for(StockDo stock:list5) {
			updateHistoryStock(stock,today,pool5);
		}
	}
	

	
}
