package bookings;

import logger.LoggerService;
import workers.FileWorker;
import workers.*;
import utils.interfaces.DAO;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BookingsDAO implements DAO<Booking> {
    private final DataWorker<Booking> worker;

    public BookingsDAO(File f) {
        this.worker = new FileWorker<>(f);
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
