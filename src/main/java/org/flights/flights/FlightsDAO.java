package org.flights.flights;

import org.flights.logger.LoggerService;
import org.flights.workers.DataWorker;
import org.flights.workers.FileWorker;
import org.flights.workers.MapWorker;
import org.flights.utils.interfaces.DAO;

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
        List<Flight> fs = worker.readAll();
        LoggerService.info("Trying to read flight with id: " + id);
        return fs.stream().filter(f -> id.equals(f.getId())).findFirst();
    }

    public void save(Flight b) throws IOException {
        ArrayList<Flight> bs = new ArrayList<>(worker.readAll());
        bs.remove(b);
        bs.add(b);
        worker.saveAll(bs);
        LoggerService.info("Saved flight with id: " + b.getId());
    }

    public void delete(String id) throws IOException {
        List<Flight> bs = worker.readAll().stream().filter(b -> !b.getId().equals(id)).toList();
        worker.saveAll(bs);
        LoggerService.info("Deleted flight with id: " + id);
    }

    public List<Flight> readAll() throws IOException {
        LoggerService.info("Reading all flights");
        return worker.readAll();
    }
}