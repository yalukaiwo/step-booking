package flights;

import workers.DataWorker;
import workers.FileWorker;
import workers.MapWorker;
import utils.interfaces.DAO;

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
        List<Flight> flights = worker.readAll();
        return flights.stream()
                .filter(f -> Objects.equals(f.getId(), id))
                .findFirst();
    }

    public void save(Flight b) throws IOException {
        ArrayList<Flight> bs = new ArrayList<>(worker.readAll());
        bs.remove(b);
        bs.add(b);
        worker.saveAll(bs);
    }

    public void saveAll(List<Flight> xs) throws IOException {
        worker.saveAll(xs);
    }

    public void delete(String id) throws IOException {
        List<Flight> bs = worker.readAll().stream().filter(b -> !b.getId().equals(id)).toList();
        worker.saveAll(bs);
    }

    public List<Flight> readAll() throws IOException {
        return worker.readAll();
    }

    public Flight getFlightByFlightId(String flightId) throws IOException {
        List<Flight> flights = readAll();
        for (Flight flight : flights) {
            if (flight.getId().equals(flightId)) {
                return flight;
            }
        }
        return null;
    }

}
