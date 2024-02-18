package utils.exceptions;

import console.MenuHelper;
import console.colored_console.Ansi;

public class ExitSessionException extends Exception {
    public ExitSessionException() {
        super(MenuHelper.colorize("Exiting session.", MenuHelper.magentaAttribute) + Ansi.RESET);
    }
}
