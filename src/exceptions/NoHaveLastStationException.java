package exceptions;

public class NoHaveLastStationException extends ImpossibleCreateStationException {
    public NoHaveLastStationException() {
        super("Отсутствует последняя станция!");
    }
}
