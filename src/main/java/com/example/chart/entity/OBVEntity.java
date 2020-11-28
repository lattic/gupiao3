package com.example.chart.entity;


import java.util.ArrayList;
import java.util.List;

import com.example.chart.base.entity.Entry;

/**
 * Created by Rex on 2018/11/13.
 */
public class OBVEntity implements ChartEntity {

    public List<Entry> obvList;
    //均线
    public List<Entry> obv30List;
    public String indexDes = "";
    public int Tn = 0;

    public OBVEntity() {
        this.obvList = new ArrayList<>();
        this.obv30List = new ArrayList<>();
    }

    @Override
    public void clearValues() {
        if (obvList != null) {
            obvList.clear();
        }
        if (obv30List != null) {
            obv30List.clear();
        }
    }

    public OBVEntity(List<Entry> obvList, List<Entry> obv30List) {
        this.obvList = obvList;
        this.obv30List = obv30List;
    }

    public static void CandleParseObV(List<ICandle> datas, int index, int TnOBV) {

        if (index > (datas.size() - 1)) {
            index = datas.size() - 1;
        }
        if (datas.size() >= 1) {

            for (int a = index; a < datas.size(); a++) {
                p_obvValueAtIndex(datas, a);

                ICandle tempModel = datas.get(a);

                if (a >= (TnOBV - 1)) {
                    tempModel.obv30ma = (float) p_averageOBVWithCount(datas, TnOBV, a);
                }

            }

        }
    }

    private static void p_obvValueAtIndex(List<ICandle> datas, int index) {
        ICandle tempModel = datas.get(index);
        if (index == 1) {
            int sgn = 1;
            ICandle lastModel = datas.get(index - 1);
            if (tempModel.Close >= lastModel.Close) {
                sgn = 1;
            } else {
                sgn = -1;
            }
            tempModel.obv = (sgn) * tempModel.Volume;

        } else if (tempModel.obv == 0 && index > 1) {
            ICandle lastModel = datas.get(index - 1);

            if (lastModel.obv != 0) {
                int sgn = 1;
                if (tempModel.Close >= lastModel.Close) {
                    sgn = 1;
                } else {
                    sgn = -1;
                }
                tempModel.obv = lastModel.obv + sgn * tempModel.Volume;
            } else {
                p_obvValueAtIndex(datas, index - 1);
                int sgn = 1;
                if (tempModel.Close >= lastModel.Close) {
                    sgn = 1;
                } else {
                    sgn = -1;
                }
                tempModel.obv = lastModel.obv + sgn * tempModel.Volume;
            }

        }

    }

    private static double p_averageOBVWithCount(List<ICandle> datas, int count, int index) {
        double result = 0.0f;
        if (index < (count - 1)) {
            return result;
        }
        double sum = 0.0f;
        for (int a = index; a > (index - count); a--) {

            ICandle tempModel = datas.get(a);
            sum += tempModel.obv;

        }
        result = sum / (double) count;

        return result;

    }


}
