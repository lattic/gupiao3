package com.example.chart.entity;

import com.example.chart.base.entity.Candle;

/**
 * Created by Rex on 2018/11/14.
 */
public class ICandle extends Candle {
    //obv
    public float obv30ma;
    public float obv;

    //cci
    public float tp;
    public float mtp;
    public float md;
    public float cci;

    //wr
    public float max6;
    public float min6;




    //dmi
    public float tr;
    public float dm_plus;
    public float dm_minus;
    public float tr14;
    public float dm_plus14;
    public float dm_minus14;
    public float di_plus14;
    public float di_minus14;
    public float adxr;
    public float adx;

    public float sar;
    public float ep;
    public float af;
    public boolean isUp;


    //common

    public float common;//用于临时计算


    public ICandle(Candle candle, boolean isCopy) {
        if (isCopy) {
            Timestamp = candle.Timestamp;
            High = candle.High;
            Low = candle.Low;
            Open = candle.Open;
            Close = candle.Close;
            Volume = candle.Volume;
        }
    }
}
