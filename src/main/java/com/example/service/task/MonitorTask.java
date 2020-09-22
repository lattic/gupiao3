package com.example.service.task;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.demo.GuPiao;
import com.example.model.GuPiaoDo;
import com.example.model.RealTimeDo;
import com.example.service.GuPiaoService;
import com.example.uitls.ReadUrl;

@Service
public class MonitorTask implements InitializingBean {
	private static Logger logger = LoggerFactory.getLogger(MonitorTask.class);
	ThreadPoolExecutor  pool = new ThreadPoolExecutor(20, 100, 1,TimeUnit.SECONDS,
			new LinkedBlockingDeque<Runnable>(1000), 
			Executors.defaultThreadFactory(), 
			new ThreadPoolExecutor.CallerRunsPolicy());
	
	static boolean updateAll=true;
	static boolean updateReal=true;
	@Autowired
	private GuPiaoService guPiaoService;
	
//	@Scheduled(cron = "0 0/5 * * * *")
	private void init() {
		pool.execute(new Runnable() {
			@Override
			public void run() {
				if(!updateAll) {
					System.out.println("已经有程序在执行！！！");
					return;
				}
				updateAll=false;
				getAllGuPiao();
			}
		});
	}
	
	private void  getAllGuPiao() {
		for(int i=0;i<=99999;i++) {
			System.out.println(new Date()+" i==>"+i);
			GuPiao date=ReadUrl.readUrl(i, "sz0",false);
			if(date !=null) {
				GuPiaoDo model=new GuPiaoDo();
				BeanUtils.copyProperties(date, model);
				guPiaoService.guPiaoInsert(model);
			}
			date=ReadUrl.readUrl(i, "sz3",false);
			if(date !=null) {
				GuPiaoDo model=new GuPiaoDo();
				BeanUtils.copyProperties(date, model);
				guPiaoService.guPiaoInsert(model);
			}
			date=ReadUrl.readUrl(i, "sh6",false);
			if(date !=null) {
				GuPiaoDo model=new GuPiaoDo();
				BeanUtils.copyProperties(date, model);
				guPiaoService.guPiaoInsert(model);
			}
		}
		updateAll=true;
	}
	
	
	
//	@Scheduled(cron = "0/3 * * * * *")
	private void  monitor() {
		GuPiao date=ReadUrl.readUrl(2117, "sz0",true);
		if(date !=null) {
			logger.info(date.toString());
			RealTimeDo model=new RealTimeDo();
			BeanUtils.copyProperties(date, model);
			guPiaoService.realTimeInsert(model);
		}
	}

	@Scheduled(cron = "0/5 * * * * *")
	private void  monitorAll() {
		if(!updateReal) {
			System.out.println("monitorAll 已经有程序在执行！！！");
			return;
		}
		updateReal=false;
		
		List<GuPiaoDo> list=guPiaoService.listAll();
		if(list == null || list.isEmpty()) {
			return ;
		}
		for(GuPiaoDo gupiao:list) {
			pool.execute(new Runnable() {
				@Override
				public void run() {
					UpdateRealTimeTask task=new UpdateRealTimeTask();
					task.setNumber(gupiao.getNumber());
					task.setGuPiaoService(guPiaoService);
					task.run();
				}
			});
		}
		
	}
	
	@Scheduled(cron = "0/5 * * * * *")
	private void status() {
		System.out.println(pool.isTerminated()+"_"+pool.getActiveCount()+"_"+pool.getCompletedTaskCount()+"_"+pool.getPoolSize());
		if(pool.getActiveCount()<1) {
			updateReal=true;
		}
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		monitorAll();
	}
	
	
}
