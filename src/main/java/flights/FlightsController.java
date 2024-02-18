package flights;

import utils.exceptions.*;

import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class FlightsController {
    private static final int minSeats = 50;
    private static final int maxSeats = 500;
    private static final int daysCap = 3;
    private final FlightsService service;

    public FlightsController(FlightsService service) {
        this.service = service;
    }

    public Flight generateRandom() throws IOException {
        City origin = City.getRandom();
        City destination = City.getRandom(origin);
        long departureTime = ThreadLocalRandom.current().nextLong(Instant.now().toEpochMilli(), ZonedDateTime.now().plusDays(daysCap).toInstant().toEpochMilli());
        long arrivalTime = ThreadLocalRandom.current().nextLong(6, 13) * 3600 * 1000 + departureTime;
        int maxPassengers = ThreadLocalRandom.current().nextInt(minSeats, maxSeats);
        int passengers = ThreadLocalRandom.current().nextInt(0, maxPassengers);
        double initialCost = ThreadLocalRandom.current().nextDouble();
        Airline airline = Airline.getRandom();

        Flight f = create(origin, destination, airline, initialCost, departureTime, arrivalTime, maxPassengers);
        try {
            f.incrementPassengers(passengers);
        } catch (PassengerOverflowException e) {
            throw new RuntimeException(e);
        }
        return f;
    }

    public List<Flight> generateRandom(int amount) throws IOException {
        ArrayList<Flight> fs = new ArrayList<>();

        for (int i = 0; i < amount; i++) {
            fs.add(generateRandom());
        }

        return fs;
    }

    public List<Flight[]> findConnectingFlights(List<Flight> flights, City origin, City destination) {
        return flights.stream()
                .filter(outboundFlight -> outboundFlight.getOrigin().equals(origin) && !outboundFlight.getDestination().equals(destination))
                .flatMap(outboundFlight ->
                        flights.stream()
                                .filter(inboundFlight ->
                                        !inboundFlight.getOrigin().equals(origin)
                                                && outboundFlight.getDestination().equals(inboundFlight.getOrigin())
                                                && inboundFlight.getDestination().equals(destination)
                                                && inboundFlight.getDepartureTime() - outboundFlight.getArrivalTime() > 3600 * 1000
                                                && inboundFlight.getDepartureTime() - outboundFlight.getArrivalTime() <= 12 * 3600 * 1000)
                                .map(inboundFlight -> new Flight[]{outboundFlight, inboundFlight}))
                .collect(Collectors.toList());
    }

    public Flight create(City origin, City destination, Airline airline, double ticketCost, long departureTime, long arrivalTime, int maxPassengers) throws IOException {
        return service.create(origin, destination, airline, ticketCost, departureTime, arrivalTime, maxPassengers);
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

    public void save(Flight b) throws IOException {
        service.save(b);
    }

    public void save(List<Flight> xs) throws IOException {
        service.save(xs);
    }

    public List<Flight> search(City origin, City destination) throws IOException {
        return service.getAll().stream()
                .filter(f -> f.getOrigin().equals(origin) && f.getDestination().equals(destination))
                .toList();
    }


    public List<Flight> searchByDate(List <Flight> flights, String dateString) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH);
            LocalDate searchDate = LocalDate.parse(dateString, formatter);

            return flights.stream()
                    .filter(f -> isSameDay(f.getDepartureTime(), searchDate))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid date format. Please use 'dd/MM/yyyy'.");
        }
    }

    private boolean isSameDay(long dateTimeMillis, LocalDate date) {
        LocalDate flightDate = LocalDate.ofInstant(Instant.ofEpochMilli(dateTimeMillis), ZoneId.systemDefault());
        return flightDate.equals(date);
    }
}
