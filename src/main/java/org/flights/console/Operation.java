package org.flights.console;

import org.flights.utils.exceptions.ExitSessionException;
import org.flights.utils.exceptions.FlightNotFoundException;
import org.flights.utils.exceptions.UserNotFoundException;

import java.io.IOException;

@FunctionalInterface
public interface Operation {
    void operation() throws IOException, UserNotFoundException, ExitSessionException;
}
