package com.example.chart.entity;


import java.util.ArrayList;
import java.util.List;

import com.example.chart.base.entity.Entry;

/**
 * Created by Rex on 2018/11/14.
 */
public class SAREntity implements ChartEntity {

    public List<Entry> sar;
    public int Tn = 2;


    public SAREntity(List<Entry> sar) {
        this.sar = sar;
    }

    public SAREntity() {
        this.sar = new ArrayList<>();
    }

    @Override
    public void clearValues() {
        if (sar != null) {
            sar.clear();
        }
    }


}
