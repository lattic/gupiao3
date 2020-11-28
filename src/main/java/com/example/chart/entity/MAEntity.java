package com.example.chart.entity;


import java.util.ArrayList;
import java.util.List;

import com.example.chart.base.entity.Entry;

/**
 * Created by Rex on 2018/11/14.
 */
public class MAEntity implements ChartEntity {

    public List<Entry> ma;


    public MAEntity(List<Entry> ma) {
        this.ma = ma;
    }

    public MAEntity() {
        this.ma = new ArrayList<>();
    }
    @Override
    public void clearValues() {
        if (ma != null) {
            ma.clear();
        }
    }


}
