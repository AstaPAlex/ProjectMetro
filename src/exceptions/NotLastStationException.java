package exceptions;

public class NotLastStationException extends ImpossibleCreateStationException {
    public NotLastStationException() {
        super("Последняя станция не является последней!");
    }
}
