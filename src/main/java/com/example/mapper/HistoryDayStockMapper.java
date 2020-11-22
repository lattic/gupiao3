package com.example.mapper;

import java.util.List;

import com.example.model.HistoryDayStockDo;

/**
 * com.example.mappe.GuPiaoMapper
 * @author king
 *
 */
public interface HistoryDayStockMapper {

	HistoryDayStockDo getByTime(HistoryDayStockDo obj);
	
	List<HistoryDayStockDo> getAll();
	
	List<HistoryDayStockDo> getNumber(String number);
	
	int insert(HistoryDayStockDo obj);

	int delete(HistoryDayStockDo id);
	
	int updateHistoryStock(HistoryDayStockDo obj);
}
