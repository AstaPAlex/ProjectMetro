package exceptions;

public class StartEqualsFinishException extends ImpossibleBuildRoute {
    public StartEqualsFinishException() {
        super("Станция старта == станции финиша!");
    }
}
