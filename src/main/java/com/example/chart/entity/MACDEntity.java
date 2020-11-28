package com.example.chart.entity;


import java.util.ArrayList;
import java.util.List;

import com.example.chart.base.entity.BarEntry;
import com.example.chart.base.entity.Entry;

/**
 * @author Hugh.HYS
 * @date 2018/11/13
 */
public class MACDEntity implements ChartEntity {
    public List<BarEntry> bar;
    public List<Entry> diff;
    public List<Entry> dea;
    public String indexDes;

    public MACDEntity() {
        this.bar = new ArrayList<com.example.chart.base.entity.BarEntry>();
        this.diff = new ArrayList<>();
        this.dea = new ArrayList<>();
    }

    @Override
    public void clearValues() {
        if (bar != null) {
            bar.clear();
        }
        if (diff != null) {
            diff.clear();
        }
        if (dea != null) {
            dea.clear();
        }
    }
}
