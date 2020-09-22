package com.example.mapper;

import java.util.List;

import com.example.model.GuPiaoDo;

/**
 * com.example.mappe.GuPiaoMapper
 * @author king
 *
 */
public interface GuPiaoMapper {

	
	List<GuPiaoDo> getAll();
	
	GuPiaoDo getNumber(String number);

	int insert(GuPiaoDo obj);

	int delete(Long id);
}
