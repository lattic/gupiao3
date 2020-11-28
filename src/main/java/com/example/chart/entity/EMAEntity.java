package com.example.chart.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rex on 2018/11/14.
 */
public class EMAEntity implements ChartEntity {

    public List<EntryType> emaDataList;

    public EMAEntity(List<EntryType> emaDataList) {
        this.emaDataList = emaDataList;
    }

    public EMAEntity() {
        this.emaDataList = new ArrayList<>();
//        this.ema30 = new ArrayList<>();
    }

    @Override
    public void clearValues() {
        if (emaDataList != null) {
            emaDataList.clear();
        }
//        if (ema30 != null) {
//            ema30.clear();
//        }
    }


    public static void CandleParseToEMA(List<ICandle> iCandles, int i, int period) {
        ICandle tempModel = iCandles.get(i);
        if (i == 0) {
            tempModel.common = tempModel.Close;

        } else if (tempModel.common == 0) {
            ICandle lastModel = iCandles.get(i - 1);
            if (lastModel.common != 0) {
                //待修改
                tempModel.common = (float) ((2.0 / (period + 1)) * (tempModel.Close - lastModel.common) + lastModel.common);
            } else {
                CandleParseToEMA(iCandles, i - 1, period);
                tempModel.common = (float) ((2.0 / (period + 1)) * (tempModel.Close - lastModel.common) + lastModel.common);

            }

        }

//        if (i == 0) {
////            // 第一日的ema7为收盘价
////            tempModel.common = tempModel.Close;
////
////        } else if (tempModel.ema7 == 0 || tempModel.ema30 == 0) {
////
////            ICandle lastModel = iCandles.get(i - 1);
////
////            if (lastModel.ema7 != 0 && lastModel.ema30 != 0) {
////
////                tempModel.ema7 = (float) ((2.0 / 13.0) * (tempModel.Close - lastModel.ema7) + lastModel.ema7);
////
////                tempModel.ema30 = (float) ((2.0 / 27.0) * (tempModel.Close - lastModel.ema30) + lastModel.ema30);
////
////            } else {
////                CandleParseToEMA(iCandles, j, i - 1);
////                tempModel.ema7 = (float) ((2.0 / 13.0) * (tempModel.Close - lastModel.ema7) + lastModel.ema7);
////
////                tempModel.ema30 = (float) ((2.0 / 27.0) * (tempModel.Close - lastModel.ema30) + lastModel.ema30);
////            }
////
////        }
    }
}
