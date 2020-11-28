package com.example.chart.entity;
import java.util.ArrayList;
import java.util.List;

import com.example.chart.base.entity.BarEntry;

public class VolEntity implements ChartEntity {

    public List<BarEntry> bars;

    public VolEntity() {
        this.bars = new ArrayList<>();
    }

    @Override
    public void clearValues() {
        if (bars != null) {
            bars.clear();
        }
    }
}
