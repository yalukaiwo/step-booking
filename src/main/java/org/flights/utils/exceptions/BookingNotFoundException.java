package org.flights.utils.exceptions;

import org.flights.console.MenuHelper;
import org.flights.console.colored_console.Ansi;

public class BookingNotFoundException extends Exception {
    public BookingNotFoundException() {
        super(MenuHelper.colorize("Booking not found!", MenuHelper.redAttribute) + Ansi.RESET);
    }
}
