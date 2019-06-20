package dailytradereportingengine.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import dailytradereportingengine.dto.ReportResponseDTO;
import dailytradereportingengine.dto.TradeDTO;
import dailytradereportingengine.services.ReportEngineService;
import junit.framework.TestCase;

public class TestReportEngineService extends TestCase {

	ReportEngineService reportEngineService = null;

	@Before
	public void setUp() {
		reportEngineService = new ReportEngineService();
	}

	@Test
	public void testGenerateReport_NoTrades() {

		List<TradeDTO> trades = new ArrayList<>();

		ReportResponseDTO response = reportEngineService.generateReport(trades);

		assertNull(response);
	}

	@Test
	public void testGenerateReport_BuySettlementOnWorkingDays() throws ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

		List<TradeDTO> trades = new ArrayList<TradeDTO>();

		try {
			TradeDTO trade1 = new TradeDTO("foo", 'B', 0.5, "SGP", sdf.parse("17-06-2019"), sdf.parse("18-06-2019"),
					200, 100.25);
			TradeDTO trade2 = new TradeDTO("bar", 'S', 0.22, "AED", sdf.parse("18-06-2019"), sdf.parse("20-06-2019"),
					450, 150.5);
			TradeDTO trade3 = new TradeDTO("foo", 'S', 0.5, "AED", sdf.parse("17-06-2019"), sdf.parse("20-06-2019"),
					200, 100.25);
			TradeDTO trade4 = new TradeDTO("baz", 'S', 0.5, "SGP", sdf.parse("17-06-2019"), sdf.parse("19-06-2019"),
					200, 200.25);
			TradeDTO trade5 = new TradeDTO("qux", 'B', 0.5, "SAR", sdf.parse("14-06-2019"), sdf.parse("17-06-2019"),
					300, 100.00);
			TradeDTO trade6 = new TradeDTO("foo", 'B', 0.5, "INR", sdf.parse("17-06-2019"), sdf.parse("18-06-2019"),
					200, 200.25);

			trades.add(trade1);
			trades.add(trade2);
			trades.add(trade3);
			trades.add(trade4);
			trades.add(trade5);
			trades.add(trade6);

		} catch (ParseException e) {
			e.printStackTrace();
		}

		ReportResponseDTO response = reportEngineService.generateReport(trades);

		assertNotNull(response);
		assertNotNull(response.getSettlementBuyMap());
		assertNotNull(response.getSettlementSellMap());
		assertNotNull(response.getBuyTrades());
		assertNotNull(response.getSellTrades());
		assertEquals(2, response.getSettlementSellMap().entrySet().size());
		assertEquals(2, response.getSettlementBuyMap().entrySet().size());
		assertEquals(3, response.getBuyTrades().size());
		assertEquals(3, response.getSellTrades().size());
		Map<Date, Double> settlementBuyMap = response.getSettlementBuyMap();

		Map<Date, Double> settlementSellMap = response.getSettlementSellMap();

		assertEquals(20025.0, settlementSellMap.get(sdf.parse("19-06-2019")));
		assertEquals(24924.5, settlementSellMap.get(sdf.parse("20-06-2019")));

		assertEquals(15000.0, settlementBuyMap.get(sdf.parse("17-06-2019")));
		assertEquals(30050.0, settlementBuyMap.get(sdf.parse("18-06-2019")));

		List<TradeDTO> buyTrades = response.getBuyTrades();
		List<TradeDTO> sellTrades = response.getSellTrades();

		assertEquals(sdf.parse("19-06-2019"), sellTrades.get(0).getSettledDate());
		assertEquals(1, sellTrades.get(0).getRank());
		assertEquals("baz", sellTrades.get(0).getEntity());
		assertEquals(20025.0, sellTrades.get(0).getUsdSettledAmount());

