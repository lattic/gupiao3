package com.example.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class HistoryStockDo implements Serializable {

	private static final long serialVersionUID = 8195305329250487915L;

	private Long id;
	
	private String number;
	
	private String historyDay;
	
	private String historyAll;
	
	private BigDecimal kaipanjia; 
	
	private BigDecimal shoupanjia;
	
	private BigDecimal ma20Hour;
	
	private BigDecimal ma20Day;
	
	private BigDecimal height;
	
	private BigDecimal low;
	
	//波段类型 1=上升 2=下跌 3=震荡
	private Integer type;
	
	private BigDecimal boxMax;
	
	private BigDecimal boxMin;
	
	private BigDecimal boxAvg;
	
	private String remark;
	
	public HistoryStockDo() {
	}
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public BigDecimal getBoxMax() {
		return boxMax;
	}

	public void setBoxMax(BigDecimal boxMax) {
		this.boxMax = boxMax;
	}

	public BigDecimal getBoxMin() {
		return boxMin;
	}

	public void setBoxMin(BigDecimal boxMin) {
		this.boxMin = boxMin;
	}

	public BigDecimal getBoxAvg() {
		return boxAvg;
	}

	public void setBoxAvg(BigDecimal boxAvg) {
		this.boxAvg = boxAvg;
	}

	public BigDecimal getHeight() {
		return height;
	}

	public void setHeight(BigDecimal height) {
		this.height = height;
	}

	public BigDecimal getLow() {
		return low;
	}

	public void setLow(BigDecimal low) {
		this.low = low;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getHistoryDay() {
		return historyDay;
	}

	public void setHistoryDay(String historyDay) {
		this.historyDay = historyDay;
	}

	public BigDecimal getKaipanjia() {
		return kaipanjia;
	}

	public void setKaipanjia(BigDecimal kaipanjia) {
		this.kaipanjia = kaipanjia;
	}

	public BigDecimal getShoupanjia() {
		return shoupanjia;
	}

	public void setShoupanjia(BigDecimal shoupanjia) {
		this.shoupanjia = shoupanjia;
	}

	public BigDecimal getMa20Hour() {
		return ma20Hour;
	}

	public void setMa20Hour(BigDecimal ma20Hour) {
		this.ma20Hour = ma20Hour;
	}

	public BigDecimal getMa20Day() {
		return ma20Day;
	}

	public void setMa20Day(BigDecimal ma20Day) {
		this.ma20Day = ma20Day;
	}

	public String getHistoryAll() {
		return historyAll;
	}

	public void setHistoryAll(String historyAll) {
		this.historyAll = historyAll;
	}
	
	
}
