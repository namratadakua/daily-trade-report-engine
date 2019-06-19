package dailytradereportingengine;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import dailytradereportingengine.dto.Trade;
import dailytradereportingengine.services.ReportEngineService;

public class DailyTradeReportingEngine {

	
	public static void main(String[] args) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		
		List<Trade> trades = new ArrayList<Trade>();
		
		try {
			Trade trade1 = new Trade("foo", 'B', 0.5, "SGP", sdf.parse("01-01-2016"),sdf.parse("02-01-2016"), 200, 100.25);
			Trade trade2 = new Trade("bar", 'S', 0.22, "AED", sdf.parse("05-01-2016"),sdf.parse("07-01-2016"), 450, 150.5);
			Trade trade3 = new Trade("foo", 'S', 0.5, "AED", sdf.parse("01-01-2016"),sdf.parse("08-01-2016"), 200, 100.25);
			Trade trade4 = new Trade("xyz", 'S', 0.5, "SGP", sdf.parse("01-01-2016"),sdf.parse("05-01-2016"), 200, 200.25);
			Trade trade5 = new Trade("xyz", 'B', 0.5, "SAR", sdf.parse("07-01-2016"),sdf.parse("02-01-2016"), 300, 100.00);
			
			trades.add(trade1);
			trades.add(trade2);
			trades.add(trade3);
			trades.add(trade4);
			trades.add(trade5);
			ReportEngineService service = new ReportEngineService();
			service.generateReport(trades);
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
