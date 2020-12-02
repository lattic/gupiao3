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
	private static Logger logger = LoggerFactory.getLogger("real_time");
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
					if(model.getTop()<=0.1||model.getLow()<=0.1||model.getKaipanjia()<=0.1||model.getZuorishoupanjia()<=0.1||model.getChengjiaogupiao()<=0.1) {
						logger.warn("异常数据："+model.getName()+" "+model.getNumber()+" 最高价:"+model.getTop()+" 最低价："+model.getLow()+" 开盘价："+model.getKaipanjia()+" 收盘价："+model.getZuorishoupanjia()+" 成交量："+model.getChengjiaogupiao());
						return ;
					}
					BeanUtils.copyProperties(date, model);
					String key = RedisKeyUtil.getRealTimeByRealTimeDo(model);
					if(redisUtil.hasKey(key)) {
						logger.info("读取缓存:"+number+" "+model.getName()+" 时间:"+model.getDate()+" "+model.getTime()+" 当前价格:"+model.getDangqianjiage());
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
					logger.info("写入缓存成功:"+number+" "+model.getName()+" 时间:"+model.getDate()+" "+model.getTime()+" 当前价格:"+model.getDangqianjiage());
				}else {
					logger.error("查询失败:"+number);
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
