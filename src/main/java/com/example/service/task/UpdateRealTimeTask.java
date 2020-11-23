package com.example.service.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.GuPiao;
import com.example.model.RealTimeDo;
import com.example.service.GuPiaoService;
import com.example.uitls.ReadApiUrl;

public class UpdateRealTimeTask  implements Runnable {

	private String number;
	private GuPiaoService guPiaoService;
	private static Logger logger = LoggerFactory.getLogger("real_time_monitor");
	private ReadApiUrl apiUrl;
	
	@Override
	public void run() {
			try {
				GuPiao date=apiUrl.readUrl(number,true);
				if(date !=null && guPiaoService!=null) {
					logger.info("写入数据库"+number);
					RealTimeDo model=new RealTimeDo();
					BeanUtils.copyProperties(date, model);
					guPiaoService.realTimeInsert(model);
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


	
}
