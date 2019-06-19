package dailytradereportingengine.dto;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class ReportResponse {

	private Map<Date, Double> settlementBuyMap;
	
	private Map<Date, Double> settlementSellMap;
	
	private List<Trade> buyTrades;
	
	private List<Trade> sellTrades;
	
	public ReportResponse(Map<Date, Double> settlementBuyMap, Map<Date, Double> settlementSellMap,
			List<Trade> buyTrades, List<Trade> sellTrades) {
		super();
		this.settlementBuyMap = settlementBuyMap;
		this.settlementSellMap = settlementSellMap;
		this.buyTrades = buyTrades;
		this.sellTrades = sellTrades;
	}

	public Map<Date, Double> getSettlementBuyMap() {
		return settlementBuyMap;
	}

	public void setSettlementBuyMap(Map<Date, Double> settlementBuyMap) {
		this.settlementBuyMap = settlementBuyMap;
	}

	public Map<Date, Double> getSettlementSellMap() {
		return settlementSellMap;
	}

	public void setSettlementSellMap(Map<Date, Double> settlementSellMap) {
		this.settlementSellMap = settlementSellMap;
	}

	public List<Trade> getBuyTrades() {
		return buyTrades;
	}

	public void setBuyTrades(List<Trade> buyTrades) {
		this.buyTrades = buyTrades;
	}

	public List<Trade> getSellTrades() {
		return sellTrades;
	}

	public void setSellTrades(List<Trade> sellTrades) {
		this.sellTrades = sellTrades;
	}
	
}
