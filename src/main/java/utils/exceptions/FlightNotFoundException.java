package utils.exceptions;

import console.MenuHelper;
import console.colored_console.Ansi;

public class FlightNotFoundException extends Exception {
    public FlightNotFoundException() {
        super(MenuHelper.colorize("Flight not found!", MenuHelper.redAttribute) + Ansi.RESET);
    }
}
