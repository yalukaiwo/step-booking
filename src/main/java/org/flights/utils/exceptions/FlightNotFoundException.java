package org.flights.utils.exceptions;

import org.flights.console.MenuHelper;
import org.flights.console.colored_console.Ansi;

public class FlightNotFoundException extends Exception {
    public FlightNotFoundException() {
        super(MenuHelper.colorize("Flight not found!", MenuHelper.redAttribute) + Ansi.RESET);
    }
}
