package exceptions;

public class StationHaveException extends Exception {
    public StationHaveException() {
        super("Такая станция уже есть!");
    }
}
