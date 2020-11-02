package com.example.mapper;

import java.util.List;

import com.example.model.RobotSetDo;

/**
 * com.example.mappe.GuPiaoMapper
 * @author king
 *
 */
public interface RobotSetMapper {

	
	List<RobotSetDo> getAll();
	
	int insert(RobotSetDo obj);

	int delete(Long id);
}
