package console;

import utils.exceptions.*;

import java.io.IOException;

@FunctionalInterface
public interface Operation {
    void operation() throws IOException, UserNotFoundException, ExitSessionException;
}
