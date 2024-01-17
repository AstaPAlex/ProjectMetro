package exceptions;

public class ImpossibleCreateStationException extends Exception {
    public ImpossibleCreateStationException(String textException) {
        super("Ошибка создания станции! " + textException);
    }
}
