package com.example.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.mapper.GuPiaoMapper;
import com.example.mapper.RealTimeMapper;
import com.example.mapper.RobotAccountMapper;
import com.example.mapper.RobotSetMapper;
import com.example.mapper.StockMapper;
import com.example.mapper.SubscriptionMapper;
import com.example.mapper.TradingRecordMapper;
import com.example.model.GuPiaoDo;
import com.example.model.RealTimeDo;
import com.example.model.RobotAccountDo;
import com.example.model.RobotSetDo;
import com.example.model.StockDo;
import com.example.model.SubscriptionDo;
import com.example.model.TradingRecordDo;

@Service
public class GuPiaoServiceImpl implements GuPiaoService,InitializingBean {

	private static Logger logger = LoggerFactory.getLogger(GuPiaoServiceImpl.class);
	
	@Autowired
	private GuPiaoMapper guPiaoMapper;
	
	@Autowired
	private RealTimeMapper realTimeMapper;
	
	@Autowired
	private StockMapper stockMapper;
	
	@Autowired
	private SubscriptionMapper subscriptionMapper;
	
	@Autowired
	private RobotSetMapper robotSetMapper;
	@Autowired
	private RobotAccountMapper robotAccountMapper;
	@Autowired
	private TradingRecordMapper tradingRecordMapper;
	

	@Override
	public boolean realTimeInsert(RealTimeDo model) {
		try {
			int i=realTimeMapper.insert(model);
			return i>0 ? true:false;
		}catch(Exception ex) {
			
		}
		return false;
	}

	@Override
	public boolean guPiaoInsert(GuPiaoDo model) {
		GuPiaoDo data=guPiaoMapper.getNumber(model.getNumber());
		if(data!=null) {
			guPiaoMapper.delete(data.getId());
		}
		int i=guPiaoMapper.insert(model);
		return i>0 ? true:false;
	}

	@Override
	public List<GuPiaoDo> listAll() {
		return guPiaoMapper.getAll();
	}
	
	@Override
	public StockDo getNumber(String number) {
		try {
			return stockMapper.getNumber(number);
		}catch(Exception ex) {
		}
		return null;
	}
	

	@Override
	public void afterPropertiesSet() throws Exception {
		
	}

	@Override
	public void updateStock(String number, String name, int type) {
		StockDo obj=stockMapper.getNumber(number);
		if(obj == null) {
			obj=new StockDo();
			obj.setCreateDate(new Date());
			obj.setNumber(number);
			obj.setName(name);
			obj.setType(type);
			obj.setStatus(1);
			stockMapper.insert(obj);
		}
	}

	@Override
	public List<StockDo> getAllStock() {
		return stockMapper.getAll();
	}

	@Override
	public List<SubscriptionDo> listMemberAll() {
		try {
			return subscriptionMapper.getAll();
		}catch(Exception ex) {
			logger.error(ex.getMessage());
		}
		return null; 
	}

}
