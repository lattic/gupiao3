package com.example.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class RobotAccountDo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8195305329250487915L;

	private Long id;
	
	private BigDecimal total;
	
	private Long robotId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public Long getRobotId() {
		return robotId;
	}

	public void setRobotId(Long robotId) {
		this.robotId = robotId;
	}
	
	
}
