package utils.exceptions;

import java.util.InputMismatchException;

public class UserRegisterException extends InputMismatchException {
    public UserRegisterException(String message) {
        super(message);
    }
}