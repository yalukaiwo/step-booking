package console;

import java.io.IOException;

@FunctionalInterface
public interface Operation {
    void operation() throws IOException;
}
