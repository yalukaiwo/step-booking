package console;

import utils.exceptions.FlightNotFoundException;

import java.io.IOException;

@FunctionalInterface
public interface Operation {
    void operation() throws IOException, FlightNotFoundException;
}
