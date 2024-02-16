package utils.exceptions;

import console.MenuHelper;
import console.colored_console.Ansi;

public class UserNotFoundException extends Exception {
    public UserNotFoundException() {
        super(MenuHelper.colorize("User not found!", MenuHelper.redAttribute) + Ansi.RESET);
    }
}
