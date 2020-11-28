package com.example.service.task;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import com.example.demo.GuPiao;
import com.example.model.RealTimeDo;
import com.example.service.GuPiaoService;
import com.example.uitls.ReadApiUrl;
import com.example.uitls.RedisKeyUtil;
import com.example.uitls.RedisUtil;

public class UpdateRealTimeTask  implements Runnable {
	private static Logger logger = LoggerFactory.getLogger("real_time_monitor");
	private String number;
	private GuPiaoService guPiaoService;
	private ReadApiUrl apiUrl;
	private RedisUtil redisUtil;
	
	@Override
	public void run() {
			try {
				GuPiao date=apiUrl.readUrl(number,false);
				
				if(date !=null) {
					RealTimeDo model=new RealTimeDo();
					BeanUtils.copyProperties(date, model);
					String key = RedisKeyUtil.getRealTimeByRealTimeDo(model);
					if(redisUtil.hasKey(key)) {
						logger.info("已经存在:"+key);
						logger.info("已有缓存:"+key);
						return ;
					}
					redisUtil.set(key, model,60);
					String key2 =RedisKeyUtil.getRealTimeListByRealTimeDo(model);
					@SuppressWarnings("unchecked")
					Map<String,RealTimeDo> map=(Map<String,RealTimeDo>) redisUtil.get(key2);
					if(map == null) {
						map=new HashMap<String,RealTimeDo>();
					}
					map.put(model.getDate()+model.getTime(), model);
					redisUtil.set(key2, map,86000);
					String key3 =RedisKeyUtil.getRealTime(number);
					redisUtil.set(key3, date,30);
					logger.info("写入缓存:"+number);
				}
			} catch (Exception e) {
				logger.warn(e.getMessage(),e);
			}
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public GuPiaoService getGuPiaoService() {
		return guPiaoService;
	}

	public void setGuPiaoService(GuPiaoService guPiaoService) {
		this.guPiaoService = guPiaoService;
	}

	public UpdateRealTimeTask(GuPiaoService guPiaoService,String number,ReadApiUrl apiUrl,RedisUtil redisUtil) {
		this.guPiaoService = guPiaoService;
		this.number = number;
		this.apiUrl = apiUrl;
		this.redisUtil = redisUtil;
	}

	
}
