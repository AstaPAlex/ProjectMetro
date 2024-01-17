package exceptions;

public class NoLastStationException extends ImpossibleCreateStationException {
    public NoLastStationException() {
        super("Отсутствует последняя станция!");
    }
}
