package com.example.service.task;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
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
import com.example.model.StockDo;
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
	
	//股票名称
	public static ConcurrentHashMap<String, StockDo> stockMap=new ConcurrentHashMap<String, StockDo>();
	
	
	@Scheduled(cron = "0 0 0/1 * * *")
	private void init() {
		pool.execute(new Runnable() {
			@Override
			public void run() {
				getAllGuPiao();
			}
		});
	}
	
	//更新股票池
	private void  getAllGuPiao() {
		for(int i=3330;i<=99999;i++) {
			System.out.println(new Date()+" ==>"+i);
			GuPiao date=ReadUrl.readUrl(i, "sz0",false);
			if(date !=null) {
				GuPiaoDo model=new GuPiaoDo();
				BeanUtils.copyProperties(date, model);
				System.out.println(new Date()+" ==>"+model.getNumber()+" "+model.getName());
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
				System.out.println(new Date()+" ==>"+model.getNumber()+" "+model.getName());
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
				System.out.println(new Date()+" ==>"+model.getNumber()+" "+model.getName());
				guPiaoService.updateStock(model.getNumber(),model.getName(), 1) ;
				StockDo stock=stockMap.get(model.getNumber());
		    	if(stock != null) {
		    		stockMap.put(model.getNumber(), stock);
		    	}
			}
		}
	}
	

	
	@Override
	public void afterPropertiesSet() throws Exception {
		Date now=new Date();
    	SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String robotbuy = MessageFormat.format("【实时监听启动】"+dateformat.format(now),new Object[] {});
        DingTalkRobotHTTPUtil.sendMsg(DingTalkRobotHTTPUtil.APP_TEST_SECRET, robotbuy, null, false);
	}
	
	
}
