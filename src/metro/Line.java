package metro;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TransferQueue;

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

    public List<TreeMap<LocalDate, BigDecimal>> getReport() {
        List<TreeMap<LocalDate, BigDecimal>> report = new ArrayList<>();
        for (Station station : stations) {
            report.add(station.getReport());
        }
        return report;
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
