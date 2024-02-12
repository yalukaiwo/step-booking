package flights;

import utils.exceptions.FlightNotFoundException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class FlightsService {
    private final FlightsDAO db;

    public FlightsService(FlightsDAO dao) {
        this.db = dao;
    }

    public Flight create(City origin, City destination, Airline airline, double ticketCost, long departureTime, int maxPassengers) throws IOException {
        Flight f = new Flight(origin, destination, airline, ticketCost, departureTime, maxPassengers);
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
        Optional<Flight> flightOptional = db.read(id);
        if (flightOptional.isPresent()) {
            return flightOptional.get();
        } else {
            throw new FlightNotFoundException();
        }
    }

    public List<Flight> getAll() throws IOException {
        return db.readAll();
    }

    public Optional<Flight> read(String id) throws IOException {
        return db.read(id);
    }

    public void save(Flight b) throws IOException {
        db.save(b);
    }

    public void saveAll(List<Flight> xs) throws IOException {
        db.saveAll(xs);
    }

    public void clear() throws IOException {
        List<Flight> fs = db.readAll();
        for (Flight f : fs) {
            db.delete(f.getId());
        }
    }
    public Flight getFlightBuFlightId(String flightId) throws IOException {
        return db.getFlightByFlightId(flightId);
    }
}
