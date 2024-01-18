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
            throw new ImpossibleCreateLineException();
        }
    }

    public void createFirstStation(Color color, String name) throws ImpossibleCreateStationException {
        checkHaveLineAndHaveNotStation(color, name);
        if (isEmptyListStation(color)) {
            lines.get(color).getStations().add(new Station(name, lines.get(color), this));
        } else {
            throw new HaveFirstStationException();
        }
    }

    public void createFinallyStation(Color color, String name, Duration time,
                                     String... changeStations) throws ImpossibleCreateStationException, ChangeLineException {
        checkHaveLineAndHaveNotStation(color, name);
        if (!checkHaveLastStation(color)) {
            throw new NoLastStationException();
        } else if (isLastStationHaveNextStation(color)) {
            throw new NotLastStationException();
        } else if (time.isZero()) {
            throw new TimeIsZeroException();
        }
        Station newStation = new Station(name, lines.get(color).getLastStation(), lines.get(color), this);
        createStationsChange(changeStations, newStation);
        lines.get(color).getLastStation().setStationNext(newStation);
        lines.get(color).getLastStation().setTimeTransferToNextStation(time);
        lines.get(color).getStations().add(newStation);
    }

    public Station findStationChange(Color startColor, Color finishColor) throws ChangeLineException {
        if (startColor.equals(finishColor)) {
            throw new ChangeLineException();
        }
        for (Station station : lines.get(startColor).getStations()) {
            if (station.checkChangeLine(finishColor) != null) {
                return station;
            }
        }
        throw new ChangeLineException();
    }

    public int sumRuns(String start, String finish) throws ImpossibleBuildRoute, ChangeLineException {
        Station stationStart = getStationsName(start);
        Station stationFinish = getStationsName(finish);
        if (stationStart == null || stationFinish == null) {
            throw new NameStationException();
        }
        if (stationStart.equals(stationFinish)) {
            throw new StartEqualsFinishException();
        }
        return sumRunsCalculate(stationStart, stationFinish);
    }

    public int sumRunsCalculate(Station start, Station finish) throws ChangeLineException {
        if (!start.getLine().getColor().equals(finish.getLine().getColor())) {
            Station stationChange = findStationChange(start.getLine().getColor(), finish.getLine().getColor());
            int sum1 = sumRunsOnOneLine(start, stationChange);
            int sum2 = sumRunsOnOneLine(stationChange.checkChangeLine(finish.getLine().getColor()), finish);
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


    private void createStationsChange(String[] changeStations, Station newStation)
            throws ChangeLineException {
        for (String stationName : changeStations) {
            Station stationChange = getStationsName(stationName);
            if (stationChange != null) {
                if (!stationChange.getLine().equals(newStation.getLine())) {
                    newStation.addChangeStation(getStationsName(stationName));
                    stationChange.addChangeStation(newStation);
                }
            } else {
                throw new ChangeLineException();
            }
        }
    }

    private boolean checkHaveLine(Color color) {
        return lines.containsKey(color);
    }

    private void checkHaveLineAndHaveNotStation(Color color, String name) throws NoLineWithColorException,
            StationHaveException {
        if (!checkHaveLine(color)) {
            throw new NoLineWithColorException();
        } else if (getStationsName(name) != null) {
            throw new StationHaveException();
        }
    }

    private boolean isEmptyListStation(Color color) {
        return lines.get(color).getStations().isEmpty();
    }

    private boolean isLastStationHaveNextStation(Color color) {
        return lines.get(color).getLastStation().getStationNext() != null;
    }

    private boolean checkHaveLastStation(Color color) {
        return lines.get(color).getLastStation() != null;
    }

    public Station getStationsName(String name) {
        if (!lines.isEmpty()) {
            for (Line lineMetro : lines.values()) {
                if (lineMetro.getStationName(name) != null) {
                    return lineMetro.getStationName(name);
                }
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "metro.Metro{"
                + "city='" + city + '\''
                + ", lines=" + lines.values()
                + '}';
    }
}
