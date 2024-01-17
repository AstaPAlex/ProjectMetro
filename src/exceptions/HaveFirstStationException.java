package exceptions;

public class HaveFirstStationException extends ImpossibleCreateStationException {
    public HaveFirstStationException() {
        super("У линии уже есть первая станция!");
    }
}
