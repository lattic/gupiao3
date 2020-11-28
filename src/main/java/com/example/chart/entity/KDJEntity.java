package com.example.chart.entity;

import java.util.ArrayList;
import java.util.List;

import com.example.chart.base.entity.Entry;

/**
 */
public class KDJEntity implements ChartEntity {

    public List<Entry> k;
    public List<Entry> d;
    public List<Entry> j;
    public String  indexDes;

    public KDJEntity() {
        this.k = new ArrayList<>();
        this.d = new ArrayList<>();
        this.j = new ArrayList<>();
    }

    @Override
    public void clearValues() {
        if (k != null) {
            k.clear();
        }
        if (d != null) {
            d.clear();
        }
        if (j != null) {
            j.clear();
        }
    }
}
