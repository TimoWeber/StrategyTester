package trading.weber.StrategyTester.controller;

import org.springframework.stereotype.Controller;
import org.ta4j.core.BaseTimeSeries;
import org.ta4j.core.TimeSeries;

@Controller
public class SeriesController {
	
	public TimeSeries createTimeSeries(String symbol) {
		TimeSeries timeSeries = new BaseTimeSeries.SeriesBuilder().withName("test_series").build();
		
		ApiImportController apiImporter = new ApiImportController();
		timeSeries = apiImporter.getData(symbol, timeSeries);
		return timeSeries;
	}

}
