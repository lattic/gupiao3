package com.example.chart.entity;


import java.util.ArrayList;
import java.util.List;

import com.example.chart.base.entity.Entry;

/**
 * Created by Rex on 2018/11/13.
 */
public class DMIEntity implements ChartEntity {


    public List<Entry> pdiList;
    public List<Entry> mdiList;
    //adx
    public List<Entry> adxList;
    //adxr
    public List<Entry> adxrList;
    public String indexDes = "";

    public DMIEntity() {
        this.pdiList = new ArrayList<>();
        this.mdiList = new ArrayList<>();
        this.adxList = new ArrayList<>();
        this.adxrList = new ArrayList<>();
    }

    @Override
    public void clearValues() {
        if (pdiList != null) {
            pdiList.clear();
        }
        if (mdiList != null) {
            mdiList.clear();
        }
        if (adxList != null) {
            adxList.clear();
        }

        if (adxrList != null) {
            adxrList.clear();
        }
    }

    public DMIEntity(List<Entry> pdiList, List<Entry> mdiList, List<Entry> adxList, List<Entry> adxrList) {
        this.pdiList = pdiList;
        this.mdiList = mdiList;
        this.adxList = adxList;
        this.adxrList = adxrList;
    }

    public List<Entry> getDataByName(String name) {
        if (name.equals("PDI")) {
            return pdiList;
        } else if (name.equals("MDI")) {
            return mdiList;
        } else if (name.equals("ADX")) {
            return adxList;
        } else if (name.equals("ADXR")) {
            return adxrList;
        }
        return pdiList;

    }


    public static void ParseByP1(List<ICandle> dmiDatas, int atIndex, int p1) {


        ICandle currentModel = dmiDatas.get(atIndex);

        if (atIndex - 1 >= 0) {

            ICandle lastModel = dmiDatas.get(atIndex - 1);


            currentModel.tr = Math.max(
                    Math.max(Math.abs(currentModel.High - currentModel.Low), Math.abs(currentModel.High - lastModel.Close)), Math.abs(currentModel.Low - lastModel.Close)
            ); // 取三者的最大值;


            double dmplus = (currentModel.High - lastModel.High);

            double dmminus = (lastModel.Low - currentModel.Low);

            currentModel.dm_plus = (dmplus > 0 && dmplus > dmminus) ? (float) dmplus : 0;

            currentModel.dm_minus = (dmminus > 0 && dmminus > dmplus) ? (float) dmminus : 0;

        }


        if (atIndex == 14 - 1) { //di_plus在13才有值

            double tr14 = 0;
            double dm_plus14 = 0;
            double dm_minus14 = 0;

            for (int i = atIndex; i > atIndex - 14; i--) {

                ICandle tempModel = dmiDatas.get(i);

                tr14 += tempModel.tr;
                dm_plus14 += tempModel.dm_plus;
                dm_minus14 += tempModel.dm_minus;

            }


            currentModel.tr14 = (float) (tr14 / 14.f);
            currentModel.dm_plus14 = (float) (dm_plus14 / 14.f);
            currentModel.dm_minus14 = (float) (dm_minus14 / 14.f);

            currentModel.di_plus14 = (float) (dm_plus14 * 100.f / tr14);
            currentModel.di_minus14 = (float) (dm_minus14 * 100.f / tr14);

        } else if (atIndex > 14 - 1) {
            if (currentModel.dm_plus14 == 0 && currentModel.dm_minus14 == 0 && currentModel.tr14 == 0) {
                ICandle lastModel = dmiDatas.get(atIndex - 1);
                if (lastModel.dm_plus14 == 0 && lastModel.dm_minus14 == 0 && lastModel.tr14 == 0) {

                    ParseByP1(dmiDatas, atIndex - 1, p1);
                    currentModel.dm_plus14 = currentModel.dm_plus / 14.f + (13.f / 14.f) * lastModel.dm_plus14;
                    currentModel.dm_minus14 = currentModel.dm_minus / 14 + (13.f / 14.f) * lastModel.dm_minus14;
                    currentModel.tr14 = currentModel.tr / 14.f + (13.f / 14.f) * lastModel.tr14;


                    currentModel.di_plus14 = currentModel.dm_plus14 * 100.f / currentModel.tr14;
                    currentModel.di_minus14 = currentModel.dm_minus14 * 100.f / currentModel.tr14;

                } else {


                    currentModel.dm_plus14 = currentModel.dm_plus / 14.f + (13.f / 14.f) * lastModel.dm_plus14;
                    currentModel.dm_minus14 = currentModel.dm_minus / 14 + (13.f / 14.f) * lastModel.dm_minus14;
                    currentModel.tr14 = currentModel.tr / 14.f + (13.f / 14.f) * lastModel.tr14;

                    currentModel.di_plus14 = currentModel.dm_plus14 * 100.f / currentModel.tr14;
                    currentModel.di_minus14 = currentModel.dm_minus14 * 100.f / currentModel.tr14;

                }

            }

        }
    }


    public static void ParseByP2(List<ICandle> dmiDatas, int atIndex, int p1, int p2) {
        ICandle tempModel = dmiDatas.get(atIndex);

        if (atIndex == 14 + 6 - 2) {

            double adx = 0;
            for (int i = atIndex; i > atIndex - 6; i--) {

                ICandle tempModel2 = dmiDatas.get(i);
                if ((tempModel2.di_minus14 + tempModel2.di_plus14) != 0) {
                    adx += Math.abs((tempModel2.di_minus14 - tempModel2.di_plus14) / (tempModel2.di_minus14 + tempModel2.di_plus14)) * 100.f;
                }
            }
            tempModel.adx = (float) (adx / 14.f);

        } else if (tempModel.adx == 0 && atIndex > 14 + 6 - 2) {

            ICandle lastModel = dmiDatas.get(atIndex - 1);
            if (lastModel.adx != 0) {
                tempModel.adx = (Math.abs((tempModel.di_minus14 - tempModel.di_plus14) / (tempModel.di_minus14 + tempModel.di_plus14)) * 100.f) / 6.f + (5.f / 6.f) * lastModel.adx;
            } else {
                ParseByP2(dmiDatas, atIndex - 1, p1, p2);
                tempModel.adx = (Math.abs((tempModel.di_minus14 - tempModel.di_plus14) / (tempModel.di_minus14 + tempModel.di_plus14)) * 100.f) / 6.f + (5.f / 6.f) * lastModel.adx;
            }

        }


        //计算adxr

        if (atIndex == 14 + 6 + 5 - 3) {
            double adxr = 0;
            for (int i = atIndex; i > atIndex - 6; i--) {
                ICandle tempModel2 = dmiDatas.get(i);
                adxr += tempModel2.adx;

            }


            tempModel.adxr = (float) (adxr / 5.f);

        } else if (atIndex > 14 + 6 + 5 - 3) {

            ICandle lastModel = dmiDatas.get(atIndex - 1);

            if (lastModel.adxr != 0) {
                tempModel.adxr = tempModel.adx / 6.f + (5.f / 6.f) * lastModel.adxr;
            } else {
                ParseByP2(dmiDatas, atIndex - 1, p1, p2);
                tempModel.adxr = tempModel.adx / 6.f + (5.f / 6.f) * lastModel.adxr;
            }

        }
    }
}
