package flights;

import utils.exceptions.FlightNotFoundException;

import java.io.IOException;
import java.util.*;

public class FlightsService {
    private final FlightsDAO db;

    public FlightsService(FlightsDAO dao) {
        this.db = dao;
    }

    public Flight create(City origin, City destination, Airline airline, double ticketCost, long departureTime, long arrivalTime, int maxPassengers) throws IOException {
        Flight f = new Flight(origin, destination, airline, ticketCost, departureTime, arrivalTime, maxPassengers);
        db.save(f);
        return f;
    }

    public void delete(Flight f) throws IOException {
        db.delete(f.getId());
    }

    public void delete(String id) throws IOException {
        db.delete(id);
    }

    public Flight getById(String id) throws IOException, FlightNotFoundException {
        Optional<Flight> f = db.read(id);
        if (f.isEmpty()) throw new FlightNotFoundException();
        return f.get();
    }

    public List<Flight> getAll() throws IOException {
        return db.readAll();
    }

    public void save(Flight f) throws IOException {
        db.save(f);
    }

    public void save(List<Flight> fs) throws IOException {
        for (Flight f : fs) {
            db.save(f);
        }
    }
}
