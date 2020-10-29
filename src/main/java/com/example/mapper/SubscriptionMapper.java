package com.example.mapper;

import java.util.List;

import com.example.model.SubscriptionDo;

public interface SubscriptionMapper {
	
	List<SubscriptionDo> getAll();
	
	int insert(SubscriptionDo obj);

	int delete(Long id);
}
