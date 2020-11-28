package com.example.model;

import java.math.BigDecimal;

public class StockPriceVo {
	
	private String number;
	
	private String name;
	
	private String historyDay;
	
	private String historyAll;
	
	private BigDecimal open; 
	
	private BigDecimal close;
	
	private BigDecimal high;
	
	private BigDecimal low;
	
	private BigDecimal avg;
	
	private BigDecimal ma20hour;
	
	private BigDecimal ma5Day;
	
	private BigDecimal ma20Day;
	
	private BigDecimal boxMax;
	
	private BigDecimal boxMin;
	
	private BigDecimal boxAvg;
	
	private Long volume;
	
	private String remark;

	public Long getVolume() {
		return volume;
	}

	public void setVolume(Long volume) {
		this.volume = volume;
	}

	public BigDecimal getMa20hour() {
		return ma20hour;
	}

	public void setMa20hour(BigDecimal ma20hour) {
		this.ma20hour = ma20hour;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHistoryDay() {
		return historyDay;
	}

	public void setHistoryDay(String historyDay) {
		this.historyDay = historyDay;
	}

	public String getHistoryAll() {
		return historyAll;
	}

	public void setHistoryAll(String historyAll) {
		this.historyAll = historyAll;
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
