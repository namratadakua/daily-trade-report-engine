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

import dailytradereportingengine.constants.ArabicWorkingDays;
import dailytradereportingengine.constants.WorkingDays;
import dailytradereportingengine.dto.ReportResponseDTO;
import dailytradereportingengine.dto.TradeDTO;;

/**
 * This service class has functionality related to daily trade reporting
 * 
 * @author namratadakua
 *
 */
public class ReportEngineService {

	/**
	 * This method generates report for daily trades
	 * 
	 * @param trades
	 * @return ReportResponse
	 */
	public ReportResponseDTO generateReport(List<TradeDTO> trades) {

		if (CollectionUtils.isEmpty(trades)) {
			System.out.println("No data found for generating report");
			return null;
		}

		Map<Date, Double> settlementBuyMap = new HashMap<>();

		Map<Date, Double> settlementSellMap = new HashMap<>();

		List<TradeDTO> buyTrades = new ArrayList<>();
		List<TradeDTO> sellTrades = new ArrayList<>();

		for (TradeDTO trade : trades) {

			Date settledDate = getSettledDate(trade.getSettlmentDate(), trade.getCurrency());
			trade.setSettledDate(settledDate);

			double usdSettledAmount = trade.getAgreedFx() * trade.getPricePerUnit() * trade.getUnits();
			trade.setUsdSettledAmount(usdSettledAmount);
			if (trade.getInstructionType() == 'B') {
				buyTrades.add(trade);
				if (settlementBuyMap.containsKey(settledDate)) {
					settlementBuyMap.put(settledDate, settlementBuyMap.get(settledDate) + usdSettledAmount);
				} else {
					settlementBuyMap.put(settledDate, usdSettledAmount);
				}
			} else {
				sellTrades.add(trade);
				if (settlementSellMap.containsKey(settledDate)) {
					settlementSellMap.put(settledDate, settlementSellMap.get(settledDate) + usdSettledAmount);
				} else {
					settlementSellMap.put(settledDate, usdSettledAmount);
				}
			}

		}

		assignRank(buyTrades);
		assignRank(sellTrades);

		System.out.println("=========== Amount in USD settled incoming everyday ==========");
		settlementSellMap = printUsdAmountPerDayReport(settlementSellMap);

		System.out.println();
		System.out.println("=========== Amount in USD settled outgoing everyday ==========");
		settlementBuyMap = printUsdAmountPerDayReport(settlementBuyMap);

		System.out.println();
		System.out.println("=========== Ranking of Entities based on Incoming Amount ==========");
		printEntityRank(sellTrades);

		System.out.println();
		System.out.println("=========== Ranking of Entities based on outgoing Amount ==========");
		printEntityRank(buyTrades);

		ReportResponseDTO response = new ReportResponseDTO(settlementBuyMap, settlementSellMap, buyTrades, sellTrades);
		return response;
	}

	/**
	 * This methods finds the actual settlement date for a trade
	 * 
	 * @param settlementDate
	 * @param currency
	 * @return Date
	 */
	private Date getSettledDate(Date settlementDate, String currency) {

		Date settledDate = settlementDate;
		if (StringUtils.equals(currency, "AED") || StringUtils.equals(currency, "SAR")) {
			settledDate = getNextArabicWorkigDay(settledDate);

		} else {
			settledDate = getNextWorkigDay(settlementDate);

		}
		return settledDate;
	}

	/**
	 * This methods finds the next Arabic working day recursively 
	 * 
	 * @param date
	 * @return Date
	 */
	private Date getNextArabicWorkigDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

		boolean workingDay = ArabicWorkingDays.isWorkingDay(dayOfWeek);

		if (workingDay) {
			return date;
		} else {
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			return getNextArabicWorkigDay(calendar.getTime());
		}
	}

	/**
	 * This methods find the next working day recursively
	 * 
	 * @param date
	 * @return Date
	 */
	private Date getNextWorkigDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

		boolean workingDay = WorkingDays.isWorkingDay(dayOfWeek);

		if (workingDay) {
			return date;
		} else {
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			return getNextWorkigDay(calendar.getTime());
		}
	}

	/**
	 * This methods assigns rank to a trade for a day based on settlement USD
	 * amount
	 * 
	 * @param trades
	 */
	private void assignRank(List<TradeDTO> trades) {

		if (CollectionUtils.isEmpty(trades)) {
			return;
		}

		trades.sort(Comparator.comparing(TradeDTO::getSettledDate).thenComparing(TradeDTO::getUsdSettledAmount,
				Comparator.reverseOrder()));
		int rankIndex = 1;
		double rankUsdAmount = trades.get(0).getUsdSettledAmount();
		Date settledDate = trades.get(0).getSettledDate();

		trades.get(0).setRank(rankIndex);

		for (int index = 1; index < trades.size(); index++) {
			TradeDTO trade = trades.get(index);
			if (settledDate.compareTo(trade.getSettledDate()) == 0) {
				if (trade.getUsdSettledAmount() == rankUsdAmount) {
					trade.setRank(rankIndex);
				} else {
					rankUsdAmount = trade.getUsdSettledAmount();
					trade.setRank(++rankIndex);
				}
			} else {
				rankIndex = 1;
				trade.setRank(rankIndex);
				settledDate = trade.getSettledDate();
				rankUsdAmount = trade.getUsdSettledAmount();
			}
		}
	}

	/**
	 * This method prints the report of settlement date and total
	 * incoming/outing USD amount
	 * 
	 * @param settlementMap
	 * @return LinkedHashMap<Date, Double>
	 */
	private LinkedHashMap<Date, Double> printUsdAmountPerDayReport(Map<Date, Double> settlementMap) {

		System.out.println();
		System.out.print("Settled Date   | Total Settlement Amount of day");
		System.out.println();
		LinkedHashMap<Date, Double> sortedMap = new LinkedHashMap<>();

		settlementMap.entrySet().stream().sorted(Map.Entry.comparingByKey())
				.forEachOrdered(t -> sortedMap.put(t.getKey(), t.getValue()));

		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
		for (Entry<Date, Double> tradeEntry : sortedMap.entrySet()) {
			System.out.println();
			System.out.print(sdf.format(tradeEntry.getKey()));
			System.out.print("  |  ");
			System.out.print(tradeEntry.getValue());
			System.out.println();
		}
		return sortedMap;
	}

	/**
	 * This method prints the daily report of trades based on settlement date
	 * and rank of the trade for the date
	 * 
	 * @param trades
	 */
	private void printEntityRank(List<TradeDTO> trades) {
		System.out.println();
		System.out.print("Settles Date   |  Entity | Settled Amount | Rank");
		System.out.println();
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
		trades.stream().forEach(t -> {
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
