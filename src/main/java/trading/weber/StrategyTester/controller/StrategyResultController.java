package trading.weber.StrategyTester.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.ta4j.core.Strategy;
import org.ta4j.core.TimeSeries;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.analysis.criteria.*;


import trading.weber.StrategyTester.strategy.TwoPeriodRsiStrategy;

@Controller
public class StrategyResultController {

	@GetMapping("/")
	public String strategyResult(Model model) {
//		create time series
		SeriesController seriesController = new SeriesController();
		TimeSeries timeSeries = seriesController.createTimeSeries("dow");
		
//		create strategy
		Strategy tradingStrategy = new TwoPeriodRsiStrategy().buildStrategy(timeSeries);
		
//		test strategy 
		StrategyController strategyController = new StrategyController();
		TradingRecord tradingRecord = strategyController.testStrategy(timeSeries, tradingStrategy);


		
		model.addAttribute("profit", new TotalProfitCriterion().calculate(timeSeries, tradingRecord));
		model.addAttribute("numberOfBars", new NumberOfBarsCriterion().calculate(timeSeries, tradingRecord));
		model.addAttribute("averageProfit", new AverageProfitCriterion().calculate(timeSeries, tradingRecord));
		model.addAttribute("numberOfTrades", new NumberOfTradesCriterion().calculate(timeSeries, tradingRecord));
		model.addAttribute("profitableTradesRatio", new AverageProfitableTradesCriterion().calculate(timeSeries, tradingRecord));
		model.addAttribute("maxDrawdown", new MaximumDrawdownCriterion().calculate(timeSeries, tradingRecord));
		model.addAttribute("rewardRiskRatio", new RewardRiskRatioCriterion().calculate(timeSeries, tradingRecord));
		model.addAttribute("totalTransactionCost", new LinearTransactionCostCriterion(1000, 0.005).calculate(timeSeries, tradingRecord));
		model.addAttribute("buyAndHold", new BuyAndHoldCriterion().calculate(timeSeries, tradingRecord));
		model.addAttribute("customVsbuyAndHold", new VersusBuyAndHoldCriterion(new TotalProfitCriterion()).calculate(timeSeries, tradingRecord));
		return "strategyResult";
	}
}
