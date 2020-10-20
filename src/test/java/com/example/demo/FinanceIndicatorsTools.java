package com.example.demo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FinanceIndicatorsTools {
	 private static  BigDecimal two = new BigDecimal(2);
	 
	    /**
	     * Calculate EMA,
	     *
	     * @param list
	     *            :Price list to calculate，the first at head, the last at tail.
	     * @return
	     */
	    public static final BigDecimal getEXPMAV1(final List<BigDecimal> list, final int number) {
	        // 开始计算EMA值，
	        BigDecimal k = two.divide(new BigDecimal(number +1),2,RoundingMode.HALF_UP); //计算出序数
	        BigDecimal ema = list.get(0);// 第一天ema等于当天收盘价
	        for (int i = 1; i < list.size(); i++) {
	            // 第二天以后，当天收盘 收盘价乘以系数再加上昨天EMA乘以系数-1
	            ema = list.get(i).multiply(k).add(ema.multiply(BigDecimal.ONE.subtract(k)) ) ;
	 
	        }
	        return ema;
	    }
	 
	 
	    /**
	     * DEA：离差平均值。今日DEA = （前一日DEA) * ((N-1)/(N+1)) + (今日DIF) * 2/(N+1)）
	     */
	 
	    public static final BigDecimal getMyDea(final List<BigDecimal> diffList, final int N) {
	 
	 
	        BigDecimal fix02 = two.divide(new BigDecimal(N+1),2,RoundingMode.HALF_UP);
	         if(diffList.size() == 1){
	             return  fix02.multiply(diffList.get(0));
	        }else {
	 
	            BigDecimal fix08 = new BigDecimal(N-1).divide(new BigDecimal(N+1),2,RoundingMode.HALF_UP);
	 
	            BigDecimal right =  fix02.multiply(diffList.get(diffList.size() -1)) ;
	 
	            List<BigDecimal> sublist =  diffList.subList(0,diffList.size()-1);
	            BigDecimal left  = fix08.multiply(getMyDea(sublist,N));
	 
	            return left.add(right);
	        }
	 
	    }
	 
	 
	    /**
	     * 参考文章：
	     *      https://blog.csdn.net/u014427812/article/details/37508179（原理）
	     *      https://www.cnblogs.com/xhqgogogo/p/3386426.html（实现篇，有点糙，根据上面的原理优化了）
	     *
	     * @param list
	     * @param number
	     * @return
	     */
	    public static final BigDecimal getMyEXPMA(final List<BigDecimal> list, final int number) {
	         if(list.size() == 1){
	            return  list.get(0);
	        }else {
	            List<BigDecimal> sublist =  list.subList(0,list.size()-1);
	 
	            BigDecimal left =   two.multiply(list.get(list.size()-1)).divide(new BigDecimal(number +1),2,RoundingMode.HALF_UP);
	 
	            BigDecimal last = getMyEXPMA(  sublist,     number);//递归调用
	            BigDecimal right = new BigDecimal(number -1).multiply(last).divide(new BigDecimal(number +1),2,RoundingMode.HALF_UP);
	 
	            return left .add(right);
	        }
	 
	    }
	 
	    /**
	     * calculate MACD values
	     *
	     * @param list
	     *            :Price list to calculate，the first at head, the last at tail.
	     * @param shortPeriod  12
	     *            :the short period value.
	     * @param longPeriod 26
	     *            :the long period value.
	     * @param midPeriod  9
	     *            :the mid period value.
	     * @return
	     */
	    public static final HashMap<String, BigDecimal> getMACDV1(final List<BigDecimal> list, final int shortPeriod, final int longPeriod, int midPeriod) {
	        HashMap<String, BigDecimal> macdData = new HashMap<>();
	        List<BigDecimal> diffList = new ArrayList<BigDecimal>();
	        BigDecimal shortEMA = BigDecimal.ZERO;
	        BigDecimal longEMA = BigDecimal.ZERO;
	        BigDecimal dif = BigDecimal.ZERO;
	        BigDecimal dea = BigDecimal.ZERO;
	 
	        for (int i = list.size() - 1; i >= 0; i--) {
	            List<BigDecimal> sublist = list.subList(0, list.size() - i);
	            shortEMA = getMyEXPMA(sublist, shortPeriod);
	            longEMA =  getMyEXPMA(sublist, longPeriod);
	            dif = shortEMA.subtract(longEMA);
	            diffList.add(dif);
	        }
	        dea = getMyDea(diffList, midPeriod);
	 
	        dea = dea.setScale(2,BigDecimal.ROUND_HALF_DOWN);
	        dif = dif.setScale(2,BigDecimal.ROUND_HALF_DOWN);
	 
	        BigDecimal MACD = two.multiply (dif.subtract(dea));
	        MACD = MACD.setScale(2,BigDecimal.ROUND_HALF_DOWN);
	 
	        macdData.put("DIF", dif);
	        macdData.put("DEA", dea);
	        macdData.put("MACD",MACD);
	        return macdData;
	    }

}
