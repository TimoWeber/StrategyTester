package trading.weber.StrategyTester.controller;

import org.springframework.stereotype.Controller;
import org.ta4j.core.Strategy;
import org.ta4j.core.TimeSeries;
import org.ta4j.core.TimeSeriesManager;
import org.ta4j.core.TradingRecord;



@Controller
public class StrategyController {
	public TradingRecord testStrategy(TimeSeries timeSeries, Strategy tradingStrategy) {
		TimeSeriesManager seriesManager = new TimeSeriesManager(timeSeries);
		TradingRecord tradingRecord = seriesManager.run(tradingStrategy);
		
		return tradingRecord;
	}
}
