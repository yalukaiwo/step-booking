package org.flights.utils.exceptions;

import org.flights.console.MenuHelper;
import org.flights.console.colored_console.Ansi;

public class PassengerOverflowException extends Exception {
    public PassengerOverflowException() {
        super(MenuHelper.colorize("Passenger amount exceeded!", MenuHelper.redAttribute) + Ansi.RESET);
    }
}
