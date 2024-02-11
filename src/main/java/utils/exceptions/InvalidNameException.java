package utils.exceptions;

public class InvalidNameException extends IllegalArgumentException {
    public InvalidNameException(String message) {
        super(message);
    }

    public static class UserRegisterException extends Throwable {
    }
}
