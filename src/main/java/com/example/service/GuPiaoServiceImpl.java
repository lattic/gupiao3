package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.mapper.GuPiaoMapper;
import com.example.mapper.RealTimeMapper;
import com.example.model.GuPiaoDo;
import com.example.model.RealTimeDo;

@Service
public class GuPiaoServiceImpl implements GuPiaoService {

	@Autowired
	private GuPiaoMapper guPiaoMapper;
	
	@Autowired
	private RealTimeMapper realTimeMapper;
	

	@Override
	public boolean realTimeInsert(RealTimeDo model) {
		try {
			int i=realTimeMapper.insert(model);
			return i>0 ? true:false;
		}catch(Exception ex) {
			
		}
		return false;
	}

	@Override
	public boolean guPiaoInsert(GuPiaoDo model) {
		GuPiaoDo data=guPiaoMapper.getNumber(model.getNumber());
		if(data!=null) {
			guPiaoMapper.delete(data.getId());
		}
		int i=guPiaoMapper.insert(model);
		return i>0 ? true:false;
	}

	@Override
	public List<GuPiaoDo> listAll() {
		return guPiaoMapper.getAll();
	}

}
