package metro;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.TreeMap;

public class TicketOffice {
    private final TreeMap<LocalDate, BigDecimal> salesReport = new TreeMap<>();

    public void sellTicket(LocalDate date, long price) {
        if (!salesReport.containsKey(date)) {
            salesReport.put(date, BigDecimal.ZERO);
        }
        salesReport.put(
                date,
                salesReport.get(date).add(BigDecimal.valueOf(price)));
    }

    public TreeMap<LocalDate, BigDecimal> getReport() {
        return salesReport;
    }

    @Override
    public String toString() {
        return "TicketOffice{"
                + "salesReport=" + salesReport
                + '}';
    }
}
