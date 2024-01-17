package exceptions;

public class NoLineWithColorException extends ImpossibleCreateStationException {
    public NoLineWithColorException() {
        super("Линии с таким цветом не существует!");
    }
}
