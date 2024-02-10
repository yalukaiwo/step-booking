package workers;

import java.io.*;
import java.util.List;

public interface DataWorker<T> {
    void saveAll(List<T> xs) throws IOException;

    List<T> readAll() throws IOException;
}
