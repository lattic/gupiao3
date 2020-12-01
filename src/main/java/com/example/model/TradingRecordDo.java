package com.example.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class TradingRecordDo implements Serializable  {

	private static final long serialVersionUID = 6244267443156745046L;
	public static final Integer options_nothink=0;
	public static final Integer options_buy=1;
	public static final Integer options_sell=2;
	
	private Long id;
	
	private Date createDate;
	
	private String number;
	
	private String name;
	
	private String dtId;
	
	private BigDecimal total;
	
	private BigDecimal price;
	
	private Integer num;
	/**
	 * 0=不操作
	 * 1=买入
	 * 2=卖出
	 */
	private Integer options;
	
	private String remark;

	
	public TradingRecordDo(Date createDate, String number, String name, String dtId, BigDecimal price, Integer num,
			Integer options, String remark) {
		super();
		this.createDate = createDate;
		this.number = number;
		this.name = name;
		this.dtId = dtId;
		this.price = price;
		this.num = num;
		this.options = options;
		this.remark = remark;
		this.total=price.multiply(new BigDecimal(num));
		this.price=this.price.setScale(2, BigDecimal.ROUND_UP);
		this.total=this.total.setScale(2, BigDecimal.ROUND_UP);
	}

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
