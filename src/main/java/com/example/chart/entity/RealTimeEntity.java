package com.example.chart.entity;


import java.util.ArrayList;

import com.example.chart.base.entity.Entry;

/**
 * @author Hugh.HYS
 * @date 2018/11/12
 */
public class RealTimeEntity implements ChartEntity {
    public ArrayList<Entry> price;
    public ArrayList<Entry> priceMa;

    public RealTimeEntity() {
        this.price = new ArrayList<>();
        this.priceMa = new ArrayList<>();
    }

    @Override
    public void clearValues() {
        if (price != null) {
            price.clear();
        }
        if (priceMa != null) {
            priceMa.clear();
        }
    }
}
