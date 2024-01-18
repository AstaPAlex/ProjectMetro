package exceptions;

public class NameStationException extends Exception {
    public NameStationException() {
        super("Станция с таким именем отсутствует");
    }
}
