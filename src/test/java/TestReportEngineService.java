import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Test;

import dailytradereportingengine.dto.ReportResponse;
import dailytradereportingengine.dto.Trade;
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

		List<Trade> trades = new ArrayList<>();

		ReportResponse response = reportEngineService.generateReport(trades);

		assertNull(response);
	}

	@Test
	public void testGenerateReport_BuySettlementOnWorkingDays() throws ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

		List<Trade> trades = new ArrayList<Trade>();

		try {
			Trade trade1 = new Trade("foo", 'B', 0.5, "SGP", sdf.parse("17-06-2019"), sdf.parse("18-06-2019"), 200,
					100.25);
			Trade trade2 = new Trade("bar", 'S', 0.22, "AED", sdf.parse("18-06-2019"), sdf.parse("20-06-2019"), 450,
					150.5);
			Trade trade3 = new Trade("foo", 'S', 0.5, "AED", sdf.parse("17-06-2019"), sdf.parse("20-06-2019"), 200,
					100.25);
			Trade trade4 = new Trade("baz", 'S', 0.5, "SGP", sdf.parse("17-06-2019"), sdf.parse("19-06-2019"), 200,
					200.25);
			Trade trade5 = new Trade("qux", 'B', 0.5, "SAR", sdf.parse("14-06-2019"), sdf.parse("17-06-2016"), 300,
					100.00);
			Trade trade6 = new Trade("foo", 'B', 0.5, "INR", sdf.parse("17-06-2019"), sdf.parse("18-06-2019"), 200,
					100.25);

			trades.add(trade1);
			trades.add(trade2);
			trades.add(trade3);
			trades.add(trade4);
			trades.add(trade5);
			trades.add(trade6);

		} catch (ParseException e) {
			e.printStackTrace();
		}

		ReportResponse response = reportEngineService.generateReport(trades);

		assertNotNull(response);
		assertNotNull(response.getSettlementBuyMap());
		assertNotNull(response.getSettlementSellMap());
		assertNotNull(response.getBuyTrades());
		assertNotNull(response.getSellTrades());
		assertEquals(response.getSettlementSellMap().entrySet().size(), 2);
		assertEquals(response.getSettlementBuyMap().entrySet().size(), 2);
		assertEquals(response.getBuyTrades().size(), 3);
		assertEquals(response.getSellTrades().size(), 3);
		
		int index = 0;
		for(Entry<Date, Double> trade : response.getSettlementSellMap().entrySet()) {
			if(index == 0) {
				assertEquals(trade.getKey(), sdf.parse("19-06-2019"));
				assertEquals(trade.getValue(), 20025.0);
			}
			if(index == 1) {
				assertEquals(trade.getKey(), sdf.parse("20-06-2019"));
				assertEquals(trade.getValue(), 24924.5);
			}
			index++;
		}
		
		index=0;
		for(Entry<Date, Double> trade : response.getSettlementBuyMap().entrySet()) {
			if(index == 0) {
				assertEquals(trade.getKey(), sdf.parse("17-06-2019"));
				assertEquals(trade.getValue(), 15000.0);
			}
			if(index == 1) {
				assertEquals(trade.getKey(), sdf.parse("18-06-2019"));
				assertEquals(trade.getValue(), 20050.0);
			}
			index++;
		}
		
	}
}
