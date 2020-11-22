package com.example.model.ths;

import java.util.List;


public class Tables {
	private String thscode;
	private List<String> time;
	private Table table;
	public String getThscode() {
		return thscode;
	}
	public void setThscode(String thscode) {
		this.thscode = thscode;
	}
	public List<String> getTime() {
		return time;
	}
	public void setTime(List<String> time) {
		this.time = time;
	}
	public Table getTable() {
		return table;
	}
	public void setTable(Table table) {
		this.table = table;
	}
}
