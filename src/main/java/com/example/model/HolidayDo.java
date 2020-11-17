package com.example.model;

import java.io.Serializable;
import java.util.Date;

public class HolidayDo implements Serializable {

	private Long id;
	
	private Date holiday;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getHoliday() {
		return holiday;
	}

	public void setHoliday(Date holiday) {
		this.holiday = holiday;
	}
	
	
}
