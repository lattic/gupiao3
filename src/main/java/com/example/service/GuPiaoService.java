package com.example.service;

import java.util.List;

import com.example.model.GuPiaoDo;
import com.example.model.RealTimeDo;

public interface GuPiaoService {
	boolean realTimeInsert(RealTimeDo model);
	boolean guPiaoInsert(GuPiaoDo model);
	List<GuPiaoDo> listAll();
}
