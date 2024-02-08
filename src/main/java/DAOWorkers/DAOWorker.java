package DAOWorkers;

import java.io.*;
import java.util.List;

public interface DAOWorker<T> {
    void saveAll(List<T> xs) throws IOException;

    List<T> readAll() throws IOException;
}
