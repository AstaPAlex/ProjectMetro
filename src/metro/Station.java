package metro;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class Station {
    private static final long PRICE_SEASON_TICKET = 3000;
    private static final long PRICE_ONE_TRANSFER = 5;
    private static final long PRICE_TICKET = 20;
    private final String name;
    private final Metro metro;
    private final Line line;
    private final TicketOffice ticketOffice = new TicketOffice();
    private final Set<Station> stationsChange = new HashSet<>();
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

    public void sellTicket(LocalDateTime dateTime, String startStation, String finishStation) {
        int countRuns = metro.countRuns(startStation, finishStation);
        LocalDate date = dateTime.toLocalDate();
        ticketOffice.sellTicket(date, countRuns * PRICE_ONE_TRANSFER + PRICE_TICKET);
    }

    public void sellSeasonTicket(LocalDate date) {
        ticketOffice.sellTicket(date, PRICE_SEASON_TICKET);
        metro.addSeasonTicket(date);
    }

    public void extendSeasonTicket(String id, LocalDate date) {
        metro.extendSeasonTicket(id, date);
        ticketOffice.sellTicket(date, PRICE_SEASON_TICKET);
    }

    public TreeMap<LocalDate, BigDecimal> getReport() {
        return ticketOffice.getReport();
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
        stationsChange.add(station);
    }

    public void setStationNext(Station stationNext) {
        this.stationNext = stationNext;
    }

    public boolean checkStationChangeLine(Color finishColor) {
        return stationsChange.stream()
                .anyMatch(station -> finishColor.equals(station.line.getColor()));
    }

    public Station getStationBefore() {
        return stationBefore;
    }

    public void addStationsChange(String[] changeStations) {
        if (changeStations.length > 0) {
            Arrays.stream(changeStations)
                    .map(metro::getStation)
                    .filter(stationsChange -> !stationsChange.getLine().equals(line))
                    .findFirst()
                    .ifPresentOrElse((this::addStationChange), () -> {
                        throw new RuntimeException("Станция пересадки находится на той же линии!");
                    });
        }
    }

    private void addStationChange(Station station) {
        this.addChangeStation(station);
        station.addChangeStation(this);
    }

    private String getStringColorChangeLines() {
        return stationsChange.stream()
                .map(station -> station.getLine().getColor().getStringColor())
                .collect(Collectors.joining(", "));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        return this.name.equals(((Station) o).name) || this.stationsChange.contains(o);
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
