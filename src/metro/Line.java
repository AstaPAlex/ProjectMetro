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
        return stations.stream()
                .reduce((a, b) -> b)
                .orElseThrow(() -> new RuntimeException("Нет последней станции"));
    }

    public List<TreeMap<LocalDate, BigDecimal>> getReports() {
        return stations.stream()
                .map(Station::getReport)
                .toList();
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
