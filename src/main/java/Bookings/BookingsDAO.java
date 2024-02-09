package Bookings;

import DataWorkers.FileWorker;
import DataWorkers.MapWorker;
import DataWorkers.DataWorker;
import Flights.Flight;
import Utils.Interfaces.DAO;

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
        return bs.stream().filter(b -> Objects.equals(b.getId(), id)).findFirst();
    }

    public void save(Booking b) throws IOException {
        ArrayList<Booking> bs = new ArrayList<>(worker.readAll());
        bs.remove(b);
        bs.add(b);
        worker.saveAll(bs);
    }

    public void delete(String id) throws IOException {
        List<Booking> bs = worker.readAll().stream().filter(b -> !b.getId().equals(id)).toList();
        worker.saveAll(bs);
    }

    public List<Booking> readAll() throws IOException {
        return worker.readAll();
    }
}
