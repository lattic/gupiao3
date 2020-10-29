package com.example.model;

import java.io.Serializable;
import java.util.Date;

public class SubscriptionDo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2022773262678015013L;
	
	private Long id;
	
	private String number;
	
	private String dingtalkId;
	
	private Date createDate;
	
	private String begintime;
	
	private String remark;
	
	private Integer status;

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

	public String getDingtalkId() {
		return dingtalkId;
	}

	public void setDingtalkId(String dingtalkId) {
		this.dingtalkId = dingtalkId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getBegintime() {
		return begintime;
	}

	public void setBegintime(String begintime) {
		this.begintime = begintime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
