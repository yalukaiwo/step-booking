package utils.exceptions;

public class PassengerOverflowException extends Exception {
    public PassengerOverflowException() {
        super("Passenger amount exceeded!");
    }
}
