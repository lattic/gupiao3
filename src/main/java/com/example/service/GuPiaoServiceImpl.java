package com.example.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ai.MockDeal;
import com.example.mapper.GuPiaoMapper;
import com.example.mapper.HistoryStockMapper;
import com.example.mapper.HolidayMapper;
import com.example.mapper.RealTimeMapper;
import com.example.mapper.RobotAccountMapper;
import com.example.mapper.RobotSetMapper;
import com.example.mapper.StockMapper;
import com.example.mapper.SubscriptionMapper;
import com.example.mapper.TradingRecordMapper;
import com.example.model.GuPiaoDo;
import com.example.model.HistoryPriceDo;
import com.example.model.HistoryStockDo;
import com.example.model.HolidayDo;
import com.example.model.RealTimeDo;
import com.example.model.RobotAccountDo;
import com.example.model.RobotSetDo;
import com.example.model.StockDo;
import com.example.model.SubscriptionDo;
import com.example.model.TradingRecordDo;
import com.example.uitls.DateUtils;
import com.example.uitls.ReadUrl;
import com.example.uitls.RedisUtil;

@Service
public class GuPiaoServiceImpl implements GuPiaoService, InitializingBean {

	private static Logger logger = LoggerFactory.getLogger(GuPiaoServiceImpl.class);

	@Autowired
	private GuPiaoMapper guPiaoMapper;

	@Autowired
	private RealTimeMapper realTimeMapper;

	@Autowired
	private StockMapper stockMapper;

	@Autowired
	private SubscriptionMapper subscriptionMapper;

	@Autowired
	private RobotSetMapper robotSetMapper;
	
	@Autowired
	private RobotAccountMapper robotAccountMapper;
	
	@Autowired
	private TradingRecordMapper tradingRecordMapper;
	
	@Autowired
	private HistoryStockMapper historyStockMapper;
	
	@Autowired
	private HolidayMapper holidayMapper;
	
	@Autowired
	private MockDeal mockDeal;
	
	@Resource
	private RedisUtil redisUtil;
	


	private void updateHistoryStockByDB(List<HistoryStockDo> list,Integer type,String remark) {
		BigDecimal max=new BigDecimal(0.00);
		BigDecimal min=new BigDecimal(100000.00);
		BigDecimal avg=new BigDecimal(0.00);
		for(HistoryStockDo stock:list) {
			if(stock.getHeight().compareTo(max)>= 1) {
				max=stock.getHeight();
			}
			if(stock.getLow().compareTo(min)<= -1) {
				min=stock.getLow();
			}
			avg=avg.add(stock.getShoupanjia());
		}
		avg=avg.divide(new BigDecimal(list.size()),3,BigDecimal.ROUND_HALF_UP);
		for(HistoryStockDo stock:list) {
			stock.setBoxMax(max);
			stock.setBoxMin(min);
			stock.setBoxAvg(avg);
			stock.setType(type);
			stock.setRemark(remark);
			try {
				historyStockMapper.updateHistoryStock(stock);
			}catch (Exception e) {
				e.printStackTrace();
				System.err.println(remark);
			}
		}
	}
	
	
	@Override
	public void updateHistoryStock(String number) {
		List<HistoryPriceDo> list = ReadUrl.readUrl(number, 60);
		if (list == null || list.isEmpty()) {
			logger.warn("没有获取到数据");
			return;
		}
		final SimpleDateFormat df1 = new SimpleDateFormat("yyyyMMdd");// 设置日期格式
		final SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMddHHmm");// 设置日期格式
		for (HistoryPriceDo price : list) {
			HistoryStockDo tr = new HistoryStockDo();
			tr.setKaipanjia(price.getKaipanjia());
			tr.setShoupanjia(price.getShoupanjia());
			tr.setHeight(price.getZuigaojia());
			tr.setLow(price.getZuidijia());
			tr.setMa20Hour(price.getMa20());
			tr.setMa20Day(new BigDecimal(0));
			tr.setHistoryDay(df1.format(price.getDateime()));
			tr.setHistoryAll(df2.format(price.getDateime()));
			tr.setNumber(number);
			if (historyStockMapper.getByTime(tr) == null) {
				historyStockMapper.insert(tr);
			}
		}
	}

	@Override
	public boolean realTimeInsert(RealTimeDo model) {
		try {
			int i = realTimeMapper.insert(model);
			return i > 0 ? true : false;
		} catch (Exception ex) {

		}
		return false;
	}

	@Override
	public boolean guPiaoInsert(GuPiaoDo model) {
		GuPiaoDo data = guPiaoMapper.getNumber(model.getNumber());
		if (data != null) {
			guPiaoMapper.delete(data.getId());
		}
		int i = guPiaoMapper.insert(model);
		return i > 0 ? true : false;
	}

	@Override
	public List<GuPiaoDo> listAll() {
		return guPiaoMapper.getAll();
	}

	@Override
	public StockDo getNumber(String number) {
		try {
			return stockMapper.getNumber(number);
		} catch (Exception ex) {
		}
		return null;
	}

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	@Override
	public void updateStock(String number, String name, int type) {
		StockDo obj = stockMapper.getNumber(number);
		if (obj == null) {
			obj = new StockDo();
			obj.setCreateDate(new Date());
			obj.setNumber(number);
			obj.setName(name);
			obj.setType(type);
			obj.setStatus(1);
			stockMapper.insert(obj);
		}
	}

