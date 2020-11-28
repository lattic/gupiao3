package com.example.chart.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rex on 2018/11/13.
 */
public class WREntity implements ChartEntity {

    public List<EntryType> wrDataList;

    public String indexDes = "";


    @Override
    public void clearValues() {
        wrDataList.clear();

    }

    public WREntity() {
        this.wrDataList = new ArrayList<>();
    }


    public static void CandleParseWR(List<ICandle> candles, int index, int period) {

        ICandle currCandle = candles.get(index);
        float max6 = 0.0f, min6 = 0.0f;

        for (int k = index; k > index - period; k--) {
            if (k >= 0) {
                ICandle iCandle = candles.get(k);
                if (iCandle.High > max6) {
                    max6 = iCandle.High;
                }

                if (iCandle.Low < min6) {
                    min6 = iCandle.Low;
                }
            }

        }

        currCandle.max6 = max6;
        currCandle.min6 = min6;

    }


}
