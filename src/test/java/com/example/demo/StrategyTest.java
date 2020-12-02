package com.example.demo;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.example.mapper.HistoryDayStockMapper;
import com.example.model.RobotAccountDo;
import com.example.model.RobotSetDo;
import com.example.model.StockDo;
import com.example.model.StockPriceVo;
import com.example.model.TradingRecordDo;
import com.example.service.GuPiaoService;
import com.example.service.TrendStrategyService;
import com.example.service.task.DataTask;
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
	
	@Autowired
	private DataTask dataTask;
	
	
	private static String appSecret = "bb888ac7199ba68c327c8a0e44fbf0ee6b65b5b0f490beb39a209a295e132a4f";
	private String number="sz002030";
	private static final SimpleDateFormat DF_YYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
	
	@Test
	public void TestStrategyByEMa() {
		List<StockDo> list1= new ArrayList<StockDo>();
		List<StockDo> list=guPiaoService.getAllStock();
		int k=list.size()/5;
		list1=list.subList(0, k);
		for(StockDo sk:list1) {
			List<StockPriceVo> spList=trendStrategyService.transformByDayLine(historyDayStockMapper.getNumber(sk.getNumber()));
			RobotAccountDo account=new RobotAccountDo();
			RobotSetDo config=new RobotSetDo();
			account.setTotal(new BigDecimal(100000));
			List<TradingRecordDo> rtList=trendStrategyService.getStrategyByEMA(spList, account, config);
			for(TradingRecordDo rt:rtList) {
				BigDecimal total =rt.getTotal().add(account.getTotal());
				total=total.setScale(2, BigDecimal.ROUND_UP);
				System.out.println(DF_YYYY_MM_DD.format(rt.getCreateDate())+" "+rt.getNumber()+" 当天均价："+rt.getPrice()+" "+rt.getRemark());
			}
		}
		
	}
	
	
	//@Test
	public void TestStrateByBoll() {
		
		List<StockPriceVo> spList=trendStrategyService.transformByDayLine(historyDayStockMapper.getNumber(number));
		RobotAccountDo account=new RobotAccountDo();
		RobotSetDo config=new RobotSetDo();
		account.setTotal(new BigDecimal(100000));
		List<TradingRecordDo> rtList=trendStrategyService.getStrateByBoll(spList, account, config);
		for(TradingRecordDo rt:rtList) {
			BigDecimal total =rt.getTotal().add(account.getTotal());
			total=total.setScale(2, BigDecimal.ROUND_UP);
			System.out.println(DF_YYYY_MM_DD.format(rt.getCreateDate())+" "+rt.getNumber()+" 当天均价："+rt.getPrice()+" "+rt.getRemark());
		}
	}
	
	
	//@Test
	public void excuteRunListenTest() {
		guPiaoService.updateHistoryStock("sh600305");
		guPiaoService.timeInterval("sh600305");
		dataTask.excuteRunListen("sh600305",appSecret,"20201115");
		while(true) {
			try {
				Thread.sleep(10L);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
