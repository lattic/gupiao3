package com.example.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeries;
import org.ta4j.core.indicators.SMAIndicator;
import org.ta4j.core.indicators.bollinger.BollingerBandsLowerIndicator;
import org.ta4j.core.indicators.bollinger.BollingerBandsMiddleIndicator;
import org.ta4j.core.indicators.bollinger.BollingerBandsUpperIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.indicators.statistics.StandardDeviationIndicator;

import com.example.chart.base.entity.Candle;
import com.example.chart.base.entity.Entry;
import com.example.chart.entity.BollEntity;
import com.example.chart.entity.MAEntity;
import com.example.mapper.HistoryDayStockMapper;
import com.example.mapper.HistoryStockMapper;
import com.example.mapper.RobotAccountMapper;
import com.example.mapper.RobotSetMapper;
import com.example.model.HistoryDayStockDo;
import com.example.model.HistoryPriceDo;
import com.example.model.MockLog;
import com.example.model.RealTimeDo;
import com.example.model.RobotAccountDo;
import com.example.model.RobotSetDo;
import com.example.model.StockPriceVo;
import com.example.model.TradingRecordDo;
import com.example.uitls.DateUtils;
import com.example.uitls.RedisKeyUtil;
import com.example.uitls.RedisUtil;

@Service
public class TrendStrategyServiceImpl implements TrendStrategyService {

	private static Logger logger = LoggerFactory.getLogger(TrendStrategyServiceImpl.class);

	@Resource
	private RedisUtil redisUtil;

	@Autowired
	private RobotAccountMapper robotAccountMapper;

	@Autowired
	private RobotSetMapper robotSetMapper;

	@Autowired
	private HistoryDayStockMapper historyDayStockMapper;

	@Autowired
	private HistoryStockMapper historyStockMapper;

	@Override
	public BarSeries transformBarSeriesByStockPrice(List<StockPriceVo> list) {
		if(list == null || list.isEmpty()) {
			return null;
		}
		BarSeries series = new BaseBarSeries(list.get(0).getNumber());
		for(StockPriceVo stock:list) {
			    Timestamp timestamp=new Timestamp(DateUtils.getDateForYYYYMMDDHHMM_NUMBER(stock.getHistoryAll()).getTime());
	            ZonedDateTime date = ZonedDateTime.ofInstant(timestamp.toInstant(), ZoneOffset.UTC);
	            double open = stock.getOpen().doubleValue();
	            double high = stock.getHigh().doubleValue();
	            double low = stock.getLow().doubleValue();
	            double close = stock.getClose().doubleValue();
	            double volume = stock.getVolume();
	            series.addBar(date, open, high, low, close, volume);
	        }
		return series;
	}
	
	@Override
	public List<Candle> transformStockPrice(List<StockPriceVo> list) {
		List<Candle> rsList = new ArrayList<Candle>();
		for (StockPriceVo stock : list) {
			Candle c= new Candle(
					DateUtils.getDateForYYYYMMDDHHMM_NUMBER(stock.getHistoryAll()).getTime(),
					stock.getHigh().floatValue(),
					stock.getLow().floatValue(),
					stock.getOpen().floatValue(),
					stock.getClose().floatValue(),
					stock.getVolume()
					);
			rsList.add(c);
		}
		return rsList;
	}

	@Override
	public List<StockPriceVo> transformByDayLine(List<HistoryDayStockDo> list) {
		List<StockPriceVo> rsList = new ArrayList<StockPriceVo>();
		for (HistoryDayStockDo dayStock : list) {
			StockPriceVo stock = new StockPriceVo();
			stock.setClose(dayStock.getClose());
			stock.setOpen(dayStock.getOpen());
			stock.setLow(dayStock.getLow());
			stock.setHigh(dayStock.getHigh());
			stock.setVolume(dayStock.getVolume());
			stock.setNumber(dayStock.getNumber());
			stock.setHistoryAll(dayStock.getHistoryDay() + "1500");
			stock.setHistoryDay(dayStock.getHistoryDay());
			String name = (String) redisUtil.get(RedisKeyUtil.getStockName(dayStock.getNumber()));
			stock.setName(name);
			rsList.add(stock);
		}
		return rsList;
	}

	@Override
	public List<StockPriceVo> transformByMinuteLine(List<HistoryPriceDo> list) {
		List<StockPriceVo> rsList = new ArrayList<StockPriceVo>();
		for (HistoryPriceDo minuteStock : list) {
			StockPriceVo stock = new StockPriceVo();
			stock.setClose(minuteStock.getShoupanjia());
			stock.setOpen(minuteStock.getKaipanjia());
			stock.setLow(minuteStock.getZuidijia());
			stock.setHigh(minuteStock.getZuigaojia());
			stock.setNumber(minuteStock.getNumber());
			stock.setMa20hour(minuteStock.getMa20());
			stock.setHistoryAll(DateUtils.getDateForYYYYMMDDHHMMByDate(minuteStock.getDateime()));
			stock.setHistoryDay(DateUtils.getDateForYYYYMMDDByDate(minuteStock.getDateime()));
			String name = (String) redisUtil.get(RedisKeyUtil.getStockName(minuteStock.getNumber()));
			stock.setName(name);
			rsList.add(stock);
		}
		return rsList;
	}

