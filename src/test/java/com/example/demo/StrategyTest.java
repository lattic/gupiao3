package com.example.demo;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jfree.data.time.TimeSeriesCollection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeries;
import org.ta4j.core.indicators.EMAIndicator;
import org.ta4j.core.indicators.SMAIndicator;
import org.ta4j.core.indicators.bollinger.BollingerBandsLowerIndicator;
import org.ta4j.core.indicators.bollinger.BollingerBandsMiddleIndicator;
import org.ta4j.core.indicators.bollinger.BollingerBandsUpperIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.indicators.statistics.StandardDeviationIndicator;

import com.example.chart.base.entity.Candle;
import com.example.chart.base.entity.Entry;
import com.example.chart.entity.BollEntity;
import com.example.mapper.HistoryDayStockMapper;
import com.example.model.RealTimeDo;
import com.example.model.StockPriceVo;
import com.example.service.GuPiaoService;
import com.example.service.TrendStrategyService;
import com.example.uitls.DateUtils;
import com.example.uitls.ReadApiUrl;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { GupiaoApplication.class })
public class StrategyTest {
	@Autowired
	private TrendStrategyService trendStrategyService;
	
	@Autowired
	private HistoryDayStockMapper historyDayStockMapper;
	
	@Autowired
	private ReadApiUrl readApiUrl;
	
	@Autowired
	private GuPiaoService guPiaoService;
	
	private String number="sh600305";
	
	@Test
	public void Test() {
		
		List<StockPriceVo> spList=trendStrategyService.transformByDayLine(historyDayStockMapper.getNumber(number));
		List<Candle> originData=trendStrategyService.transformStockPrice(spList);
		
		
		
		
	}
}
