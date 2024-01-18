package exceptions;

public class NameStationException extends ImpossibleBuildRoute {
    public NameStationException() {
        super("Ошибка в имени станции!");
    }
}
