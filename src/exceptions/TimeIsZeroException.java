package exceptions;

public class TimeIsZeroException extends ImpossibleCreateStationException {
    public TimeIsZeroException() {
        super("Время перегона не может быть нулевым!");
    }
}
