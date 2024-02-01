package metro;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

public class Line {
    private final Color color;
    private final LinkedHashSet<Station> stations = new LinkedHashSet<>();
    private final Metro metro;

    public Line(Color color, Metro metro) {
        this.color = color;
        this.metro = metro;
    }

    public Color getColor() {
        return color;
    }

    public LinkedHashSet<Station> getStations() {
        return stations;
    }

    public Station getLastStation() {
        Iterator<Station> iterator = stations.iterator();
        Station lastStation = null;
        while (iterator.hasNext()) {
            lastStation = iterator.next();
        }
        return lastStation;
    }

    public Station getStationName(String name) {
        for (Station station : stations) {
            if (station.getName().equalsIgnoreCase(name)) {
                return station;
            }
        }
        return null;
    }

    public TreeMap<LocalDate, BigDecimal> getReport() {
        TreeMap<LocalDate, BigDecimal> report = new TreeMap<>();
        for (Station station : stations) {
            addReport(report, station);
        }
        return report;
    }

    private void addReport(TreeMap<LocalDate, BigDecimal> report, Station station) {
        for (LocalDate date : station.getReport().keySet()) {
            if (report.containsKey(date)) {
                BigDecimal price1 = report.get(date);
                BigDecimal price2 = station.getReport().get(date);
                BigDecimal newPrice = new BigDecimal(String.valueOf(price1.add(price2)));
                report.put(date, newPrice);
            } else {
                report.put(date, station.getReport().get(date));
            }
        }
    }

    @Override
    public String toString() {
        return "metro.Line{"
                + "color='" + color.getStringColor()
                + "', stations=" + stations
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Line line = (Line) o;
        return color == line.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color);
    }
}
