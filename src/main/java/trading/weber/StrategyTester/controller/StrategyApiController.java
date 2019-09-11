package trading.weber.StrategyTester.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.ta4j.core.Strategy;
import org.ta4j.core.TimeSeries;
import org.ta4j.core.Trade;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.analysis.criteria.AverageProfitCriterion;
import org.ta4j.core.analysis.criteria.AverageProfitableTradesCriterion;
import org.ta4j.core.analysis.criteria.LinearTransactionCostCriterion;
import org.ta4j.core.analysis.criteria.MaximumDrawdownCriterion;
import org.ta4j.core.analysis.criteria.NumberOfBarsCriterion;
import org.ta4j.core.analysis.criteria.NumberOfTradesCriterion;
import org.ta4j.core.analysis.criteria.RewardRiskRatioCriterion;
import org.ta4j.core.analysis.criteria.TotalProfitCriterion;

import trading.weber.StrategyTester.entity.ApiConvertedTrade;
import trading.weber.StrategyTester.strategy.TwoPeriodRsiStrategy;

@RestController
@RequestMapping("/api/strategy")
@CrossOrigin(origins = "http://localhost:4200")
public class StrategyApiController {
	
	@GetMapping("/trades/all")
	public Map<String, Object> all(){
//		create time series
		SeriesController seriesController = new SeriesController();
		TimeSeries timeSeries = seriesController.createTimeSeries("dow");
		
//		create strategy
		Strategy tradingStrategy = new TwoPeriodRsiStrategy().buildStrategy(timeSeries);
		
//		test strategy 
		StrategyController strategyController = new StrategyController();
		TradingRecord tradingRecord = strategyController.testStrategy(timeSeries, tradingStrategy);
				
		return this.convertTrades(tradingRecord, timeSeries);
	}
	
	@RequestMapping(value = "/**", method = RequestMethod.OPTIONS)
    public ResponseEntity handle() {
        return new ResponseEntity(HttpStatus.OK);
    }
	
	public Map<String, Object> convertTrades(TradingRecord tradingRecord, TimeSeries timeSeries) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		List<Trade> trades = tradingRecord.getTrades();
		List<ApiConvertedTrade> apiConvertedTrades = new ArrayList<ApiConvertedTrade>();
		for (Trade trade : trades) {
			ApiConvertedTrade newTrade = new ApiConvertedTrade();
			newTrade.setTradeBuy(trade.getEntry().getPrice());
			newTrade.setTradeSell(trade.getExit().getPrice());
			newTrade.setCloseDate(
					timeSeries.getBar(trade.getExit().getIndex()).getEndTime()
					);	        
			apiConvertedTrades.add(newTrade);
	        }
		
        this.getTradeAnalysis(tradingRecord, timeSeries);
        returnMap.put("tradeAnalysis", this.getTradeAnalysis(tradingRecord, timeSeries));
        returnMap.put("trades", apiConvertedTrades);
		
		return returnMap;
	}
	private Map<String, Double> getTradeAnalysis(TradingRecord tradingRecord, TimeSeries timeSeries) {
		Map<String, Double> tradeAnalysis = new HashMap<String, Double>();
        TotalProfitCriterion totalProfit = new TotalProfitCriterion();

	    // Total profit
        tradeAnalysis.put("totalProfit", (totalProfit.calculate(timeSeries, tradingRecord)).doubleValue());
        // Number of bars
        tradeAnalysis.put("noOfBars", (new NumberOfBarsCriterion().calculate(timeSeries, tradingRecord)).doubleValue());
        // Average profit (per bar)
        tradeAnalysis.put("averageProfit", (new AverageProfitCriterion().calculate(timeSeries, tradingRecord)).doubleValue());
        // Number of trades
        tradeAnalysis.put("noOfTrades", (new NumberOfTradesCriterion().calculate(timeSeries, tradingRecord)).doubleValue());
        // Profitable trades ratio
        tradeAnalysis.put("profitableTrades", (new AverageProfitableTradesCriterion().calculate(timeSeries, tradingRecord)).doubleValue());
        // Maximum drawdown
        tradeAnalysis.put("maxDrawdown", (new MaximumDrawdownCriterion().calculate(timeSeries, tradingRecord)).doubleValue());
        // Reward-risk ratio
        tradeAnalysis.put("rewardRisk", (new RewardRiskRatioCriterion().calculate(timeSeries, tradingRecord)).doubleValue());
        // Total transaction cost
        tradeAnalysis.put("totalTransactionCost", (new LinearTransactionCostCriterion(1000, 0.005).calculate(timeSeries, tradingRecord)).doubleValue());
        
        return tradeAnalysis;
	}
}