	@Override
	public List<StockPriceVo> transformByRealTime(List<RealTimeDo> list) {
		List<StockPriceVo> rsList = new ArrayList<StockPriceVo>();
		for (RealTimeDo rtStock : list) {
			StockPriceVo stock = new StockPriceVo();
			stock.setClose(new BigDecimal(rtStock.getKaipanjia()));
			stock.setOpen(new BigDecimal(rtStock.getDangqianjiage()));
			stock.setLow(new BigDecimal(rtStock.getLow()));
			stock.setHigh(new BigDecimal(rtStock.getTop()));
			stock.setNumber(rtStock.getNumber());
			stock.setHistoryAll(rtStock.getDate() + rtStock.getTime());
			stock.setHistoryDay(rtStock.getDate());
			String name = (String) redisUtil.get(RedisKeyUtil.getStockName(rtStock.getNumber()));
			stock.setName(name);
			rsList.add(stock);
		}
		return rsList;
	}

	@Override
	public MockLog transformByTradingRecord(List<TradingRecordDo> list) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TradingRecordDo> getStrategyByBand(List<StockPriceVo> list, RobotAccountDo account, RobotSetDo config) {
		List<TradingRecordDo> rsList = new ArrayList<TradingRecordDo>();

		for (StockPriceVo stock : list) {

		}
		return null;
	}

	@Override
	public List<TradingRecordDo> getStrategyByBox(List<StockPriceVo> list, RobotAccountDo account, RobotSetDo config) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TradingRecordDo> getStrategyByRebound(List<StockPriceVo> list, RobotAccountDo account,
			RobotSetDo config) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TradingRecordDo> getStrategyByMa(List<StockPriceVo> list, RobotAccountDo account, RobotSetDo config) {
		List<TradingRecordDo> rslist=new ArrayList<TradingRecordDo>();
		MAEntity ma = buildMaEntry(list);
	    
	    for(int i=0;i<list.size();i++) {
	    	StockPriceVo price=list.get(i);
	    	Entry maValue=ma.ma.get(i);
	    	double buyPoint=maValue.getY()*0.97;
	    	double sellPoint=maValue.getY()*1.05;
	    	double stopLossPoint=maValue.getY()*0.95;
	    	
	    	if(price.getOpen().doubleValue() <= buyPoint && price.getOpen().doubleValue()>=stopLossPoint) {
	    		double sotck=account.getTotal().intValue() * 0.2/ (price.getOpen().intValue()*100);
	    		int num=(int)sotck*100;
	    		TradingRecordDo buyRecord=new TradingRecordDo(
	    				DateUtils.getDateForYYYYMMDDHHMM_NUMBER(price.getHistoryAll()),
	    				price.getNumber(),
	    				price.getName(),
	    				config.getDtId(),
	    				price.getOpen(),
	    				num,
	    				TradingRecordDo.options_buy,
	    				"低于MA均线买入"
	    				);
	    		rslist.add(buyRecord);
	    	}
	    	if(price.getOpen().doubleValue() <= stopLossPoint) {
	    		double sotck=account.getTotal().intValue() * 0.2/ (price.getOpen().intValue()*100);
	    		int num=(int)sotck*100;
	    		TradingRecordDo buyRecord=new TradingRecordDo(
	    				DateUtils.getDateForYYYYMMDDHHMM_NUMBER(price.getHistoryAll()),
	    				price.getNumber(),
	    				price.getName(),
	    				config.getDtId(),
	    				price.getOpen(),
	    				num,
	    				TradingRecordDo.options_sell,
	    				"跌破止损卖出"
	    				);
	    		rslist.add(buyRecord);
	    	}
	    	if(price.getOpen().doubleValue() >= sellPoint) {
	    		double sotck=account.getTotal().intValue() * 0.2/ (price.getOpen().intValue()*100);
	    		int num=(int)sotck*100;
	    		TradingRecordDo buyRecord=new TradingRecordDo(
	    				DateUtils.getDateForYYYYMMDDHHMM_NUMBER(price.getHistoryAll()),
	    				price.getNumber(),
	    				price.getName(),
	    				config.getDtId(),
	    				price.getOpen(),
	    				num,
	    				TradingRecordDo.options_sell,
	    				"止盈卖出"
	    				);
	    		rslist.add(buyRecord);
	    	}
	    }
		return rslist;
	}

	private MAEntity buildMaEntry(List<StockPriceVo> list) {
		BarSeries series = transformBarSeriesByStockPrice(list);
		ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
	    SMAIndicator avg = new SMAIndicator(closePrice, 20);
	    List<Entry> maList =new  ArrayList<Entry>();
	    for(int i=0;i<list.size();i++) {
	    	Entry entry=new Entry();
	    	entry.setX(list.get(i).getHistoryAll());
	    	entry.setY(avg.getValue(i).doubleValue());
	    	entry.setData(list.get(i));
	    	maList.add(entry);
	    }
	    return new MAEntity(maList);
	}

