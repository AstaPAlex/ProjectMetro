package exceptions;

public class NameStationException extends ImpossibleCreateStationException {
    public NameStationException() {
        super("Ошибка в имени станции!");
    }
}
