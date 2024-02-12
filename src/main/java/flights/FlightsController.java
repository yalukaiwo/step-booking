package flights;

import utils.exceptions.FlightNotFoundException;
import utils.exceptions.PassengerOverflowException;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.Objects;

public class FlightsController {
    private static final int minSeats = 50;
    private static final int maxSeats = 500;
    private final FlightsService service;

    public FlightsController(FlightsService service) {
        this.service = service;
    }

    public Flight create(City origin, City destination, Airline airline, double ticketCost, long departureTime, int maxPassengers) throws IOException {
        return service.create(origin, destination, airline, ticketCost, departureTime, maxPassengers);
    }

    public Optional<Flight> generateRandom() throws IOException {
        City origin = City.getRandom();
        City destination = City.getRandom(origin);
        long maxSecondsFromNow = 86400;
        long maxSeconds = Instant.now().getEpochSecond() + maxSecondsFromNow;
        long departureTime = ThreadLocalRandom.current().nextLong(Instant.now().getEpochSecond(), maxSeconds);

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
    }

    public List<Flight> generateRandom(int amount) {
        return IntStream.range(0, amount)
                .mapToObj(i -> {
                    try {
                        return generateRandom().orElse(null); // Return null if flight creation fails
                    } catch (IOException e) {
                        e.printStackTrace(); // Handle or log the exception
                        return null; // Return null if an exception occurs
                    }
                })
                .filter(Objects::nonNull) // Filter out null flights
                .collect(Collectors.toList());
    }

    public List<Flight> getAllDepartingIn(int hours) throws IOException {
        return service.getAll().stream().filter(f -> f.getHoursBeforeDeparting() <= hours).toList();
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

    public Flight getFlightBuFlightId(String flightId) throws IOException {
        return service.getFlightBuFlightId(flightId);
    }
}
