package metro;

import java.util.ArrayList;
import java.util.List;

public class Line {
    private final Color color;
    private final List<Station> stations = new ArrayList<>();
    private final Metro metro;

    public Line(Color color, Metro metro) {
        this.color = color;
        this.metro = metro;
    }

    public Color getColor() {
        return color;
    }

    public List<Station> getStations() {
        return stations;
    }

    public Station getLastStation() {
        return stations.get(stations.size() - 1);
    }

    public Station getStationName(String name) {
        for (Station station : stations) {
            if (station.getName().equalsIgnoreCase(name)) {
                return station;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "metro.Line{"
                + "color='" + color.getStringColor()
                + "', stations=" + stations
                + '}';
    }
}
