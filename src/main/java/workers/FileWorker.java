package workers;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileWorker<T> implements DataWorker<T> {
    private final File f;

    public FileWorker(File f) {
        this.f = f;
    }

    public void saveAll(List<T> xs) throws IOException {
        if (!f.exists()) f.createNewFile();
        try (var oos = new ObjectOutputStream(new FileOutputStream(f))) {
            oos.writeObject(xs);
        }
    }

    public List<T> readAll() throws IOException {
        try (var ois = new ObjectInputStream(new FileInputStream(f))) {
            @SuppressWarnings("unchecked")
            List<T> xs = (List<T>) ois.readObject();
            return xs;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        }
    }
}
