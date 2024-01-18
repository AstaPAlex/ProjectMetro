package exceptions;

public class StartEqualsFinishException extends Exception {
    public StartEqualsFinishException() {
        super("Станция старта == станции финиша!");
    }
}
