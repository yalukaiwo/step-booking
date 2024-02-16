package utils.interfaces;

import utils.exceptions.UserNotFoundException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface DAO<T> {
    Optional<T> read(String id) throws IOException, UserNotFoundException;
    void save(T x) throws IOException;
    void delete(String id) throws IOException;
    List<T> readAll() throws IOException;
}
