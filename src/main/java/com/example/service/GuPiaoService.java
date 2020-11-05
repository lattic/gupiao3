package com.example.service;

import java.util.List;

import com.example.model.GuPiaoDo;
import com.example.model.RealTimeDo;
import com.example.model.StockDo;
import com.example.model.SubscriptionDo;

public interface GuPiaoService {
	boolean realTimeInsert(RealTimeDo model);

	boolean guPiaoInsert(GuPiaoDo model);

	List<GuPiaoDo> listAll();

	void updateStock(String number, String name, int type);

	StockDo getNumber(String number);

	List<StockDo> getAllStock();
	
	List<SubscriptionDo> listMemberAll();
	
	void updateHistoryStock(String number);
}
