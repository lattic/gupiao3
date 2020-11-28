package com.example.chart.base.entity;

/**
 * Created by Rex on 2019/4/4.
 * 一般为你所使用的图标工具自带
 */
public class Entry {
    private String x;
    private double y;
    private Object obj;

    public Entry() {
    }

    public Entry(String x, double y) {
        this.x = x;
        this.y = y;
    }

    public Entry(String x, double y, Object obj) {
        this.x = x;
        this.y = y;
        this.obj = obj;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Object getData() {
        return obj;
    }

    public void setData(Object obj) {
        this.obj = obj;
    }
}
