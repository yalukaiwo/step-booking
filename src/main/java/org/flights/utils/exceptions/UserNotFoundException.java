package org.flights.utils.exceptions;

import org.flights.console.MenuHelper;
import org.flights.console.colored_console.Ansi;

public class UserNotFoundException extends Exception {
    public UserNotFoundException() {
        super(MenuHelper.colorize("User not found!", MenuHelper.redAttribute) + Ansi.RESET);
    }
}
