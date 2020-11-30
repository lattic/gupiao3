package com.example.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class TradingRecordDo implements Serializable  {

	private static final long serialVersionUID = 6244267443156745046L;

	private Long id;
	
	private Date createDate;
	
	private String number;
	
	private String name;
	
	private String dtId;
	
	private BigDecimal total;
	
	private BigDecimal price;
	
	private Integer num;
	
	private Integer options;
	
	private String remark;

	public TradingRecordDo() {
	}
	
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

	public String getDtId() {
		return dtId;
	}

	public void setDtId(String dtId) {
		this.dtId = dtId;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}


	public Integer getOptions() {
		return options;
	}

	public void setOptions(Integer options) {
		this.options = options;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
}
