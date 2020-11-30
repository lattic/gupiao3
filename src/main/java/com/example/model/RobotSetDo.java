package com.example.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class RobotSetDo implements Serializable  {

	private static final long serialVersionUID = 4330902221504993783L;

	private Long id;
	
	private String robotName;
	
	private String number;
	
	private String name;
	
	private String dtId;
	
	private Date beginTime;
	
	private Date endTime;
	
	private Boolean isExecutionSale;
	
	private BigDecimal stopLossesPrice;

	public RobotSetDo() {
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRobotName() {
		return robotName;
	}

	public void setRobotName(String robotName) {
		this.robotName = robotName;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getDtId() {
		return dtId;
	}

	public void setDtId(String dtId) {
		this.dtId = dtId;
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

	public boolean isExecutionSale() {
		return isExecutionSale;
	}

	public void setExecutionSale(boolean isExecutionSale) {
		this.isExecutionSale = isExecutionSale;
	}

	public BigDecimal getStopLossesPrice() {
		return stopLossesPrice;
	}

	public void setStopLossesPrice(BigDecimal stopLossesPrice) {
		this.stopLossesPrice = stopLossesPrice;
	}
	
	
	
}
