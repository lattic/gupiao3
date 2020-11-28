package com.example.chart.base.entity;

/**
 */
public class Candle {
    public long Timestamp;
    public float High;
    public float Low;
    public float Open;
    public float Close;
    public float Volume;
    //分时图属性
    public float average;

    public float dea;
    public float dif;
    public float macd;

    public float k;
    public float d;
    public float j;
    public float rsv9;




    //是否是填充数据
    public boolean isFill;
    public boolean isIncread;

    public Candle() {
    }

    public Candle(long timestamp, float high, float low, float open, float close, float volume) {
        Timestamp = timestamp;
        High = high;
        Low = low;
        Open = open;
        Close = close;
        Volume = volume;
    }

    public Candle(long timestamp, float high, float low, float open, float close, float volume,
                  boolean isFill) {
        Timestamp = timestamp;
        High = high;
        Low = low;
        Open = open;
        Close = close;
        Volume = volume;
        this.isFill = isFill;
    }

    public Candle(long timestamp, float high, float low, float open, float close, float volume, float average, boolean isFill) {
        Timestamp = timestamp;
        High = high;
        Low = low;
        Open = open;
        Close = close;
        Volume = volume;
        this.average = average;
        this.isFill = isFill;
    }

    @Override
    public String toString() {
        return "Timestamp:" + this.Timestamp + " High:" + this.High + " Low: " + this.Low + " Open: " + this.Open + " Close: " + this.Close + " Volume:" + this.Volume;
    }
}
