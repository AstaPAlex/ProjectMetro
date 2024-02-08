package metro;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Metro {
    private final String city;
    private final HashSet<Line> lines = new HashSet<>();
    private final HashMap<String, LocalDate> seasonTickets = new HashMap<>();
    private int idSeasonTicket;

    public Metro(String nameCity) {
        this.city = nameCity;
    }

    public void createLine(Color color) {
        checkHaveNotLine(color);
        lines.add(new Line(color, this));
    }


    public Station createFirstStation(Color color, String name) {
        checkHaveLine(color);
        checkHaveNotStation(name);
        if (isEmptyLine(color)) {
            Station station = new Station(name, getLine(color), this);
            getLine(color).getStations().add(station);
            return station;
        }
        throw new RuntimeException("У линии уже есть первая станция");
    }

    public Station createFinallyStation(Color color, String name, Duration time, String... changeStations) {
        checkHaveLine(color);
        checkHaveNotStation(name);
        checkHaveLastStation(color);
        checkLastStationHaveNotNextStation(color);
        checkTimeIsNotZero(time);
        Station newStation = new Station(name, getLine(color).getLastStation(), getLine(color), this);
        newStation.addStationsChange(changeStations);
        getLine(color).getLastStation().setStationNext(newStation);
        getLine(color).getLastStation().setTimeTransferToNextStation(time);
        getLine(color).getStations().add(newStation);
        return newStation;
    }

    public int countRuns(String start, String finish) {
        Station stationStart = getStation(start);
        Station stationFinish = getStation(finish);
        if (stationStart.equals(stationFinish)) {
            throw new RuntimeException("Станция старта == станции финиша!");
        }
        return countRunsDifferentLine(stationStart, stationFinish);
    }

    public void addSeasonTicket(LocalDate date) {
        if (idSeasonTicket < 0 || idSeasonTicket > 9999) {
            throw new RuntimeException("Вышли за пределы доступимого номера абонемента!");
        }
        String id = "a" + "0".repeat(4 - String.valueOf(idSeasonTicket).length()) + idSeasonTicket;
        seasonTickets.put(id, date.plusMonths(1));
        idSeasonTicket++;
    }

    public boolean checkValidation(String id, LocalDate date) {
        checkHaveSeasonTicket(id);
        return seasonTickets.get(id).isAfter(date);
    }

    public void extendSeasonTicket(String id, LocalDate date) {
        checkHaveSeasonTicket(id);
        seasonTickets.put(id, date.plusMonths(1));
    }

    private void checkHaveSeasonTicket(String id) {
        if (!seasonTickets.containsKey(id)) {
            throw new RuntimeException("Абономент с таким номером не найден!");
        }
    }

    public void printReport() {
        String text = createMainReport().entrySet().stream()
                .map(report -> report.getKey() + " - " + report.getValue())
                .collect(Collectors.joining("\n"));
        System.out.println(text);
    }

    public TreeMap<LocalDate, BigDecimal> createMainReport() {
        return lines.stream()
                .flatMap(line -> line.getReports().stream())
                .flatMap(tree -> tree.entrySet().stream())
                .collect(
                        Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                BigDecimal::add,
                                TreeMap::new
                        ));
    }

    private int countRunsDifferentLine(Station start, Station finish) {
        if (!start.getLine().getColor().equals(finish.getLine().getColor())) {
            Color startColor = start.getLine().getColor();
            Color finishColor = finish.getLine().getColor();
            Station stationChange = findStationChange(startColor, finishColor);
            int countRunsStartLine = countRunsOnOneLine(start, stationChange);
            int countRunsFinishLine = countRunsOnOneLine(findStationChange(finishColor, startColor), finish);
            countRunsStartLine = (countRunsStartLine == -1) ? 0 : countRunsStartLine;
            countRunsFinishLine = (countRunsFinishLine == -1) ? 0 : countRunsFinishLine;
            return countRunsStartLine + countRunsFinishLine;
        }
        return countRunsOnOneLine(start, finish);
    }
    
    private int countRunsOnOneLine(Station start, Station finish) {
        int countRuns = countSumRunsOnNext(start, finish);
        if (countRuns == -1) {
            return countRunsOnBefore(start, finish);
        }
        return countRuns;
    }

    private int countSumRunsOnNext(Station start, Station finish) {
        Station nextStation = start.getStationNext();
        if (nextStation == null) {
            return -1;
        }
        if (nextStation.equals(finish)) {
            return 1;
        }
        int countRuns = countSumRunsOnNext(nextStation, finish);
        return (countRuns == -1 ? -1 : 1 + countRuns);
    }

    private void checkHaveNotStation(String name) {
        lines.stream()
                .flatMap(line -> line.getStations().stream())
                .map(Station::getName)
                .filter(name::equals)
                .findFirst()
                .ifPresent(e -> {
                    throw new RuntimeException("Такая станция уже существует!");
                });
    }

    private int countRunsOnBefore(Station finish, Station start) {
        Station beforeStation = finish.getStationBefore();
        if (beforeStation == null) {
            return -1;
        }
        if (beforeStation.equals(start)) {
            return 1;
        }
        int count = countRunsOnBefore(beforeStation, start);
        return (count == -1 ? -1 : 1 + count);
    }

    private Station findStationChange(Color startColor, Color finishColor) {
        if (startColor.equals(finishColor)) {
            throw new RuntimeException("Пересадка невозможна!");
        }
        return getLine(startColor).getStations().stream()
                .filter(station -> station.checkStationChangeLine(finishColor))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Пересадка невозможна!"));
    }

    public Station getStation(String name) {
        return lines.stream()
                .flatMap(line -> line.getStations().stream())
                .filter(station -> name.equals(station.getName()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Такой станции не существует!"));
    }

    private void checkHaveNotLine(Color color) {
        lines.stream()
                .filter(line -> line.getColor().equals(color))
                .findFirst()
                .ifPresentOrElse(e -> {
                    throw new RuntimeException("Такая линия уже существует");
                }, () -> {});
    }

    private void checkHaveLine(Color color) {
        getLine(color);
    }

    private boolean isEmptyLine(Color color) {
        return getLine(color).getStations().isEmpty();
    }

    private void checkHaveLastStation(Color color) {
        if (getLine(color).getLastStation() == null) {
            throw new RuntimeException("Отсутствует последняя станция!");
        }
    }

    private  void checkTimeIsNotZero(Duration time) {
        if (time.isZero()) {
            throw new RuntimeException("Время перегона не может быть нулевым!");
        }
    }

    private void checkLastStationHaveNotNextStation(Color color) {
        if (getLine(color).getLastStation().getStationNext() != null) {
            throw new RuntimeException("Следующая станция уже существует");
        }
    }

    private Line getLine(Color color) {
        return lines.stream()
                .filter(line -> line.getColor().equals(color))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Такой линии не существует!"));
    }

    @Override
    public String toString() {
        return "metro.Metro{"
                + "city='" + city + '\''
                + ", lines=" + lines
                + '}';
    }
}
