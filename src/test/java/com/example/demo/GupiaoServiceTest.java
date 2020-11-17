package com.example.demo;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.example.ai.MockDeal;
import com.example.mapper.HistoryStockMapper;
import com.example.mapper.RobotAccountMapper;
import com.example.mapper.RobotSetMapper;
import com.example.mapper.TradingRecordMapper;
import com.example.model.HistoryPriceDo;
import com.example.model.HistoryStockDo;
import com.example.model.RobotAccountDo;
import com.example.model.RobotSetDo;
import com.example.model.TradingRecordDo;
import com.example.service.GuPiaoService;
import com.example.service.task.MonitorTask;
import com.example.uitls.DateUtils;
import com.example.uitls.RedisUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { GupiaoApplication.class })
public class GupiaoServiceTest {
	private static String appSecret = "bb888ac7199ba68c327c8a0e44fbf0ee6b65b5b0f490beb39a209a295e132a4f";

	@Autowired
	private RobotSetMapper robotSetMapper;
	@Autowired
	private RobotAccountMapper robotAccountMapper;
	@Autowired
	private TradingRecordMapper tradingRecordMapper;
	// @Autowired
	private MonitorTask monitorTask;
	@Autowired
	private HistoryStockMapper historyStockMapper;
	@Autowired
	private GuPiaoService guPiaoService;
	@Autowired
	private MockDeal mockDeal;

	@Resource
	private RedisUtil redisUtil;

	// @Test
	public void ramTest() {
		redisUtil.set("test", "34323423");
		System.out.println(redisUtil.get("test"));
	}

	@Test
	public void mockDeal() {
		mockDeal.mockDeal("sz399006", "2020-07-15", appSecret, true);
	}

	// @Test
	public void mock() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		guPiaoService.timeInterval("sh000001");
		List<HistoryPriceDo> list = mockDeal.cutList("sh000001", "2020-07-15", "2020-07-21");
		for (HistoryPriceDo price : list) {
			System.out.println(sdf.format(price.getDateime()));
		}
	}

	// @Test
	public void AiBuyIn() {
		monitorTask.AiBuyIn();
	}

	// @Test
	public void updateHistory() {
		guPiaoService.updateHistoryStock("sh601702");
	}

	// @Test
	public void addHistory() {
		HistoryStockDo tr = new HistoryStockDo();
		tr.setKaipanjia(new BigDecimal("10.223"));
		tr.setMa20Day(new BigDecimal("10.223"));
		tr.setMa20Hour(new BigDecimal("10.223"));
		tr.setShoupanjia(new BigDecimal("10.223"));
		tr.setHistoryDay("2323");
		tr.setNumber("3434");
		historyStockMapper.insert(tr);
	}

	// @Test
	public void delHistory() {
		for (HistoryStockDo rs : historyStockMapper.getAll()) {
			System.out.println(JSON.toJSONString(rs));
			historyStockMapper.delete(rs.getId());
		}
		;
	}

	// @Test
	public void addTradingRecord() {
		TradingRecordDo tr = new TradingRecordDo();
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

	// @Test
	public void delTradingRecord() {
		for (TradingRecordDo tr : tradingRecordMapper.getAll()) {
			System.out.println(JSON.toJSONString(tr));
			tradingRecordMapper.delete(tr.getId());
		}
		;
	}

	// @Test
	public void addRobotAccount() {
		RobotAccountDo ra = new RobotAccountDo();
		ra.setRobotId(342343234L);
		ra.setTotal(new BigDecimal("10.223"));
		robotAccountMapper.insert(ra);
	}

	// @Test
	public void delRobotAccount() {
		for (RobotAccountDo ra : robotAccountMapper.getAll()) {
			System.out.println(JSON.toJSONString(ra));
			robotAccountMapper.delete(ra.getId());
		}
		;
	}

	// @Test
	public void addRobotSet() {
		RobotSetDo rs = new RobotSetDo();
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

	// @Test
	public void delRobotSet() {
		for (RobotSetDo rs : robotSetMapper.getAll()) {
			System.out.println(JSON.toJSONString(rs));
			robotSetMapper.delete(rs.getId());
		}
		;
	}

}
