package flights;

import utils.exceptions.FlightNotFoundException;
import utils.exceptions.PassengerOverflowException;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FlightsController {
    private static final int minSeats = 50;
    private static final int maxSeats = 500;
    private final FlightsService service;

    public FlightsController(FlightsService service) {
        this.service = service;
    }

    public Optional<Flight> generateRandom() {
        try {
            City origin = City.getRandom();
            City destination = City.getRandom(origin);
            long departureTime = Instant.ofEpochSecond(ThreadLocalRandom.current().nextLong(Instant.now().getEpochSecond(), Instant.MAX.getEpochSecond())).toEpochMilli();
            int maxPassengers = ThreadLocalRandom.current().nextInt(minSeats, maxSeats);
            int passengers = ThreadLocalRandom.current().nextInt(0, maxPassengers);
            double initialCost = ThreadLocalRandom.current().nextDouble();
            Airline airline = Airline.getRandom();

            Flight f = create(origin, destination, airline, initialCost, departureTime, maxPassengers);
            try {
                f.incrementPassengers(passengers);
                return Optional.of(f);
            } catch (PassengerOverflowException e) {
                return Optional.empty();
            }
        } catch (IOException e) {
            // Handle IOException or rethrow it depending on the context
            return Optional.empty();
        }
    }

    public List<Flight> generateRandom(int amount) {
        return IntStream.range(0, amount)
                .mapToObj(i -> generateRandom().orElse(null))
                .filter(flight -> flight != null)
                .collect(Collectors.toList());
    }

    public List<Flight> getAllDepartingIn(int hours) throws IOException {
        return service.getAll().stream().filter(f -> f.getHoursBeforeDeparting() <= hours).toList();
    }

    public Flight create(City origin, City destination, Airline airline, double ticketCost, long departureTime, int maxPassengers) throws IOException {
        return service.create(origin, destination, airline, ticketCost, departureTime, maxPassengers);
    }

    public void clear() throws IOException {
        service.clear();
    }

    public void delete(Flight f) throws IOException {
        service.delete(f);
    }

    public void delete(String id) throws IOException {
        service.delete(id);
    }

    public Flight getById(String id) throws IOException, FlightNotFoundException {
        return service.getById(id);
    }

    public List<Flight> getAll() throws IOException {
        return service.getAll();
    }

    public List<Flight> searchFlight(City origin, City destination) throws IOException {
        return service.getAll().stream()
                .filter(f -> f.getOrigin().equals(origin) && f.getDestination().equals(destination))
                .collect(Collectors.toList());
    }
}