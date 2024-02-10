package utils.exceptions;

public class FlightNotFoundException extends Exception {
    public FlightNotFoundException() {
        super("Flight not found!");
    }
}
