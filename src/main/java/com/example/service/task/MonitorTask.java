package com.example.service.task;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
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
import com.example.uitls.DingTalkRobotHTTPUtil;
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
	
	@Scheduled(cron = "0 0 0/2 * * *")
	private void init() {
		pool.execute(new Runnable() {
			@Override
			public void run() {
				if(!updateAll) {
					logger.info("已经有程序在执行！！！");
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
	private void  monitorAll() {
		if(!updateReal) {
			logger.info("monitorAll 已经有程序在执行！！！");
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
//					ReadUrl.readUrl(gupiao.getNumber(),60);
//					UpdateRealTimeTask task=new UpdateRealTimeTask();
//					task.setNumber(gupiao.getNumber());
//					task.setGuPiaoService(guPiaoService);
//					task.run();
				}
			});
		}
		
	}
	
//	@Scheduled(cron = "0/5 * * * * *")
	private void status() {
		logger.info("检查线程完成状态："+pool.getActiveCount());
		if(pool.getActiveCount()<1) {
			updateReal=true;
		}
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		monitorAll();
		Date now=new Date();
    	SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String robotbuy = MessageFormat.format("GS【实时监听启动】"+dateformat.format(now),new Object[] {});
        DingTalkRobotHTTPUtil.sendMsg(DingTalkRobotHTTPUtil.APP_SECRET, robotbuy, null, false);
	}
	
	
}
