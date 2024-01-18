package metro;

import exceptions.ChangeLineException;
import exceptions.ImpossibleBuildRoute;
import exceptions.NameStationException;
import exceptions.StartEqualsFinishException;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.*;

public class Station {
    private final String name;
    private final Metro metro;
    private final Line line;
    private final TicketOffice ticketOffice = new TicketOffice();
    private final Set<Station> changeLines = new HashSet<>();
    private Station stationBefore;
    private Station stationNext;
    private Duration timeTransferToNextStation;

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
        for (Station station : changeLines) {
            result = String.join(delimiter, station.getLine().getColor().getStringColor());
        }
        return result;
    }

    public BigDecimal sellTicket(Calendar date, String start, String finish) throws NameStationException,
            StartEqualsFinishException, ChangeLineException {
        int sumRuns = metro.sumRuns(start, finish);
        return ticketOffice.getPrice(date, sumRuns);
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

    public Station findStationChangeLine(Color finishColor) {
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

    public TicketOffice getTicketOffice() {
        return ticketOffice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Station station = (Station) o;
        return this.name.equals(((Station) o).name) || this.changeLines.contains(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "metro.Station{"
                + "name='" + name + '\''
                + ", changeLines=" + getStringColorChangeLines()
                + '}';
    }

}
