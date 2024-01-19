package metro;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class TicketOffice {
    private final HashMap<Calendar, BigDecimal> salesReport;
    private final String name;
    public TicketOffice(String name) {
        this.name = name;
        salesReport = new HashMap<>();

    }

    public BigDecimal getPrice(Calendar date, int sumRuns) {
        if (!salesReport.containsKey(date)) {
            salesReport.put(date, new BigDecimal("0"));
        }
        BigDecimal priceTicket = BigDecimal.valueOf(20 + 5L * sumRuns);
        BigDecimal newPriceTickets = salesReport.get(date).add(priceTicket);
        salesReport.put(date, newPriceTickets);
        return priceTicket;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("Касса: " + name + "\n");
        for (Calendar data : salesReport.keySet()) {
            result.append("Дата: ").append(new SimpleDateFormat("dd MMM yyy").format(data.getTime()));
            result.append(", продажи: ").append(salesReport.get(data)).append("\n");
        }
        return result.toString();
    }
}
