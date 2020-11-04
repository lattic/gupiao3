package com.example.mapper;

import java.util.List;

import com.example.model.HistoryStockDo;

/**
 * com.example.mappe.GuPiaoMapper
 * @author king
 *
 */
public interface HistoryStockMapper {

	HistoryStockDo getByTime(HistoryStockDo obj);
	
	List<HistoryStockDo> getAll();

	int insert(HistoryStockDo obj);

	int delete(Long id);
}
