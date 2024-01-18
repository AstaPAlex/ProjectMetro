package exceptions;

public class LineExistsException extends ImpossibleCreateLineException {
    public LineExistsException(String message) {
        super(message);
    }
}
