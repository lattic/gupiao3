package com.example.strategy;

import java.util.List;

import com.example.model.HistoryDayStockDo;
import com.example.model.HistoryPriceDo;
import com.example.model.MockLog;
import com.example.model.RealTimeDo;
import com.example.model.RobotAccountDo;
import com.example.model.RobotSetDo;
import com.example.model.StockPriceVo;
import com.example.model.TradingRecordDo;

public class TrendStrategyServiceImpl implements TrendStrategyService {

	@Override
	public List<StockPriceVo> transformByDayLine(List<HistoryDayStockDo> list) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<StockPriceVo> transformByMinuteLine(List<HistoryPriceDo> list) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<StockPriceVo> transformByRealTime(List<RealTimeDo> list) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MockLog transformByTradingRecord(List<TradingRecordDo> list) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TradingRecordDo> getStrategyByBand(List<StockPriceVo> list, RobotAccountDo account, RobotSetDo config) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		return null;
	}


}
