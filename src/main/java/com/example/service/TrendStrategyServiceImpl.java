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
import com.example.chart.entity.EMAEntity;
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
	
	@Override
	public List<TradingRecordDo> getStrategyByEMa(List<StockPriceVo> list, RobotAccountDo account, RobotSetDo config) {
		List<TradingRecordDo> rslist=new ArrayList<TradingRecordDo>();
		EMAEntity ema = buildEmaEntry(list);
	    
		boolean isbuy=false;
	    for(int i=0;i<list.size();i++) {
	    	StockPriceVo price=list.get(i);
	    	Entry maValue1=ema.getEmaList1().get(i);
	    	Entry maValue2=ema.getEmaList2().get(i);
	    	double buyPoint=maValue1.getY();
	    	double sellPoint=buyPoint*1.1;
	    	double stopLossPoint=maValue2.getY();
	    	
	    	if(price.getClose().doubleValue() <= stopLossPoint) {
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
	    		isbuy=true;
	    		rslist.add(buyRecord);
	    	}
	    	
	    	//股价收盘在MA1,MA2之下或 MA1不在MA2之上都是跳过
	    	if(maValue1.getY() < maValue2.getY() || price.getClose().doubleValue()<maValue1.getY() || price.getClose().doubleValue()<maValue2.getY()) {
	    		isbuy=false;
	    		continue;
	    	}
	    	
	    	//趋势判断 10天必须比现在低
	    	if(i>10 && ema.getEmaList1().get(i-10).getY()>ema.getEmaList1().get(i).getY()) {
	    		isbuy=true;
	    	}
	    	
	    	//开盘价在MA1之下，收盘价在MA1之上
	    	if(isbuy && price.getOpen().doubleValue() <= buyPoint && price.getClose().doubleValue() >= buyPoint) {
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
	    				"突破EMA 87 均线买入"
	    				);
	    		rslist.add(buyRecord);
	    	}
	    	if(price.getClose().doubleValue() >= sellPoint) {
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
	    		isbuy=true;
	    		rslist.add(buyRecord);
	    	}
	    }
		return rslist;
	}

	private EMAEntity buildEmaEntry(List<StockPriceVo> list) {
		BarSeries series = transformBarSeriesByStockPrice(list);
		ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
	    SMAIndicator avg1 = new SMAIndicator(closePrice, 87);
	    SMAIndicator avg2 = new SMAIndicator(closePrice, 144);
	    List<Entry> maList1 =new  ArrayList<Entry>();
	    List<Entry> maList2 =new  ArrayList<Entry>();
	    for(int i=0;i<list.size();i++) {
	    	Entry entry1=new Entry();
	    	entry1.setX(list.get(i).getHistoryAll());
	    	entry1.setY(avg1.getValue(i).doubleValue());
	    	entry1.setData(list.get(i));
	    	maList1.add(entry1);
	    	
	    	Entry entry2=new Entry();
	    	entry2.setX(list.get(i).getHistoryAll());
	    	entry2.setY(avg2.getValue(i).doubleValue());
	    	entry2.setData(list.get(i));
	    	maList2.add(entry2);
	    }
	   
	    return new EMAEntity(maList1,maList2);
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
		for(int i=20;i<list.size();i++) {
	    	StockPriceVo price=list.get(i);
	    	Entry upValue=boll.getUpList().get(i);
	    	Entry midValue=boll.getMidList().get(i);
	    	Entry lowerValue=boll.getLowerList().get(i);
	    	boolean isSell=false;
	    	boolean isBuy=false;
	    	double buyPoint=midValue.getY()*0.97;
	    	double stopLossPoint=midValue.getY()*0.95;
	    	double sellPoint=upValue.getY();
	    	if(price.getOpen().doubleValue()==0) {
	    		price.setOpen(price.getClose());
	    	}
	    	
	    	double avg=(price.getOpen().doubleValue()+price.getClose().doubleValue())/2;
	    	int numList=0;
	    	for(TradingRecordDo rc:rslist) {
	    		if(rc.getOptions()==TradingRecordDo.options_buy) {
	    			isSell=true;
	    			numList+=rc.getNum();
	    		}
	    		if(rc.getOptions()==TradingRecordDo.options_sell) {
	    			isSell=false;
	    			numList-=rc.getNum();
	    		}
	    	}
	    	if(isSell && avg >= sellPoint) {
	    		double total=account.getTotal().doubleValue()+numList*avg;
	    		account.setTotal(new BigDecimal(total));
	    		
	    		TradingRecordDo buyRecord=new TradingRecordDo(
	    				DateUtils.getDateForYYYYMMDDHHMM_NUMBER(price.getHistoryAll()),
	    				price.getNumber(),
	    				price.getName(),
	    				config.getDtId(),
	    				price.getOpen(),
	    				numList,
	    				TradingRecordDo.options_sell,
	    				"\n操作建议：分批止盈"
	    				+"\n买入点："+new BigDecimal(buyPoint).setScale(2,BigDecimal.ROUND_DOWN)+"-"+new BigDecimal(midValue.getY()*0.99).setScale(2,BigDecimal.ROUND_DOWN)
	    				+"\n止损点："+new BigDecimal(stopLossPoint).setScale(2,BigDecimal.ROUND_DOWN)
	    				+"\n止盈点："+new BigDecimal(sellPoint).setScale(2,BigDecimal.ROUND_DOWN)+"-"+new BigDecimal(upValue.getY()*1.02).setScale(2,BigDecimal.ROUND_DOWN)
	    				);
	    		rslist.add(buyRecord);
	    		continue;
	    	}
	    	if(isSell && avg <= stopLossPoint) {
	    		double total=account.getTotal().doubleValue()+numList*avg;
	    		account.setTotal(new BigDecimal(total));
	    		
	    		TradingRecordDo buyRecord=new TradingRecordDo(
	    				DateUtils.getDateForYYYYMMDDHHMM_NUMBER(price.getHistoryAll()),
	    				price.getNumber(),
	    				price.getName(),
	    				config.getDtId(),
	    				price.getOpen(),
	    				numList,
	    				TradingRecordDo.options_sell,
	    				"\n操作建议：分批止损"
	    				+"\n买入点："+new BigDecimal(buyPoint).setScale(2,BigDecimal.ROUND_DOWN)+"-"+new BigDecimal(midValue.getY()*0.99).setScale(2,BigDecimal.ROUND_DOWN)
	    				+"\n止损点："+new BigDecimal(stopLossPoint).setScale(2,BigDecimal.ROUND_DOWN)
	    				+"\n止盈点："+new BigDecimal(sellPoint).setScale(2,BigDecimal.ROUND_DOWN)+"-"+new BigDecimal(upValue.getY()*1.02).setScale(2,BigDecimal.ROUND_DOWN)
	    				);
	    		rslist.add(buyRecord);
	    		continue;
	    	}
	    	
	    	
	    	
	    	//趋势向上
	    	if(boll.getMidList().get(i-10).getY() < boll.getMidList().get(i).getY()*1.02) {
	    		isBuy=true;
	    	} 
	    	//开盘与收盘价格需要覆盖中轨低1% 价格要覆盖
	    	if(isBuy && price.getLow().doubleValue() <= buyPoint &&  price.getLow().doubleValue() >= stopLossPoint) {
	    		isBuy=true;
	    	}  
	    	
	    	if(isBuy) {
				double sotck = account.getTotal().intValue() * 0.25 / (avg * 100);
				int num = (int) (sotck - 1) * 100;
	    		if(num <=0) {
	    			continue;
	    		}
	    		double total=account.getTotal().doubleValue()-num*avg;
	    		account.setTotal(new BigDecimal(total));
	    		TradingRecordDo buyRecord=new TradingRecordDo(
	    				DateUtils.getDateForYYYYMMDDHHMM_NUMBER(price.getHistoryAll()),
	    				price.getNumber(),
	    				price.getName(),
	    				config.getDtId(),
	    				new BigDecimal(avg),
	    				num,
	    				TradingRecordDo.options_buy,
	    				"\n操作建议：分批买入"
	    				+"\n买入点："+new BigDecimal(buyPoint).setScale(2,BigDecimal.ROUND_DOWN)+"-"+new BigDecimal(midValue.getY()*0.99).setScale(2,BigDecimal.ROUND_DOWN)
	    				+"\n止损点："+new BigDecimal(stopLossPoint).setScale(2,BigDecimal.ROUND_DOWN)
	    				+"\n止盈点："+new BigDecimal(sellPoint).setScale(2,BigDecimal.ROUND_DOWN)+"-"+new BigDecimal(upValue.getY()*1.02).setScale(2,BigDecimal.ROUND_DOWN)
	    				);
	    		
	    		rslist.add(buyRecord);
	    	}else {
	    		String remark="\n操作建议：空仓观望";
	    		if(isSell) {
	    			remark="\n操作建议：持股待涨";
				}
	    		TradingRecordDo buyRecord=new TradingRecordDo(
	    				DateUtils.getDateForYYYYMMDDHHMM_NUMBER(price.getHistoryAll()),
	    				price.getNumber(),
	    				price.getName(),
	    				config.getDtId(),
	    				new BigDecimal(avg),
	    				0,
	    				TradingRecordDo.options_nothink,
	    				remark+"\n买入点："+new BigDecimal(buyPoint).setScale(2,BigDecimal.ROUND_DOWN)+"-"+new BigDecimal(midValue.getY()*0.99).setScale(2,BigDecimal.ROUND_DOWN)
	    				+"\n止损点："+new BigDecimal(stopLossPoint).setScale(2,BigDecimal.ROUND_DOWN)
	    				+"\n止盈点："+new BigDecimal(sellPoint).setScale(2,BigDecimal.ROUND_DOWN)+"-"+new BigDecimal(upValue.getY()*1.02).setScale(2,BigDecimal.ROUND_DOWN)
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
