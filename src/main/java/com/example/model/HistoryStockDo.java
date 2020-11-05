package com.example.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class HistoryStockDo implements Serializable {

	/**
	 * 
	 */
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
