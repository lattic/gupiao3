package com.example.service;

import java.util.List;

import com.example.model.TradingRecordDo;

public interface MockTradingService {
	
	boolean isBuy(String number,String robotId);
	
	boolean buyIn(TradingRecordDo tr,String robotId);
	
	boolean sellOut(TradingRecordDo tr,String robotId);
	
	List<TradingRecordDo> showList(String number,String robotId);
}
