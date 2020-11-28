package com.example.chart.entity;



import java.util.ArrayList;
import java.util.List;

import com.example.chart.base.entity.Entry;

/**
 * Created by Rex on 2018/11/13.
 */
public class BollEntity implements ChartEntity {
    //存储上轨数据
    private List<Entry> upList;
    //存储中轨数据
    private List<Entry> midList;
    //存储下轨数据
    private List<Entry> lowerList;

    public BollEntity(List<Entry> upList, List<Entry> midList, List<Entry> lowerList) {
        this.upList = upList;
        this.midList = midList;
        this.lowerList = lowerList;
    }

    public BollEntity() {
        this.upList = new ArrayList<>();
        this.midList = new ArrayList<>();
        this.lowerList = new ArrayList<>();
    }

    @Override
    public void clearValues() {
        if (upList != null) {
            upList.clear();
        }
        if (midList != null) {
            midList.clear();
        }
        if (lowerList != null) {
        	lowerList.clear();
        }
    }
}
