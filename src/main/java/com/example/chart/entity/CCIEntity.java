package com.example.chart.entity;


import java.util.ArrayList;
import java.util.List;

import com.example.chart.base.entity.Entry;

/**
 * Created by Rex on 2018/11/13.
 */
public class CCIEntity implements ChartEntity {
    public List<Entry> cci;

    public List<Entry> tp;

    public List<Entry> mtp;

    public List<Entry> md;
    public String indexDes="";

    public CCIEntity(List<Entry> cci, List<Entry> tp, List<Entry> mtp, List<Entry> md) {
        this.cci = cci;
        this.tp = tp;
        this.mtp = mtp;
        this.md = md;
    }

    public CCIEntity() {
        this.cci = new ArrayList<>();
        this.mtp = new ArrayList<>();
        this.tp = new ArrayList<>();
        this.md = new ArrayList<>();
    }

    @Override
    public void clearValues() {
        if (cci != null) {
            cci.clear();
        }
        if (mtp != null) {
            mtp.clear();
        }
        if (tp != null) {
            tp.clear();
        }
        if (md != null) {
            md.clear();
        }
    }


    public static void CandleParseCCI(List<ICandle> datas, int index, int Tn) {

                ICandle tempModel = datas.get(index);
                tempModel.tp = (tempModel.High + tempModel.Low + tempModel.Close) / 3.0f;

                if (index >= Tn - 1) {
                    tempModel.mtp = (float) p_averageCCI_MTPWithCount(datas, Tn, index);
                    tempModel.md = (float) p_averageCCI_MDWithCount(datas, Tn, index);
                    tempModel.cci = (tempModel.tp - tempModel.mtp) / (tempModel.md) / 0.015f;
                }
    }

    /**
     * 计算CCI_MTP的平均值
     *
     * @param datas
     * @param count 平均数的个数
     * @param index 计算平均数结束的下标
     */
    private static double p_averageCCI_MTPWithCount(List<ICandle> datas, int count, int index) {
        double result = 0.0f;
        if (index < (count - 1)) {
            return result;
        }
        double sum = 0.0f;
        for (int a = index; a > (index - count); a--) {
            ICandle tempModel = datas.get(a);
            sum += tempModel.tp;

        }
        result = sum / (double) count;
        return result;
    }

    /**
     * 计算CCI_MD的平均值
     *
     * @param datas
     * @param count 平均数的个数
     * @param index 计算平均数结束的下标
     */
    private static double p_averageCCI_MDWithCount(List<ICandle> datas, int count, int index) {
        double result = 0.0f;

        if (index < (count - 1)) {
            return result;
        }
        double sum = 0.0f;
        for (int a = index; a > (index - count); a--) {
            ICandle tempModel = datas.get(a);
            sum += Math.abs(tempModel.tp - tempModel.mtp);

        }
        result = sum / (double) count;
        return result;
    }


}


