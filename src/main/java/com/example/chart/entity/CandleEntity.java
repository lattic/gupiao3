package com.example.chart.entity;


import java.util.ArrayList;
import java.util.List;

import com.example.chart.base.entity.Candle;

/**
 * @author Hugh.HYS
 * @date 2018/11/12
 */
public class CandleEntity implements ChartEntity {
    public List<Candle> candleEntries;
//    public ArrayList<Entry> ma5DataOfCandle;
//    public ArrayList<Entry> ma10DataOfCandle;
//    public ArrayList<Entry> ma20DataOfCandle;
    public List<EntryType> maDataOfCandleList;
    public CandleEntity() {
        candleEntries = new ArrayList<>();
//        ma5DataOfCandle = new ArrayList<>();
//        ma10DataOfCandle = new ArrayList<>();
//        ma20DataOfCandle = new ArrayList<>();
        maDataOfCandleList = new ArrayList<>();
    }



    @Override
    public void clearValues() {
        if (candleEntries != null) {
            candleEntries.clear();
        }

        if (maDataOfCandleList != null) {
            maDataOfCandleList.clear();
        }
//        if (ma5DataOfCandle != null) {
//            ma5DataOfCandle.clear();
//        }
//        if (ma10DataOfCandle != null) {
//            ma10DataOfCandle.clear();
//        }
//        if (ma20DataOfCandle != null) {
//            ma20DataOfCandle.clear();
//        }
    }
}
