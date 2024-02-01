package metro;

import exceptions.*;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.*;

public class Metro {
    private static int idSeasonTicket;
    private final String city;
    private final HashSet<Line> lines = new HashSet<>();
    private final HashMap<String, LocalDate> seasonTickets = new HashMap<>();

    public Metro(String nameCity) {
        this.city = nameCity;
    }

    public void createLine(Color color) throws ImpossibleCreateLineException {
        if (!checkHaveLine(color)) {
            lines.add(new Line(color, this));
            return;
        }
        throw new ImpossibleCreateLineException("Линия с таким цветом уже существует");
    }

    public Station createFirstStation(Color color, String name) throws ImpossibleCreateStationException,
            StationAlreadyExistsException, LineNotFoundException {
        if (!checkHaveLine(color)) {
            throw new LineNotFoundException("Такой линии не существует!");
        }
        if (checkHaveStation(name)) {
            throw new StationAlreadyExistsException("Станция с таким именем уже существует");
        }
        if (isEmptyLine(color)) {
            Station station = new Station(name, getLine(color), this);
            getLine(color).getStations().add(station);
            return station;
        }
        throw new ImpossibleCreateStationException("У линии уже есть первая станция");
    }

    public Station createFinallyStation(Color color, String name, Duration time,
                                     String... changeStations) throws ImpossibleCreateStationException,
            StationAlreadyExistsException, LineNotFoundException {
        if (!checkHaveLine(color)) {
            throw new LineNotFoundException("Такой линии не существует!");
        }
        if (checkHaveStation(name)) {
            throw new StationAlreadyExistsException("Станция с таким именем уже существует");
        }
        if (!checkHaveLastStation(color)) {
            throw new NotFoundStationException("Отсутствует последняя станция!");
        }
        if (checkLastStationHaveNextStation(color)) {
            throw new StationAlreadyExistsException("Следующая станция уже существует");
        }
        if (time.isZero()) {
            throw new TimeIsZeroException();
        }
        Station newStation = new Station(name, getLine(color).getLastStation(), getLine(color), this);
        addStationsChange(changeStations, newStation);
        getLine(color).getLastStation().setStationNext(newStation);
        getLine(color).getLastStation().setTimeTransferToNextStation(time);
        getLine(color).getStations().add(newStation);
        return newStation;
    }

    public int sumRuns(String start, String finish) throws ChangeLineException,
            StartEqualsFinishException, LineNotFoundException, NotFoundStationException {
        Station stationStart = getStation(start);
        Station stationFinish = getStation(finish);
        if (stationStart.equals(stationFinish)) {
            throw new StartEqualsFinishException();
        }
        return sumRunsDifferentLine(stationStart, stationFinish);
    }

    public void addSeasonTicket(LocalDate date) throws NumberTicketIndexOutOfBoundsException {
        if (idSeasonTicket < 0 || idSeasonTicket > 9999) {
            throw new NumberTicketIndexOutOfBoundsException("Вышли за пределы доступимого номера абонемента!");
        }
        String id = "a" + "0".repeat(4 - String.valueOf(idSeasonTicket).length()) + idSeasonTicket;
        seasonTickets.put(id, date.plusMonths(1));
        idSeasonTicket++;
    }

    public boolean checkValidation(String id, LocalDate date) throws NotFoundTicket {
        if (seasonTickets.containsKey(id)) {
            return seasonTickets.get(id).isAfter(date);
        }
        throw new NotFoundTicket("Абономент с таким номером не найден!");
    }

    public void extendSeasonTicket(String id, LocalDate date) throws NotFoundTicket {
        if (!seasonTickets.containsKey(id)) {
            throw new NotFoundTicket("Абономент с таким номером не найден!");
        }
        seasonTickets.put(id, date.plusMonths(1));
    }

    public void printReport() {
        TreeMap<LocalDate, BigDecimal> mainReport = createMainReport();
        StringBuilder result = new StringBuilder();
        for (LocalDate date : mainReport.keySet()) {
            result.append(date).append(" - ").append(mainReport.get(date)).append("\n");
        }
        System.out.println(result);
    }

    private TreeMap<LocalDate, BigDecimal> createMainReport() {
        TreeMap<LocalDate, BigDecimal> mainReport = new TreeMap<>();
        for (Line line : lines) {
            List<TreeMap<LocalDate, BigDecimal>> reportLine = line.getReport();
            for (TreeMap<LocalDate, BigDecimal> reportStation : reportLine) {
                addReportStation(mainReport, reportStation);
            }

        }
        return mainReport;
    }

