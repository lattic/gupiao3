package com.example.demo;
import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.example.mapper.HistoryDayStockMapper;
import com.example.model.RobotAccountDo;
import com.example.model.RobotSetDo;
import com.example.model.StockPriceVo;
import com.example.service.GuPiaoService;
import com.example.service.TrendStrategyService;
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
		//List<Candle> originData=trendStrategyService.transformStockPrice(spList);
		RobotAccountDo account=new RobotAccountDo();
		RobotSetDo config=new RobotSetDo();
		account.setTotal(new BigDecimal(100000));
		trendStrategyService.getStrateByBoll(spList, account, config);
		
		
	}
}
