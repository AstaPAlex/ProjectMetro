package exceptions;

public class NotFoundStationException extends ImpossibleCreateStationException {
    public NotFoundStationException(String message) {
        super(message);
    }
}
