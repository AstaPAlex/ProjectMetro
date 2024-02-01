package metro;

import exceptions.*;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class Station {
    private static final long PRICE_SEASON_TICKET = 3000;
    private static final long PRICE_ONE_TRANSFER = 5;
    private static final long PRICE_TICKET = 20;
    private final String name;
    private final Metro metro;
    private final Line line;
    private final TicketOffice ticketOffice;
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
        this.ticketOffice = new TicketOffice();
    }

    public Station(String name, Line lineMetro, Metro metro) {
        this.name = name;
        this.line = lineMetro;
        this.metro = metro;
        this.ticketOffice = new TicketOffice();
    }

    private String getStringColorChangeLines() {
        String result = null;
        String delimiter = ",";
        for (Station station : changeLines) {
            result = String.join(delimiter, station.getLine().getColor().getStringColor());
        }
        return result;
    }

    public void sellTicket(LocalDateTime dateTime, String start, String finish) throws StartEqualsFinishException,
            ChangeLineException, LineNotFoundException, NotFoundStationException {
        int sumRuns = metro.sumRuns(start, finish);
        LocalDate date = dateTime.toLocalDate();
        ticketOffice.sellTicket(date, sumRuns * PRICE_ONE_TRANSFER + PRICE_TICKET);
    }

    public void sellSeasonTicket(LocalDate date) throws NumberTicketIndexOutOfBoundsException {
        getTicketOffice().sellTicket(date, PRICE_SEASON_TICKET);
        metro.addSeasonTicket(date);
    }

    public void extendSeasonTicket(String id, LocalDate date) throws NotFoundTicket {
        metro.extendSeasonTicket(id, date);
        getTicketOffice().sellTicket(date, PRICE_SEASON_TICKET);
    }

    public TreeMap<LocalDate, BigDecimal> getReport() {
        return ticketOffice.getSalesReport();
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
