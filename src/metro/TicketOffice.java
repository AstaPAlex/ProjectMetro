package metro;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.TreeMap;

public class TicketOffice {
    private final TreeMap<LocalDate, BigDecimal> salesReport = new TreeMap<>();

    public void sellTicket(LocalDate date, long price) {
        if (!salesReport.containsKey(date)) {
            salesReport.put(date, new BigDecimal("0"));
        }
        BigDecimal priceTicket = BigDecimal.valueOf(price);
        BigDecimal newPriceTickets = salesReport.get(date).add(priceTicket);
        salesReport.put(date, newPriceTickets);
    }

    public TreeMap<LocalDate, BigDecimal> getSalesReport() {
        return salesReport;
    }

    @Override
    public String toString() {
        return "TicketOffice{"
                + "salesReport=" + salesReport
                + '}';
    }
}
