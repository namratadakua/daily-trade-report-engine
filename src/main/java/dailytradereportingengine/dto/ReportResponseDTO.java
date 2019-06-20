package dailytradereportingengine.dto;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class ReportResponseDTO {

	private Map<Date, Double> settlementBuyMap;
	
	private Map<Date, Double> settlementSellMap;
	
	private List<TradeDTO> buyTrades;
	
	private List<TradeDTO> sellTrades;
	
	public ReportResponseDTO(Map<Date, Double> settlementBuyMap, Map<Date, Double> settlementSellMap,
			List<TradeDTO> buyTrades, List<TradeDTO> sellTrades) {
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

	public List<TradeDTO> getBuyTrades() {
		return buyTrades;
	}

	public void setBuyTrades(List<TradeDTO> buyTrades) {
		this.buyTrades = buyTrades;
	}

	public List<TradeDTO> getSellTrades() {
		return sellTrades;
	}

	public void setSellTrades(List<TradeDTO> sellTrades) {
		this.sellTrades = sellTrades;
	}
	
}
