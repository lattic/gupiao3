package com.example.demo;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.example.ai.MockDeal;
import com.example.mapper.HistoryStockMapper;
import com.example.mapper.RobotAccountMapper;
import com.example.mapper.RobotSetMapper;
import com.example.mapper.TradingRecordMapper;
import com.example.model.HistoryStockDo;
import com.example.model.RobotAccountDo;
import com.example.model.RobotSetDo;
import com.example.model.TradingRecordDo;
import com.example.service.GuPiaoService;
import com.example.service.task.MonitorTask;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {GupiaoApplication.class})
public class GupiaoServiceTest {
	private static String appSecret="bb888ac7199ba68c327c8a0e44fbf0ee6b65b5b0f490beb39a209a295e132a4f";
	
	@Autowired
	private RobotSetMapper robotSetMapper;
	@Autowired
	private RobotAccountMapper robotAccountMapper;
	@Autowired
	private TradingRecordMapper tradingRecordMapper;
	@Autowired
	private MonitorTask monitorTask;
	@Autowired
	private HistoryStockMapper historyStockMapper;
	@Autowired
	private GuPiaoService guPiaoService;
	@Autowired
	private MockDeal mockDeal;
	
	@Test
	public void mock() {
		mockDeal.mockDeal("sh601702", "2020-09-01",appSecret,true);
	}
	
	//@Test
	public void AiBuyIn() {
		monitorTask.AiBuyIn();
	}
	
	//@Test
	public void updateHistory() {
		guPiaoService.updateHistoryStock("sh601702");
	}
	
	//@Test
	public void addHistory() {
		HistoryStockDo tr= new HistoryStockDo();
		tr.setKaipanjia(new BigDecimal("10.223"));
		tr.setMa20Day(new BigDecimal("10.223"));
		tr.setMa20Hour(new BigDecimal("10.223"));
		tr.setShoupanjia(new BigDecimal("10.223"));
		tr.setHistoryDay("2323");
		tr.setNumber("3434");
		historyStockMapper.insert(tr);
	}
	
	//@Test
	public void delHistory() {
		for(HistoryStockDo rs:historyStockMapper.getAll()) {
			System.out.println(JSON.toJSONString(rs));
			historyStockMapper.delete(rs.getId());
		};
	}
	
	
	//@Test
	public void addTradingRecord() {
		TradingRecordDo tr= new TradingRecordDo();
		tr.setCreateDate(new Date());
		tr.setDtId("3f3f33f3f");
		tr.setName("nadfdf");
		tr.setNum(323423);
		tr.setNumber("sdc2v2323");
		tr.setOptions(2);
		tr.setPrice(new BigDecimal("10.223"));
		tr.setTotal(new BigDecimal("10.343223"));
		tr.setRemark("cdcdc");
		tradingRecordMapper.insert(tr);
	}
	
	//@Test
	public void delTradingRecord() {
		for(TradingRecordDo tr:tradingRecordMapper.getAll()) {
			System.out.println(JSON.toJSONString(tr));
			tradingRecordMapper.delete(tr.getId());
		};
	}
	
	
	//@Test
	public void addRobotAccount() {
		RobotAccountDo ra = new RobotAccountDo();
		ra.setRobotId(342343234L);
		ra.setTotal(new BigDecimal("10.223"));
		robotAccountMapper.insert(ra);
	}
	
	//@Test
	public void delRobotAccount() {
		for(RobotAccountDo ra:robotAccountMapper.getAll()) {
			System.out.println(JSON.toJSONString(ra));
			robotAccountMapper.delete(ra.getId());
		};
	}
	
	//@Test
	public void addRobotSet() {
		RobotSetDo rs=new RobotSetDo();
		rs.setBeginTime(new Date());
		rs.setEndTime(new Date());
		rs.setDtId("3333");
		rs.setExecutionSale(true);
		rs.setName("name");
		rs.setNumber("number");
		rs.setRobotName("testname");
		rs.setStopLossesPrice(new BigDecimal("10.223"));
		robotSetMapper.insert(rs);
	}
	
	//@Test
	public void delRobotSet() {
		for(RobotSetDo rs:robotSetMapper.getAll()) {
			System.out.println(JSON.toJSONString(rs));
			robotSetMapper.delete(rs.getId());
		};
	}
	
}
