package com.example.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HistoryPriceListDo implements Serializable  {
	private static final long serialVersionUID = -6232921205133551196L;
	
	List<String> record;
	
	public HistoryPriceListDo() {
		record=new ArrayList<String>();
	}
	
	public List<String> getRecord() {
		return record;
	}

	public void setRecord(List<String> record) {
		this.record = record;
	}

	
}