		assertEquals(sdf.parse("20-06-2019"), sellTrades.get(1).getSettledDate());
		assertEquals(1, sellTrades.get(1).getRank());
		assertEquals("bar", sellTrades.get(1).getEntity());
		assertEquals(14899.5, sellTrades.get(1).getUsdSettledAmount());

		assertEquals(sdf.parse("20-06-2019"), sellTrades.get(2).getSettledDate());
		assertEquals(2, sellTrades.get(2).getRank());
		assertEquals("foo", sellTrades.get(2).getEntity());
		assertEquals(10025.0, sellTrades.get(2).getUsdSettledAmount());

		assertEquals(sdf.parse("17-06-2019"), buyTrades.get(0).getSettledDate());
		assertEquals(1, buyTrades.get(0).getRank());
		assertEquals("qux", buyTrades.get(0).getEntity());
		assertEquals(15000.0, buyTrades.get(0).getUsdSettledAmount());

		assertEquals(sdf.parse("18-06-2019"), buyTrades.get(1).getSettledDate());
		assertEquals(1, buyTrades.get(1).getRank());
		assertEquals("foo", buyTrades.get(1).getEntity());
		assertEquals(20025.0, buyTrades.get(1).getUsdSettledAmount());

		assertEquals(sdf.parse("18-06-2019"), buyTrades.get(2).getSettledDate());
		assertEquals(2, buyTrades.get(2).getRank());
		assertEquals("foo", buyTrades.get(2).getEntity());
		assertEquals(10025.0, buyTrades.get(2).getUsdSettledAmount());

	}

	@Test
	public void testGenerateReport_BuySettlementOnWeekends() throws ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

		List<TradeDTO> trades = new ArrayList<TradeDTO>();

		try {
			TradeDTO trade1 = new TradeDTO("foo", 'B', 0.5, "SGP", sdf.parse("11-06-2019"), sdf.parse("22-06-2019"),
					200, 100.25);
			TradeDTO trade2 = new TradeDTO("bar", 'S', 0.22, "AED", sdf.parse("11-06-2019"), sdf.parse("14-06-2019"),
					450, 150.5);
			TradeDTO trade3 = new TradeDTO("foo", 'S', 0.5, "AED", sdf.parse("14-06-2019"), sdf.parse("15-06-2019"),
					200, 100.25);
			TradeDTO trade4 = new TradeDTO("baz", 'S', 0.5, "SGP", sdf.parse("17-06-2019"), sdf.parse("22-06-2019"),
					200, 200.25);
			TradeDTO trade5 = new TradeDTO("qux", 'B', 0.5, "AED", sdf.parse("14-06-2019"), sdf.parse("21-06-2019"),
					300, 100.00);
			TradeDTO trade6 = new TradeDTO("foo", 'B', 0.5, "INR", sdf.parse("17-06-2019"), sdf.parse("22-06-2019"),
					200, 200.25);

			trades.add(trade1);
			trades.add(trade2);
			trades.add(trade3);
			trades.add(trade4);
			trades.add(trade5);
			trades.add(trade6);

		} catch (ParseException e) {
			e.printStackTrace();
		}

		ReportResponseDTO response = reportEngineService.generateReport(trades);

		assertNotNull(response);
		assertNotNull(response.getSettlementBuyMap());
		assertNotNull(response.getSettlementSellMap());
		assertNotNull(response.getBuyTrades());
		assertNotNull(response.getSellTrades());
		assertEquals(2, response.getSettlementSellMap().entrySet().size());
		assertEquals(2, response.getSettlementBuyMap().entrySet().size());
		assertEquals(3, response.getBuyTrades().size());
		assertEquals(3, response.getSellTrades().size());
		Map<Date, Double> settlementBuyMap = response.getSettlementBuyMap();

		Map<Date, Double> settlementSellMap = response.getSettlementSellMap();

		assertEquals(24924.5, settlementSellMap.get(sdf.parse("16-06-2019")));
		assertEquals(20025.0, settlementSellMap.get(sdf.parse("24-06-2019")));

		assertEquals(15000.0, settlementBuyMap.get(sdf.parse("23-06-2019")));
		assertEquals(30050.0, settlementBuyMap.get(sdf.parse("24-06-2019")));

		List<TradeDTO> buyTrades = response.getBuyTrades();
		List<TradeDTO> sellTrades = response.getSellTrades();

		assertEquals(sdf.parse("16-06-2019"), sellTrades.get(0).getSettledDate());
		assertEquals(1, sellTrades.get(0).getRank());
		assertEquals("bar", sellTrades.get(0).getEntity());
		assertEquals(14899.5, sellTrades.get(0).getUsdSettledAmount());

		assertEquals(sdf.parse("16-06-2019"), sellTrades.get(1).getSettledDate());
		assertEquals(2, sellTrades.get(1).getRank());
		assertEquals("foo", sellTrades.get(1).getEntity());
		assertEquals(10025.0, sellTrades.get(1).getUsdSettledAmount());

		assertEquals(sdf.parse("24-06-2019"), sellTrades.get(2).getSettledDate());
		assertEquals(1, sellTrades.get(2).getRank());
		assertEquals("baz", sellTrades.get(2).getEntity());
		assertEquals(20025.0, sellTrades.get(2).getUsdSettledAmount());

		assertEquals(sdf.parse("23-06-2019"), buyTrades.get(0).getSettledDate());
		assertEquals(1, buyTrades.get(0).getRank());
		assertEquals("qux", buyTrades.get(0).getEntity());
		assertEquals(15000.0, buyTrades.get(0).getUsdSettledAmount());

		assertEquals(sdf.parse("24-06-2019"), buyTrades.get(1).getSettledDate());
		assertEquals(1, buyTrades.get(1).getRank());
		assertEquals("foo", buyTrades.get(1).getEntity());
		assertEquals(20025.0, buyTrades.get(1).getUsdSettledAmount());

		assertEquals(sdf.parse("24-06-2019"), buyTrades.get(2).getSettledDate());
		assertEquals(2, buyTrades.get(2).getRank());
		assertEquals("foo", buyTrades.get(2).getEntity());
		assertEquals(10025.0, buyTrades.get(2).getUsdSettledAmount());

	}

	@Test
	public void testGenerateReport_OnlyBuySettlementOnWorkingDays() throws ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

		List<TradeDTO> trades = new ArrayList<TradeDTO>();

		try {
			TradeDTO trade1 = new TradeDTO("foo", 'B', 0.5, "SGP", sdf.parse("17-06-2019"), sdf.parse("18-06-2019"),
					200, 100.25);
			TradeDTO trade5 = new TradeDTO("qux", 'B', 0.5, "SAR", sdf.parse("14-06-2019"), sdf.parse("17-06-2019"),
					300, 100.00);
			TradeDTO trade6 = new TradeDTO("foo", 'B', 0.5, "INR", sdf.parse("17-06-2019"), sdf.parse("18-06-2019"),
					200, 200.25);

			trades.add(trade1);
			trades.add(trade5);
			trades.add(trade6);

		} catch (ParseException e) {
			e.printStackTrace();
		}

		ReportResponseDTO response = reportEngineService.generateReport(trades);

		assertNotNull(response);
		assertNotNull(response.getSettlementBuyMap());
		assertNotNull(response.getSettlementSellMap());
		assertNotNull(response.getBuyTrades());
		assertNotNull(response.getSellTrades());
		assertEquals(0, response.getSettlementSellMap().entrySet().size());
		assertEquals(2, response.getSettlementBuyMap().entrySet().size());
		assertEquals(3, response.getBuyTrades().size());
		assertEquals(0, response.getSellTrades().size());
		Map<Date, Double> settlementBuyMap = response.getSettlementBuyMap();

		assertEquals(15000.0, settlementBuyMap.get(sdf.parse("17-06-2019")));
		assertEquals(30050.0, settlementBuyMap.get(sdf.parse("18-06-2019")));

		List<TradeDTO> buyTrades = response.getBuyTrades();

		assertEquals(sdf.parse("17-06-2019"), buyTrades.get(0).getSettledDate());
		assertEquals(1, buyTrades.get(0).getRank());
		assertEquals("qux", buyTrades.get(0).getEntity());
		assertEquals(15000.0, buyTrades.get(0).getUsdSettledAmount());

		assertEquals(sdf.parse("18-06-2019"), buyTrades.get(1).getSettledDate());
		assertEquals(1, buyTrades.get(1).getRank());
		assertEquals("foo", buyTrades.get(1).getEntity());
		assertEquals(20025.0, buyTrades.get(1).getUsdSettledAmount());

		assertEquals(sdf.parse("18-06-2019"), buyTrades.get(2).getSettledDate());
		assertEquals(2, buyTrades.get(2).getRank());
		assertEquals("foo", buyTrades.get(2).getEntity());
		assertEquals(10025.0, buyTrades.get(2).getUsdSettledAmount());

	}

	@Test
	public void testGenerateReport_OnlySellSettlementOnWorkingDays() throws ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

		List<TradeDTO> trades = new ArrayList<TradeDTO>();

		try {

			TradeDTO trade2 = new TradeDTO("bar", 'S', 0.22, "AED", sdf.parse("18-06-2019"), sdf.parse("20-06-2019"),
					450, 150.5);
			TradeDTO trade3 = new TradeDTO("foo", 'S', 0.5, "AED", sdf.parse("17-06-2019"), sdf.parse("20-06-2019"),
					200, 100.25);
			TradeDTO trade4 = new TradeDTO("baz", 'S', 0.5, "SGP", sdf.parse("17-06-2019"), sdf.parse("19-06-2019"),
					200, 200.25);

			trades.add(trade2);
			trades.add(trade3);
			trades.add(trade4);

		} catch (ParseException e) {
			e.printStackTrace();
		}

		ReportResponseDTO response = reportEngineService.generateReport(trades);

		assertNotNull(response);
		assertNotNull(response.getSettlementBuyMap());
		assertNotNull(response.getSettlementSellMap());
		assertNotNull(response.getBuyTrades());
		assertNotNull(response.getSellTrades());
		assertEquals(2, response.getSettlementSellMap().entrySet().size());
		assertEquals(0, response.getSettlementBuyMap().entrySet().size());
		assertEquals(0, response.getBuyTrades().size());
		assertEquals(3, response.getSellTrades().size());

		Map<Date, Double> settlementSellMap = response.getSettlementSellMap();

		assertEquals(20025.0, settlementSellMap.get(sdf.parse("19-06-2019")));
		assertEquals(24924.5, settlementSellMap.get(sdf.parse("20-06-2019")));

		List<TradeDTO> sellTrades = response.getSellTrades();

		assertEquals(sdf.parse("19-06-2019"), sellTrades.get(0).getSettledDate());
		assertEquals(1, sellTrades.get(0).getRank());
		assertEquals("baz", sellTrades.get(0).getEntity());
		assertEquals(20025.0, sellTrades.get(0).getUsdSettledAmount());

		assertEquals(sdf.parse("20-06-2019"), sellTrades.get(1).getSettledDate());
		assertEquals(1, sellTrades.get(1).getRank());
		assertEquals("bar", sellTrades.get(1).getEntity());
		assertEquals(14899.5, sellTrades.get(1).getUsdSettledAmount());

		assertEquals(sdf.parse("20-06-2019"), sellTrades.get(2).getSettledDate());
		assertEquals(2, sellTrades.get(2).getRank());
		assertEquals("foo", sellTrades.get(2).getEntity());
		assertEquals(10025.0, sellTrades.get(2).getUsdSettledAmount());

	}
}
