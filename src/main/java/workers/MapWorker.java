package workers;

import utils.interfaces.HasId;

import java.util.HashMap;
import java.util.List;

// FOR TESTING PURPOSES

public class MapWorker<T extends HasId> implements DataWorker<T> {
    private final HashMap<String, T> bs;

    public MapWorker(HashMap<String, T> bs) {
        this.bs = bs;
    }

    public void saveAll(List<T> xs) {
        bs.clear();
        xs.forEach(x -> bs.put(x.getId(), x));
    }

    public List<T> readAll() {
        return bs.values().stream().toList();
    }
}