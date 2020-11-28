package com.example.mapper;


import java.util.List;

import com.example.model.StockDo;

public interface StockMapper  {
	
	List<StockDo> getAll();
	
	int insert(StockDo obj);

	int delete(StockDo obj);
	
	StockDo getNumber(String string);
}
