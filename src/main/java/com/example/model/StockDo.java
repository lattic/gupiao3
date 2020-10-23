package com.example.model;

import java.io.Serializable;
import java.util.Date;

public class StockDo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8012342983111079749L;
	
	private Long id ;
	private Date createDate;
	private String number;
	private String name;
	//1=上证 2=深圳 3=创业
	private Integer type;
	private Integer status;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
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
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
}
