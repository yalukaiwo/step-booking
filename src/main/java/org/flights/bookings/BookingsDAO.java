package org.flights.bookings;

import org.flights.logger.LoggerService;
import org.flights.workers.FileWorker;
import org.flights.workers.MapWorker;
import org.flights.workers.DataWorker;
import org.flights.utils.interfaces.DAO;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BookingsDAO implements DAO<Booking> {
    private final DataWorker<Booking> worker;

    public BookingsDAO(File f) {
        this.worker = new FileWorker<>(f);
    }

    // FOR TESTING PURPOSES
    public BookingsDAO(HashMap<String, Booking> bs) {
        this.worker = new MapWorker<>(bs);
    }

    public Optional<Booking> read(String id) throws IOException {
        List<Booking> bs = worker.readAll();
        LoggerService.info("Trying to read booking with id: " + id);
        return bs.stream().filter(b -> Objects.equals(b.getId(), id)).findFirst();
    }

    public void save(Booking b) throws IOException {
        ArrayList<Booking> bs = new ArrayList<>(worker.readAll());
        bs.remove(b);
        bs.add(b);
        worker.saveAll(bs);
        LoggerService.info("Saved booking with id: " + b.getId());
    }

    public void delete(String id) throws IOException {
        List<Booking> bs = worker.readAll().stream().filter(b -> !b.getId().equals(id)).toList();
        worker.saveAll(bs);
        LoggerService.info("Deleted booking with id: " + id);
    }

    public List<Booking> readAll() throws IOException {
        LoggerService.info("Reading all bookings");
        return worker.readAll();
    }
}