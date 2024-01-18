package exceptions;

public class StationHaveException extends ImpossibleCreateStationException {
    public StationHaveException() {
        super("Такая станция уже есть!");
    }
}
