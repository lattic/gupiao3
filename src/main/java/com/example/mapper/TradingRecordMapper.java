package com.example.mapper;

import java.util.List;

import com.example.model.TradingRecordDo;

/**
 * com.example.mappe.GuPiaoMapper
 * @author king
 *
 */
public interface TradingRecordMapper {

	
	List<TradingRecordDo> getAll();
	
	int insert(TradingRecordDo obj);

	int delete(Long id);
}
