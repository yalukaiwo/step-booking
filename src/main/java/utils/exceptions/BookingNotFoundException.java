package utils.exceptions;

import console.MenuHelper;
import console.colored_console.Ansi;

public class BookingNotFoundException extends Exception {
    public BookingNotFoundException() {
        super(MenuHelper.colorize("Booking not found!", MenuHelper.redAttribute) + Ansi.RESET);
    }
}