	@Override
	public List<StockDo> getAllStock() {
		return stockMapper.getAll();
	}

	@Override
	public List<SubscriptionDo> listMemberAll() {
		try {
			return subscriptionMapper.getAll();
		} catch (Exception ex) {
			logger.error(ex.getMessage());
		}
		return null;
	}

	@Override
	public List<HolidayDo> getHolidayList() {
		return holidayMapper.getAll();
	}

	@Override
	public HistoryPriceDo getLastZhichengwei(String number) {
		HistoryPriceDo last=new HistoryPriceDo();
		List<HistoryStockDo>priceList=mockDeal.getBoduan(number);
		int i=priceList.size()-2;
		while(last.getMa20() == null && i>0){
			HistoryStockDo lastPrice=priceList.get(i);
			HistoryStockDo nowPrice=priceList.get(priceList.size()-1);
			long days=DateUtils.getDefDays(DateUtils.getDateForYYYYMMDD(lastPrice.getHistoryDay()),DateUtils.getDateForYYYYMMDD(nowPrice.getHistoryDay()),getHolidayList());
			if(days<5) {
				i--;
				continue;
			}
			
			List<HistoryStockDo> list = mockDeal.cutList(number, lastPrice.getHistoryAll(), nowPrice.getHistoryAll());
			BigDecimal max=new BigDecimal(0.0);
			BigDecimal min=new BigDecimal(100000.0);
			BigDecimal avg=new BigDecimal(0.0);
			int count=0;
			for (HistoryStockDo price : list) {
				count++;
				avg=avg.add(price.getShoupanjia());
				if(price.getHeight().compareTo(max)>-1) {
					max=price.getHeight();
				}
				if(price.getLow().compareTo(min)< 1) {
					min=price.getLow();
				}
				
			}
			avg = avg.divide(new BigDecimal(count),2, BigDecimal.ROUND_UP);
			max=max.setScale(2);
			min=min.setScale(2);
			last.setZhichengwei(min);
			last.setYaliwei(max);
			last.setMa20(avg);
			last.setNumber(number);
			last.setName((String)redisUtil.get(number));
			return last;
		}
		return last;
	}
	
	@Override
	public String timeInterval(String number) {
		List<HistoryStockDo>priceList=mockDeal.getBoduan(number);
		String returnStr="GS======测试波段区间分隔=========\n";
		returnStr=returnStr+"股票编码："+number+" \n";
		returnStr=returnStr+"股票名称："+(String)redisUtil.get(number)+" \n";
		for(int i=1;i<priceList.size();i++) {
			HistoryStockDo lastPrice=priceList.get(i-1);
			HistoryStockDo nowPrice=priceList.get(i);
			BigDecimal subtract=nowPrice.getShoupanjia().subtract(lastPrice.getShoupanjia());
			long days=DateUtils.getDefDays(DateUtils.getDateForYYYYMMDDHHMM_NUMBER(lastPrice.getHistoryAll()),DateUtils.getDateForYYYYMMDDHHMM_NUMBER(nowPrice.getHistoryAll()),getHolidayList());
			int type=2;
			String str="下滑趋势";
			if(subtract.compareTo(new BigDecimal(0.0))>0) {
				str="上升趋势 ";
				type=1;
			}
			if(days <5 ) {
				str="震荡行情,"+str;
				type=3;
			}
			List<HistoryStockDo> list = mockDeal.cutList(number, lastPrice.getHistoryAll(), nowPrice.getHistoryAll());
			BigDecimal max=new BigDecimal(0.0);
			BigDecimal min=new BigDecimal(100000.0);
			BigDecimal avg=new BigDecimal(0.0);
			int count=0;
			
			boolean isPoint=true;
			for (HistoryStockDo price : list) {
				count++;
				avg=avg.add(price.getShoupanjia());
				if(price.getHeight().compareTo(max)>-1) {
					max=price.getHeight();
				}
				if(price.getLow().compareTo(min)< 1) {
					min=price.getLow();
				}
				if(isPoint && count==1 && StringUtils.contains(str, "下滑趋势")) {
					str=str+"\t 参考 卖出点1:"+price.getShoupanjia()+"\t";
					isPoint=false;
				}
				if(isPoint && count==1 && StringUtils.contains(str, "上升趋势")) {
					str=str+"\t 参考 买入点1:"+price.getShoupanjia()+"\t";
					isPoint=false;
				}
				if(isPoint && days>=3 && days<5 && StringUtils.contains(str, "上升趋势")) {
					str=str+"\t 买入点2:"+price.getShoupanjia()+"\t";
					isPoint=false;
				}
			}
			if(count == 0) {
				count=1;
			}
			
			avg = avg.divide(new BigDecimal(count),2, BigDecimal.ROUND_UP);
			max=max.setScale(2);
			min=min.setScale(2);
			String context=lastPrice.getHistoryDay()+"~"+nowPrice.getHistoryDay()
			+"\t 压力位:"+max
			+"\t 支撑位:"+min
			+"\t 平均值："+avg
			+"\t 趋势："+ str
			+"\t 相隔周期："+days+"交易日 \n";
			returnStr=returnStr+context;
			updateHistoryStockByDB(list,type,context);
		}
		System.out.println(returnStr);
		return returnStr;
	}

	

}
