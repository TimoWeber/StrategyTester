package trading.weber.StrategyTester.entity;

import java.time.ZonedDateTime;
import org.ta4j.core.num.Num;

public class ApiConvertedTrade {
	double tradeBuy;
	double tradeSell;
	ZonedDateTime closeDate;
	
	public ZonedDateTime getCloseDate() {
		return closeDate;
	}
	
	public void setCloseDate(ZonedDateTime closeDate) {
		this.closeDate = closeDate;
	}
	
	public double getTradeBuy() {
		return tradeBuy;
	}
	
	public void setTradeBuy(Num tradeBuy) {
		this.tradeBuy = tradeBuy.doubleValue();
	}
	
	public double getTradeSell() {
		return tradeSell;
	}
	
	public void setTradeSell(Num tradeSell) {
		this.tradeSell = tradeSell.doubleValue();
	}
}
