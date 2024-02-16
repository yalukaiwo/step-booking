package org.flights.utils.exceptions;

import org.flights.console.MenuHelper;
import org.flights.console.colored_console.Ansi;

public class ExitSessionException extends Exception {
    public ExitSessionException(String s) {
        super(MenuHelper.colorize("Exiting session.", MenuHelper.magentaAttribute) + Ansi.RESET);
    }
}
