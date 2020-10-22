package com.example.model;

import java.math.BigDecimal;
import java.util.Date;

public class HistoryPriceDo {
	private Date dateime;
	private String number;
	private String name;
	private BigDecimal kaipanjia;
	private BigDecimal zuigaojia;
	private BigDecimal shoupanjia;
	private BigDecimal zuidijia;
	private BigDecimal chengjiaoliang;
	private BigDecimal jiagebiandong;
	private BigDecimal zhangdiefu;
	private BigDecimal ma5;
	private BigDecimal ma10;
	private BigDecimal ma20;
	private BigDecimal ma5number;
	private BigDecimal ma10number;
	private BigDecimal ma20number;
	private BigDecimal huanshoulv;
	private boolean up=false;
	private int powerValue;
	private BigDecimal yaliwei;
	private BigDecimal zhichengwei;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public BigDecimal getYaliwei() {
		return yaliwei;
	}
	public void setYaliwei(BigDecimal yaliwei) {
		this.yaliwei = yaliwei;
	}
	public BigDecimal getZhichengwei() {
		return zhichengwei;
	}
	public void setZhichengwei(BigDecimal zhichengwei) {
		this.zhichengwei = zhichengwei;
	}
	//(MA20/收盘价)*100
	private BigDecimal pianlizhi;
	
	public BigDecimal getPianlizhi() {
		return pianlizhi;
	}
	public void setPianlizhi(BigDecimal pianlizhi) {
		this.pianlizhi = pianlizhi;
	}
	public int getPowerValue() {
		return powerValue;
	}
	public void setPowerValue(int powerValue) {
		this.powerValue = powerValue;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public boolean isUp() {
		return up;
	}
	public void setUp(boolean up) {
		this.up = up;
	}
	public Date getDateime() {
		return dateime;
	}
	public void setDateime(Date dateime) {
		this.dateime = dateime;
	}
	public BigDecimal getKaipanjia() {
		return kaipanjia;
	}
	public void setKaipanjia(BigDecimal kaipanjia) {
		this.kaipanjia = kaipanjia;
	}
	public BigDecimal getZuigaojia() {
		return zuigaojia;
	}
	public void setZuigaojia(BigDecimal zuigaojia) {
		this.zuigaojia = zuigaojia;
	}
	public BigDecimal getShoupanjia() {
		return shoupanjia;
	}
	public void setShoupanjia(BigDecimal shoupanjia) {
		this.shoupanjia = shoupanjia;
	}
	public BigDecimal getZuidijia() {
		return zuidijia;
	}
	public void setZuidijia(BigDecimal zuidijia) {
		this.zuidijia = zuidijia;
	}
	public BigDecimal getChengjiaoliang() {
		return chengjiaoliang;
	}
	public void setChengjiaoliang(BigDecimal chengjiaoliang) {
		this.chengjiaoliang = chengjiaoliang;
	}
	public BigDecimal getJiagebiandong() {
		return jiagebiandong;
	}
	public void setJiagebiandong(BigDecimal jiagebiandong) {
		this.jiagebiandong = jiagebiandong;
	}
	public BigDecimal getZhangdiefu() {
		return zhangdiefu;
	}
	public void setZhangdiefu(BigDecimal zhangdiefu) {
		this.zhangdiefu = zhangdiefu;
	}
	public BigDecimal getMa5() {
		return ma5;
	}
	public void setMa5(BigDecimal ma5) {
		this.ma5 = ma5;
	}
	public BigDecimal getMa10() {
		return ma10;
	}
	public void setMa10(BigDecimal ma10) {
		this.ma10 = ma10;
	}
	public BigDecimal getMa20() {
		return ma20;
	}
	public void setMa20(BigDecimal ma20) {
		this.ma20 = ma20;
	}
	public BigDecimal getMa5number() {
		return ma5number;
	}
	public void setMa5number(BigDecimal ma5number) {
		this.ma5number = ma5number;
	}
	public BigDecimal getMa10number() {
		return ma10number;
	}
	public void setMa10number(BigDecimal ma10number) {
		this.ma10number = ma10number;
	}
	public BigDecimal getMa20number() {
		return ma20number;
	}
	public void setMa20number(BigDecimal ma20number) {
		this.ma20number = ma20number;
	}
	public BigDecimal getHuanshoulv() {
		return huanshoulv;
	}
	public void setHuanshoulv(BigDecimal huanshoulv) {
		this.huanshoulv = huanshoulv;
	}
	
}