	@Override
	public List<TradingRecordDo> getStrateByBoll(List<StockPriceVo> list, RobotAccountDo account, RobotSetDo config) {
		List<TradingRecordDo> rslist=new ArrayList<TradingRecordDo>();
		BollEntity boll= buildBollEntry(list);
		for(int i=0;i<list.size();i++) {
	    	StockPriceVo price=list.get(i);
	    	Entry upValue=boll.getUpList().get(i);
	    	Entry midValue=boll.getMidList().get(i);
	    	Entry lowerValue=boll.getLowerList().get(i);
	    	if(price.getOpen().doubleValue() <midValue.getY()) {
	    		continue;
	    	}
	    	
	    	double buyPoint=lowerValue.getY()*0.97;
	    	double stopLossPoint=lowerValue.getY()*0.95;
	    	double sellPoint=upValue.getY()*0.995;
	    	
	    	if(price.getOpen().doubleValue() <= buyPoint && price.getOpen().doubleValue()>=stopLossPoint) {
	    		double sotck=account.getTotal().intValue() * 0.2/ (price.getOpen().intValue()*100);
	    		int num=(int)sotck*100;
	    		TradingRecordDo buyRecord=new TradingRecordDo(
	    				DateUtils.getDateForYYYYMMDDHHMM_NUMBER(price.getHistoryAll()),
	    				price.getNumber(),
	    				price.getName(),
	    				config.getDtId(),
	    				price.getOpen(),
	    				num,
	    				TradingRecordDo.options_buy,
	    				"低于boll下轨买入"
	    				);
	    		rslist.add(buyRecord);
	    	}
	    	if(price.getOpen().doubleValue() <= stopLossPoint) {
	    		double sotck=account.getTotal().intValue() * 0.2/ (price.getOpen().intValue()*100);
	    		int num=(int)sotck*100;
	    		TradingRecordDo buyRecord=new TradingRecordDo(
	    				DateUtils.getDateForYYYYMMDDHHMM_NUMBER(price.getHistoryAll()),
	    				price.getNumber(),
	    				price.getName(),
	    				config.getDtId(),
	    				price.getOpen(),
	    				num,
	    				TradingRecordDo.options_sell,
	    				"跌破止损卖出"
	    				);
	    		rslist.add(buyRecord);
	    	}
	    	if(price.getOpen().doubleValue() >= sellPoint) {
	    		double sotck=account.getTotal().intValue() * 0.2/ (price.getOpen().intValue()*100);
	    		int num=(int)sotck*100;
	    		TradingRecordDo buyRecord=new TradingRecordDo(
	    				DateUtils.getDateForYYYYMMDDHHMM_NUMBER(price.getHistoryAll()),
	    				price.getNumber(),
	    				price.getName(),
	    				config.getDtId(),
	    				price.getOpen(),
	    				num,
	    				TradingRecordDo.options_sell,
	    				"止盈卖出"
	    				);
	    		rslist.add(buyRecord);
	    	}
	    }
		return rslist;
	}

	private BollEntity buildBollEntry(List<StockPriceVo> list) {
		BollEntity boll;
		BarSeries series = transformBarSeriesByStockPrice(list);
        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
        SMAIndicator avg = new SMAIndicator(closePrice, 20);
        StandardDeviationIndicator sd20 = new StandardDeviationIndicator(closePrice, 20);

        // Bollinger bands
        BollingerBandsMiddleIndicator middleBBand = new BollingerBandsMiddleIndicator(avg);
        BollingerBandsLowerIndicator lowBBand = new BollingerBandsLowerIndicator(middleBBand, sd20);
        BollingerBandsUpperIndicator upBBand = new BollingerBandsUpperIndicator(middleBBand, sd20);
        //存储上轨数据
        List<Entry> upList=new ArrayList<Entry>();
        //存储中轨数据
        List<Entry> midList=new ArrayList<Entry>();
        //存储下轨数据
        List<Entry> lowerList=new ArrayList<Entry>();
        
        for(int i=0;i<list.size();i++) {
        	Entry up=new Entry();
        	up.setX(list.get(i).getHistoryAll());
        	up.setY(upBBand.getValue(i).doubleValue());
        	up.setData(list.get(i));
        	upList.add(up);
        	
        	Entry mid=new Entry();
        	mid.setX(list.get(i).getHistoryAll());
        	mid.setY(middleBBand.getValue(i).doubleValue());
        	mid.setData(list.get(i));
        	midList.add(mid);
        	
        	Entry lower=new Entry();
        	lower.setX(list.get(i).getHistoryAll());
        	lower.setY(lowBBand.getValue(i).doubleValue());
        	lower.setData(list.get(i));
        	lowerList.add(lower);
        }
        return new BollEntity(upList, midList, lowerList);
	}

	

}
