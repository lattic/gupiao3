package com.example.chart.entity;


import java.util.ArrayList;
import java.util.List;

/**
 * @author Hugh.HYS
 * @date 2018/11/13
 */
public class RSIEntity implements ChartEntity {


    public String indexDes;
    public List<EntryType> rsiDataList;

    public RSIEntity() {
        this.rsiDataList = new ArrayList<>();
    }

    @Override
    public void clearValues() {
        rsiDataList.clear();
    }
}
