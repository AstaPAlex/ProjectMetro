package exceptions;

public class ImpossibleCreateStationException extends Exception {
    public ImpossibleCreateStationException(String textException) {
        super("Ошибка в работе! " + textException);
    }
}
