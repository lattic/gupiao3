package com.example.service;

import java.util.List;

import com.example.model.GuPiaoDo;
import com.example.model.HistoryPriceDo;
import com.example.model.HistoryStockDo;
import com.example.model.HolidayDo;
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
	
	
	
	List<HolidayDo> getHolidayList();
	
	
	
	HistoryPriceDo getLastZhichengwei(String number);
	
	
	//更新到数据库
	void updateHistoryStock(String number);
	
	//分析数据库的60分钟时间计算波段
	void timeInterval(String number);
	
	/**
	 * 获取波段分析的最后数值
	 * @param number
	 * @param size 获取最后的波段
	 * @return
	 */
	List<HistoryStockDo> getLastHistoryStock(String number,Integer size);
}
