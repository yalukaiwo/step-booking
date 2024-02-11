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
            long maxSecondsFromNow = 86400;
            long maxSeconds = Instant.now().getEpochSecond() + maxSecondsFromNow;
            long departureTime = ThreadLocalRandom.current().nextLong(Instant.now().getEpochSecond(), maxSeconds);

            // Generating trip time between 1 and 6 hours (in seconds)
            long tripTime = ThreadLocalRandom.current().nextLong(3600, 21600); // 1 hour = 3600 seconds, 6 hours = 21600 seconds

            long arrivalTime = departureTime + tripTime;

            int maxPassengers = ThreadLocalRandom.current().nextInt(minSeats, maxSeats);
            int passengers = ThreadLocalRandom.current().nextInt(0, maxPassengers);
            double initialCost = ThreadLocalRandom.current().nextDouble();
            Airline airline = Airline.getRandom();

            Flight f = create(origin, destination, tripTime, airline, initialCost, departureTime, maxPassengers);
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

    public Flight create(City origin, City destination, long tripTime, Airline airline, double ticketCost, long departureTime, int maxPassengers) throws IOException {
        return service.create(origin, destination, tripTime, airline, ticketCost, departureTime, maxPassengers);
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

    public Optional<Flight> read(String id) throws IOException {
        return service.read(id);
    }

    public void save(Flight b) throws IOException {
        service.save(b);
    }

    public void saveAll(List<Flight> xs) throws IOException {
        service.saveAll(xs);
    }

    public List<Flight> searchFlight(City origin, City destination) throws IOException {
        return service.getAll().stream()
                .filter(f -> f.getOrigin().equals(origin) && f.getDestination().equals(destination))
                .collect(Collectors.toList());
    }
}
