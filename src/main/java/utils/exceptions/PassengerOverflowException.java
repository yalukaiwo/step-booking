package utils.exceptions;

import console.MenuHelper;
import console.colored_console.Ansi;

public class PassengerOverflowException extends Exception {
    public PassengerOverflowException() {
        super(MenuHelper.colorize("Passenger amount exceeded!", MenuHelper.redAttribute) + Ansi.RESET);
    }
}
