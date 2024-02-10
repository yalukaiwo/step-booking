package utils.exceptions;

public class BookingNotFoundException extends Exception {
    public BookingNotFoundException() {
        super("Booking not found!");
    }
}
