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

public interface TrendStrategyService {

	/**
	 * 日线转换
	 * @param list
	 * @return
	 */
	List<StockPriceVo> transformByDayLine(List<HistoryDayStockDo> list);
	
	/**
	 * 分钟线转换
	 * @param list
	 * @return
	 */
	List<StockPriceVo> transformByMinuteLine(List<HistoryPriceDo> list);
	
	/**
	 * 实时转换
	 * @param list
	 * @return
	 */
	List<StockPriceVo> transformByRealTime(List<RealTimeDo> list);
	
	/**
	 * 转换到模拟结果
	 * @param list
	 * @return
	 */
	MockLog transformByTradingRecord(List<TradingRecordDo> list);
	
	/**
	 * 波段策略（60分钟，日线）
	 * @param list
	 * @param account 资金
	 * @param config  参数
	 * @return
	 */
	List<TradingRecordDo> getStrategyByBand(List<StockPriceVo> list, RobotAccountDo account,RobotSetDo config);
	
	/**
	 * 箱体操作（60分钟，日线）
	 * @param list
	 * @param account 资金
	 * @param config  参数
	 * @return
	 */
	List<TradingRecordDo> getStrategyByBox(List<StockPriceVo> list,RobotAccountDo account,RobotSetDo config);
	
	/**
	 * 反弹（60分钟，日线）
	 * @param list
	 * @param account 资金
	 * @param config  参数
	 * @return
	 */
	List<TradingRecordDo> getStrategyByRebound(List<StockPriceVo> list,RobotAccountDo account,RobotSetDo config);
	
	/**
	 * 移动平均线（60分钟，日线）
	 * @param list
	 * @param account 资金
	 * @param config  参数
	 * @return
	 */
	List<TradingRecordDo> getStrategyByMa(List<StockPriceVo> list,RobotAccountDo account,RobotSetDo config);
	
}
