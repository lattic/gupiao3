package com.example.model.ths;

import java.util.List;


public class HistoryRsDate {
	private Integer errorcode;
	private String errmsg;
	private List<Tables> tables;
	public Integer getErrorcode() {
		return errorcode;
	}
	public void setErrorcode(Integer errorcode) {
		this.errorcode = errorcode;
	}
	public String getErrmsg() {
		return errmsg;
	}
	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}
	public List<Tables> getTables() {
		return tables;
	}
	public void setTables(List<Tables> tables) {
		this.tables = tables;
	}
}
