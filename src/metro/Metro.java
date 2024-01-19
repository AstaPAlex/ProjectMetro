package metro;

import exceptions.*;
import java.time.Duration;
import java.util.HashMap;

public class Metro {
    private final String city;
    private final HashMap<Color, Line> lines = new HashMap<>();

    public Metro(String nameCity) {
        this.city = nameCity;
    }

    public void createLine(Color color) throws ImpossibleCreateLineException {
        if (!checkHaveLine(color)) {
            lines.put(color, new Line(color, this));
        } else {
            throw new LineExistsException("Такая линия уже существует");
        }
    }

    public Station createFirstStation(Color color, String name) throws ImpossibleCreateStationException,
            NameStationException {
        if (!checkHaveLine(color)) {
            throw new HaveStationException("Такой линии не существует!");
        }
        if (getStation(name) != null) {
            throw new NameStationException("Станция с таким именем уже существует");
        }
        if (isEmptyLine(color)) {
            Station station = new Station(name, lines.get(color), this);
            lines.get(color).getStations().add(station);
            return station;
        }
        return null;
    }

    public Station createFinallyStation(Color color, String name, Duration time,
                                     String... changeStations) throws ImpossibleCreateStationException,
            NameStationException {
        if (!checkHaveLine(color)) {
            throw new HaveStationException("Такой линии не существует!");
        }
        if (getStation(name) != null) {
            throw new NameStationException("Станция с таким именем уже существует");
        }
        checkHaveLastStation(color);
        checkLastStationHaveNextStation(color);
        if (time.isZero()) {
            throw new TimeIsZeroException();
        }
        Station newStation = new Station(name, lines.get(color).getLastStation(), lines.get(color), this);
        createStationsChange(changeStations, newStation);
        lines.get(color).getLastStation().setStationNext(newStation);
        lines.get(color).getLastStation().setTimeTransferToNextStation(time);
        lines.get(color).getStations().add(newStation);
        return newStation;
    }

    public int sumRuns(String start, String finish) throws ChangeLineException, NameStationException,
            StartEqualsFinishException {
        Station stationStart = getStation(start);
        Station stationFinish = getStation(finish);
        if (stationStart.equals(stationFinish)) {
            throw new StartEqualsFinishException();
        }
        return sumRunsDifferentLine(stationStart, stationFinish);
    }

    private int sumRunsDifferentLine(Station start, Station finish) throws ChangeLineException {
        if (!start.getLine().getColor().equals(finish.getLine().getColor())) {
            Color startColor = start.getLine().getColor();
            Color finishColor = finish.getLine().getColor();
            Station stationChange = findStationChange(startColor, finishColor);
            int sum1 = sumRunsOnOneLine(start, stationChange);
            int sum2 = sumRunsOnOneLine(findStationChange(finishColor, startColor), finish);
            sum1 = (sum1 == -1) ? 0 : sum1;
            sum2 = (sum2 == -1) ? 0 : sum2;
            return sum1 + sum2;
        } else {
            return sumRunsOnOneLine(start, finish);
        }
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
        } else {
            int sum = countSumRunsOnNext(nextStation, finish);
            return (sum == -1 ? -1 : 1 + sum);
        }
    }

    private int countSumRunsOnBefore(Station finish, Station start) {
        Station beforeStation = finish.getStationBefore();
        if (beforeStation == null) {
            return -1;
        }
        if (beforeStation.equals(start)) {
            return 1;
        } else {
            int sum = countSumRunsOnBefore(beforeStation, start);
            return (sum == -1 ? -1 : 1 + sum);
        }
    }

    private Station findStationChange(Color startColor, Color finishColor) throws ChangeLineException {
        if (startColor.equals(finishColor)) {
            throw new ChangeLineException();
        }
        for (Station station : lines.get(startColor).getStations()) {
            if (station.findStationChangeLine(finishColor) != null) {
                return station;
            }
        }
        throw new ChangeLineException();
    }

    private Station getStation(String name) {
        if (!lines.isEmpty()) {
            for (Line lineMetro : lines.values()) {
                if (lineMetro.getStationName(name) != null) {
                    return lineMetro.getStationName(name);
                }
            }
        }
        return null;
    }

    private void createStationsChange(String[] changeStations, Station newStation)
            throws ChangeLineException {
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
        return lines.containsKey(color);
    }

    private boolean isEmptyLine(Color color) throws HaveStationException {
        if (lines.get(color).getStations().isEmpty()) {
            return true;
        }
        throw new HaveStationException("Первая станция уже существует!");
    }

    private void checkLastStationHaveNextStation(Color color) throws NotLastStationException {
        if (lines.get(color).getLastStation().getStationNext() == null) {
            return;
        }
        throw new NotLastStationException();
    }

    private void checkHaveLastStation(Color color) throws NoHaveLastStationException {
        if (lines.get(color).getLastStation() != null) {
            return;
        }
        throw new NoHaveLastStationException();
    }

    @Override
    public String toString() {
        return "metro.Metro{"
                + "city='" + city + '\''
                + ", lines=" + lines.values()
                + '}';
    }
}
