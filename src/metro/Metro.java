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
                                     String... changeStations) throws ImpossibleCreateStationException {
        checkHaveLineAndHaveNotStation(color, name);
        if (!checkHaveLastStation(color)) {
            throw new NoLastStationException();
        } else if (isLastStationHaveNextStation(color)) {
            throw new NotLastStationException();
        } else if (time.isZero()) {
            throw new TimeIsZeroException();
        }
        Station newStation = new Station(name, lines.get(color).getLastStation(), lines.get(color), this);
        createChangeStations(changeStations, newStation);
        lines.get(color).getLastStation().setStationNext(newStation);
        lines.get(color).getLastStation().setTimeTransferToNextStation(time);
        lines.get(color).getStations().add(newStation);
    }

    private void createChangeStations(String[] changeStations, Station newStation)
            throws ImpossibleCreateStationException {
        for (String stationName : changeStations) {
            Station stationChange = getStationsName(stationName);
            if (stationChange != null) {
                if (!stationChange.getLine().equals(newStation.getLine())) {
                    newStation.addChangeStation(getStationsName(stationName));
                    stationChange.addChangeStation(newStation);
                } else {
                    throw new ChangeLineException();
                }
            } else {
                throw new NameStationException();
            }
        }
    }

    private boolean checkHaveLine(Color color) {
        return lines.containsKey(color);
    }

    private void checkHaveLineAndHaveNotStation(Color color, String name) throws NoLineWithColorException,
            NameStationException {
        if (!checkHaveLine(color)) {
            throw new NoLineWithColorException();
        } else if (getStationsName(name) != null) {
            throw new NameStationException();
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

    private Station getStationsName(String name) {
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
