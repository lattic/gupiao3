package com.example.mapper;

import java.util.List;

import com.example.model.RealTimeDo;

/**
 * com.example.mappe.GuPiaoMapper
 * @author king
 *
 */
public interface RealTimeMapper {

	
	List<RealTimeDo> getAll();
	
	RealTimeDo getOne(Long id);

	int insert(RealTimeDo obj);

	int delete(Long id);
}
