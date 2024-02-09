package Flights;

import DataWorkers.DataWorker;
import DataWorkers.FileWorker;
import DataWorkers.MapWorker;
import Utils.Interfaces.DAO;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class FlightsDAO implements DAO<Flight> {
    private final DataWorker<Flight> worker;

    public FlightsDAO(File f) {
        this.worker = new FileWorker<>(f);
    }

    // FOR TESTING PURPOSES
    public FlightsDAO(HashMap<String, Flight> bs) {
        this.worker = new MapWorker<>(bs);
    }

    public Optional<Flight> read(String id) throws IOException {
        List<Flight> bs = worker.readAll();
        return bs.stream().filter(b -> Objects.equals(b.getId(), id)).findFirst();
    }

    public void save(Flight b) throws IOException {
        ArrayList<Flight> bs = new ArrayList<>(worker.readAll());
        bs.remove(b);
        bs.add(b);
        worker.saveAll(bs);
    }

    public void delete(String id) throws IOException {
        List<Flight> bs = worker.readAll().stream().filter(b -> !b.getId().equals(id)).toList();
        worker.saveAll(bs);
    }

    public List<Flight> readAll() throws IOException {
        return worker.readAll();
    }
}
