package com.example.model;

import java.io.Serializable;
import java.util.Date;

public class MockLog implements Serializable {
	private static final long serialVersionUID = 6523392249528549952L;
	private Date beginTime;
	private Date endTime;
	private String number;
	private String name;
	private Boolean isUP;
	private Integer powerValue;
	private String logs="";
	private Integer success=0;
	private Integer fail=0;
	private Double win;
	private Double winRate; 
	private HistoryPriceDo price;
	private Boolean isBuyin=false;
	private Date lastBuyin;
	
	public MockLog() {
	}

	public Date getLastBuyin() {
		return lastBuyin;
	}

	public void setLastBuyin(Date lastBuyin) {
		this.lastBuyin = lastBuyin;
	}

	public Boolean getIsBuyin() {
		return isBuyin;
	}

	public void setIsBuyin(Boolean isBuyin) {
		this.isBuyin = isBuyin;
	}

	public HistoryPriceDo getPrice() {
		return price;
	}

	public void setPrice(HistoryPriceDo price) {
		this.price = price;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
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

	public Boolean getIsUP() {
		return isUP;
	}

	public void setIsUP(Boolean isUP) {
		this.isUP = isUP;
	}

	public Integer getPowerValue() {
		return powerValue;
	}

	public void setPowerValue(Integer powerValue) {
		this.powerValue = powerValue;
	}

	public Double getWinRate() {
		return winRate;
	}

	public void setWinRate(Double winRate) {
		this.winRate = winRate;
	}

	public Integer getSuccess() {
		return success;
	}

	public void setSuccess(Integer success) {
		this.success = success;
	}

	public Integer getFail() {
		return fail;
	}

	public void setFail(Integer fail) {
		this.fail = fail;
	}

	public Double getWin() {
		return win;
	}

	public void setWin(Double win) {
		this.win = win;
	}

	public String getLogs() {
		return logs;
	}

	public void setLogs(String logs) {
		this.logs = logs;
	}
	
}
