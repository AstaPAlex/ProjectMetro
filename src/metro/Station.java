package metro;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

public class Station {
    private final String name;
    private final Metro metro;
    private final Line line;
    private Station stationBefore;
    private Station stationNext;
    private Duration timeTransferToNextStation;
    private Set<Station> changeLines = new HashSet<>();


    public Station(String name, Station stationBefore,
                   Line lineMetro, Metro metro) {
        this.name = name;
        this.stationBefore = stationBefore;
        this.line = lineMetro;
        this.metro = metro;
    }

    public Station(String name, Line lineMetro, Metro metro) {
        this.name = name;
        this.line = lineMetro;
        this.metro = metro;
    }

    private String getStringColorChangeLines() {
        String result = null;
        String delimiter = ",";
        if (changeLines != null) {
            for (Station station : changeLines) {
                result = String.join(delimiter, station.getLine().getColor().getStringColor());
            }
        }
        return result;
    }

    public Line getLine() {
        return line;
    }

    public void setTimeTransferToNextStation(Duration timeTransferToNextStation) {
        this.timeTransferToNextStation = timeTransferToNextStation;
    }

    public String getName() {
        return name;
    }

    public Station getStationNext() {
        return stationNext;
    }

    public void addChangeStation(Station station) {
        changeLines.add(station);
    }

    public void setStationNext(Station stationNext) {
        this.stationNext = stationNext;
    }

    public Station checkChangeLine(Color finishColor) {
        for (Station station : changeLines) {
            if (finishColor.equals(station.line.getColor())) {
                return station;
            }
        }
        return null;
    }

    public Station getStationBefore() {
        return stationBefore;
    }

    @Override
    public String toString() {
        return "metro.Station{"
                + "name='" + name + '\''
                + ", changeLines=" + getStringColorChangeLines()
                + '}';
    }

}
