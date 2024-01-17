package exceptions;

public class ChangeLineException extends ImpossibleCreateStationException {

    public ChangeLineException() {
        super("Пересадка невозможна внутри одной линии!");
    }
}
