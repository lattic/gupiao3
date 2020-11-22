package com.example.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class HistoryDayStockDo implements Serializable {

	private static final long serialVersionUID = 8195305329250487915L;

	private Long id;
	
	private String number;
	
	private String historyDay;
	
	private BigDecimal open; 
	
	private BigDecimal close;
	
	private BigDecimal high;
	
	private BigDecimal low;
	
	private BigDecimal avg;
	
	private BigDecimal ma5Day;
	
	private BigDecimal ma20Day;
	
	private BigDecimal ma200Day;
	
	//波段类型 1=上升 2=下跌 3=震荡
	private Integer type;
	
	private BigDecimal boxMax;
	
	private BigDecimal boxMin;
	
	private BigDecimal boxAvg;
	
	private String remark;

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

	public BigDecimal getOpen() {
		return open;
	}

	public void setOpen(BigDecimal open) {
		this.open = open;
	}

	public BigDecimal getClose() {
		return close;
	}

	public void setClose(BigDecimal close) {
		this.close = close;
	}

	public BigDecimal getHigh() {
		return high;
	}

	public void setHigh(BigDecimal high) {
		this.high = high;
	}

	public BigDecimal getLow() {
		return low;
	}

	public void setLow(BigDecimal low) {
		this.low = low;
	}

	public BigDecimal getAvg() {
		return avg;
	}

	public void setAvg(BigDecimal avg) {
		this.avg = avg;
	}


	public BigDecimal getMa5Day() {
		return ma5Day;
	}

	public void setMa5Day(BigDecimal ma5Day) {
		this.ma5Day = ma5Day;
	}

	public BigDecimal getMa20Day() {
		return ma20Day;
	}

	public void setMa20Day(BigDecimal ma20Day) {
		this.ma20Day = ma20Day;
	}

	public BigDecimal getMa200Day() {
		return ma200Day;
	}

	public void setMa200Day(BigDecimal ma200Day) {
		this.ma200Day = ma200Day;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
}
