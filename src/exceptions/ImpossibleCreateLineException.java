package exceptions;

public class ImpossibleCreateLineException extends Exception {
    public ImpossibleCreateLineException() {
        super("Невозможно создасть линию! Линия с таким цветом уже существует!");
    }
}
