package utils.exceptions;

public class InvalidNumberException extends IllegalArgumentException {
    public InvalidNumberException(String message) {
        super(message);
    }
}