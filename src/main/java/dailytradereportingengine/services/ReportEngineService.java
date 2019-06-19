package dailytradereportingengine.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import dailytradereportingengine.dto.ReportResponse;
import dailytradereportingengine.dto.Trade;


public class ReportEngineService {
	
	public ReportResponse generateReport(List<Trade> trades) {
		
		if(CollectionUtils.isEmpty(trades)) {
			System.out.println("No data found for generating report");
			return null;
		}
		
		Map<Date, Double> settlementBuyMap = new HashMap<>();
		
		Map<Date, Double> settlementSellMap = new HashMap<>();
		
		List<Trade> buyTrades = new ArrayList<>();
		List<Trade> sellTrades = new ArrayList<>();
		
		for (Trade trade : trades) {
			
			Date settledDate = trade.getSettlmentDate();
			
			SimpleDateFormat simpleDateformat = new SimpleDateFormat("EEEE"); // the day of the week spelled out completely
	        System.out.println(simpleDateformat.format(trade.getSettlmentDate()));
			
			Calendar calendar = Calendar.getInstance();
	        calendar.setTime(trade.getSettlmentDate());
	        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
	        
	        if(StringUtils.equals(trade.getCurrency(), "AED") 
	        		|| StringUtils.equals(trade.getCurrency(), "SAR")) {
	        	if (dayOfWeek == Calendar.FRIDAY) {
	        		calendar.add(Calendar.DAY_OF_MONTH, 2);
	        		settledDate = calendar.getTime();
	        	}
	        	if (dayOfWeek == Calendar.SATURDAY) {
	        		calendar.add(Calendar.DAY_OF_MONTH, 1);
	        		settledDate = calendar.getTime();
	        	}
	        	
	        }else {
	        	if (dayOfWeek == Calendar.SATURDAY) {
	        		calendar.add(Calendar.DAY_OF_MONTH, 2);
	        		settledDate = calendar.getTime();
	        	}
	        	if (dayOfWeek == Calendar.SUNDAY) {
	        		calendar.add(Calendar.DAY_OF_MONTH, 1);
	        		settledDate = calendar.getTime();
	        	}
	        	
	        }
			trade.setSettledDate(settledDate);
			System.out.println(settledDate);
			
			double usdSettledAmount = trade.getAgreedFx() * trade.getPricePerUnit() * trade.getUnits();
			trade.setUsdSettledAmount(usdSettledAmount);
			if(trade.getInstructionType() == 'B') {
				buyTrades.add(trade);
				if(settlementBuyMap.containsKey(settledDate)) {
					settlementBuyMap.put(settledDate, 
							settlementBuyMap.get(settledDate) + usdSettledAmount);
				}else {
					settlementBuyMap.put(settledDate, usdSettledAmount);
				}
			} else {
				sellTrades.add(trade);
				if (settlementSellMap.containsKey(settledDate)) {
					settlementSellMap.put(settledDate, 
							settlementSellMap.get(settledDate) + usdSettledAmount);
				}else {
					settlementSellMap.put(settledDate, usdSettledAmount);
				}
			}
			
		}
		
		assignRank(buyTrades);
		assignRank(sellTrades);
		
		System.out.println("=========== Amount in USD settled incoming everyday ==========");
		printUsdAmountPerDayReport(settlementSellMap);
		
		System.out.println();
		System.out.println("=========== Amount in USD settled outgoing everyday ==========");
		printUsdAmountPerDayReport(settlementBuyMap);
		
		System.out.println();
		System.out.println("=========== Ranking of Entities based on Incoming Amount ==========");
		printEntityRank(sellTrades);
		
		System.out.println();
		System.out.println("=========== Ranking of Entities based on outgoing Amount ==========");
		printEntityRank(buyTrades);
		
		ReportResponse response = new ReportResponse(settlementBuyMap, settlementSellMap, buyTrades, sellTrades);
		return response;
	}
	
	private void assignRank(List<Trade> trades) {
		
		trades.sort(Comparator.comparing(Trade :: getSettledDate).thenComparing(Trade :: getUsdSettledAmount));
		int rankIndex = 1;
		double rankUsdAmount = trades.get(0).getUsdSettledAmount();
		
		trades.get(0).setRank(rankIndex);
		
		for (int index = 1; index < trades.size(); index++) {
			if (trades.get(index).getUsdSettledAmount() == rankUsdAmount) {
				trades.get(index).setRank(rankIndex);
			}else {
				rankUsdAmount = trades.get(index).getUsdSettledAmount();
				trades.get(index).setRank(++rankIndex);
			}
		}
	}
	
	private void printUsdAmountPerDayReport(Map<Date, Double> settlementMap) {
		
		LinkedHashMap<Date, Double> sortedMap = new LinkedHashMap<>();
		
		settlementMap
		.entrySet()
		.stream()
		.sorted(Map.Entry.comparingByKey())
		.forEachOrdered(t -> sortedMap.put(t.getKey(), t.getValue()));

		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
		for (Entry<Date, Double> tradeEntry : sortedMap.entrySet()) {
			System.out.println();
			System.out.print(sdf.format(tradeEntry.getKey()));
			System.out.print("  |  ");
			System.out.print(tradeEntry.getValue());
			System.out.println();
		}
	}
	
	private void printEntityRank(List<Trade> trades) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
		trades
		.stream()
		.forEach(t -> {
			System.out.println();
			System.out.print(sdf.format(t.getSettledDate()));
			System.out.print("  |  ");
			System.out.print(t.getEntity());
			System.out.print("  |  ");
			System.out.print(t.getUsdSettledAmount());
			System.out.print("  |  ");
			System.out.print(t.getRank());
			System.out.println();
		});
		
		
	}
}