    private void addReportStation(TreeMap<LocalDate, BigDecimal> mainReport,
                                  TreeMap<LocalDate, BigDecimal> reportStation) {
        for (LocalDate date : reportStation.keySet()) {
            if (mainReport.containsKey(date)) {
                BigDecimal price1 = reportStation.get(date);
                BigDecimal price2 = mainReport.get(date);
                BigDecimal newPrice = new BigDecimal(String.valueOf(price1.add(price2)));
                mainReport.put(date, newPrice);
            } else {
                mainReport.put(date, reportStation.get(date));
            }
        }
    }

    private int sumRunsDifferentLine(Station start, Station finish) throws ChangeLineException,
            LineNotFoundException {
        if (!start.getLine().getColor().equals(finish.getLine().getColor())) {
            Color startColor = start.getLine().getColor();
            Color finishColor = finish.getLine().getColor();
            Station stationChange = findStationChange(startColor, finishColor);
            int sum1 = sumRunsOnOneLine(start, stationChange);
            int sum2 = sumRunsOnOneLine(findStationChange(finishColor, startColor), finish);
            sum1 = (sum1 == -1) ? 0 : sum1;
            sum2 = (sum2 == -1) ? 0 : sum2;
            return sum1 + sum2;
        }
        return sumRunsOnOneLine(start, finish);
    }
    
    private int sumRunsOnOneLine(Station start, Station finish) {
        int sum = countSumRunsOnNext(start, finish);
        if (sum == -1) {
            return countSumRunsOnBefore(start, finish);
        }
        return sum;
    }

    private int countSumRunsOnNext(Station start, Station finish) {
        Station nextStation = start.getStationNext();
        if (nextStation == null) {
            return -1;
        }
        if (nextStation.equals(finish)) {
            return 1;
        }
        int sum = countSumRunsOnNext(nextStation, finish);
        return (sum == -1 ? -1 : 1 + sum);
    }

    private boolean checkHaveStation(String name) {
        try {
            getStation(name);
            return true;
        } catch (NotFoundStationException e) {
            return false;
        }
    }

    private int countSumRunsOnBefore(Station finish, Station start) {
        Station beforeStation = finish.getStationBefore();
        if (beforeStation == null) {
            return -1;
        }
        if (beforeStation.equals(start)) {
            return 1;
        }
        int sum = countSumRunsOnBefore(beforeStation, start);
        return (sum == -1 ? -1 : 1 + sum);
    }

    private Station findStationChange(Color startColor, Color finishColor) throws ChangeLineException,
            LineNotFoundException {
        if (startColor.equals(finishColor)) {
            throw new ChangeLineException();
        }
        for (Station station : getLine(startColor).getStations()) {
            if (station.findStationChangeLine(finishColor) != null) {
                return station;
            }
        }
        throw new ChangeLineException();
    }

    public Station getStation(String name) throws NotFoundStationException {
        for (Line lineMetro : lines) {
            Station station = lineMetro.getStationName(name);
            if (station != null) {
                return station;
            }
        }
        throw new NotFoundStationException("Такой станции не существует!");
    }

    private void addStationsChange(String[] changeStations, Station newStation)
            throws ChangeLineException, NotFoundStationException {
        for (String stationName : changeStations) {
            Station stationChange = getStation(stationName);
            if (!stationChange.getLine().equals(newStation.getLine())) {
                newStation.addChangeStation(getStation(stationName));
                stationChange.addChangeStation(newStation);
            } else {
                throw new ChangeLineException();
            }
        }
    }

    private boolean checkHaveLine(Color color) {
        try {
            getLine(color);
        } catch (LineNotFoundException e) {
            return false;
        }
        return true;
    }

    private boolean isEmptyLine(Color color) throws LineNotFoundException {
        return getLine(color).getStations().isEmpty();
    }

    private boolean checkHaveLastStation(Color color) throws LineNotFoundException {
        return getLine(color).getLastStation() != null;
    }

    private boolean checkLastStationHaveNextStation(Color color) throws LineNotFoundException {
        return getLine(color).getLastStation().getStationNext() != null;
    }

    private Line getLine(Color color) throws LineNotFoundException {
        for (Line line : lines) {
            if (line.getColor().equals(color)) {
                return line;
            }
        }
        throw new LineNotFoundException("Такой станции не существует");
    }

    @Override
    public String toString() {
        return "metro.Metro{"
                + "city='" + city + '\''
                + ", lines=" + lines
                + '}';
    }
}
