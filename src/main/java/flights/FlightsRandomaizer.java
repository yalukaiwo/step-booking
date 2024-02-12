package flights;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class FlightsRandomaizer {
    private final int quantity;
    private final int minSeats;
    private final int maxSeats;

    public FlightsRandomaizer(int quantity, int minSeats, int maxSeats) {
        this.quantity = quantity;
        this.minSeats = minSeats;
        this.maxSeats = maxSeats;
    }

    int generateValueFromRange(int min, int max) {
        Random r = new Random();
        int val = r.nextInt(max - min) + min;
        return val;
    }

    public List<Flight> get() {
        List<Flight> flights = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            flights.add(generateNewFlight());
        }
        return flights;
    }

    private long generateTripTime() {
        // Generate trip time between 1 and 6 hours (in milliseconds)
        return generateValueFromRange(1, 6) * 60 * 60 * 1000;
    }

    private Flight generateNewFlight() {
        int id = generateValueFromRange(100, 1000);
        City[] airports = City.values();
        int arraySize = airports.length;

        City origin = airports[generateValueFromRange(0, arraySize - 1)];
        City destination;
        do {
            destination = airports[generateValueFromRange(0, arraySize - 1)];
        } while (origin == destination);

        long tripTime = generateTripTime();
        int maxPassengers = generateMaxSeats();
        int passengers = generateSeats();
        Airline airline = Airline.getRandom();

        double ticketCost = evaluateTicketCost(tripTime);

        return new Flight(origin, destination, tripTime, airline, ticketCost, maxPassengers, passengers);
    }

    private double evaluateTicketCost(long tripTime) {
        final double BASE_COST_PER_HOUR = 50.0;
        final double COST_MULTIPLIER = 0.05;
        double hours = tripTime / (1000.0 * 60 * 60);
        return BASE_COST_PER_HOUR + (hours * COST_MULTIPLIER);
    }

    private int generateSeats() {
        Random random = new Random();
        return random.nextInt(maxSeats - minSeats + 1) + minSeats;
    }

    private int generateMaxSeats() {
        Random random = new Random();
        return random.nextInt(maxSeats - minSeats + 1) + minSeats;
    }

    public List<Flight> searchFlightsBetweenCitiesWithConnection(List<Flight> allFlights) {
        List<Flight> foundFlights = new ArrayList<>();

        Map<City, List<Flight>> flightsByOrigin = new HashMap<>();
        for (Flight flight : allFlights) {
            flightsByOrigin.computeIfAbsent(flight.getOrigin(), k -> new ArrayList<>()).add(flight);
        }

        City[] airports = City.values();

        for (City origin : airports) {
            for (City destination : airports) {
                if (origin != destination) {
                    // Find direct flights
                    foundFlights.addAll(flightsByOrigin.getOrDefault(origin, Collections.emptyList())
                            .stream()
                            .filter(flight -> flight.getDestination() == destination)
                            .collect(Collectors.toList()));

                    // Find connecting flights
                    List<Flight> originFlights = flightsByOrigin.getOrDefault(origin, Collections.emptyList());
                    List<Flight> destinationFlights = flightsByOrigin.getOrDefault(destination, Collections.emptyList());

                    for (Flight firstLeg : originFlights) {
                        for (Flight secondLeg : destinationFlights) {
                            if (firstLeg.getDestination() == secondLeg.getOrigin()) {
                                Instant firstArrival = Instant.ofEpochMilli(firstLeg.getDepartureTime() + firstLeg.getTripTime());
                                Instant secondDeparture = Instant.ofEpochMilli(secondLeg.getDepartureTime());
                                Duration layoverTime = Duration.between(firstArrival, secondDeparture);
                                Duration maxLayoverTime = Duration.ofHours(12);

                                if (layoverTime.compareTo(maxLayoverTime) <= 0) {
                                    foundFlights.add(firstLeg);
                                    foundFlights.add(secondLeg);
                                }
                            }
                        }
                    }
                }
            }
        }

        return foundFlights;
    }

    private List<Flight> searchConnectingFlights(City origin, City destination, Map<City, List<Flight>> flightsByOrigin) {
        List<Flight> connectingFlights = new ArrayList<>();

        List<Flight> originFlights = flightsByOrigin.getOrDefault(origin, Collections.emptyList());
        List<Flight> destinationFlights = flightsByOrigin.getOrDefault(destination, Collections.emptyList());

        for (Flight firstLeg : originFlights) {
            for (Flight secondLeg : destinationFlights) {
                if (firstLeg.getDestination() == secondLeg.getOrigin()) {
                    Instant firstArrival = Instant.ofEpochMilli(firstLeg.getDepartureTime() + firstLeg.getTripTime());
                    Instant secondDeparture = Instant.ofEpochMilli(secondLeg.getDepartureTime());
                    Duration layoverTime = Duration.between(firstArrival, secondDeparture);
                    Duration maxLayoverTime = Duration.ofHours(12);

                    if (layoverTime.compareTo(maxLayoverTime) <= 0) {
                        connectingFlights.add(firstLeg);
                        connectingFlights.add(secondLeg);
                    }
                }
            }
        }

        return connectingFlights;
    }

    public static void main(String[] args) {
        FlightsRandomaizer randomizer = new FlightsRandomaizer(10, 50, 500);
        List<Flight> flights = randomizer.get();

        for (Flight flight : flights) {
            System.out.println(flight);
        }

        List<Flight> connectingFlights = randomizer.searchFlightsBetweenCitiesWithConnection(flights);
        System.out.println("Connecting Flights:");
        for (Flight flight : connectingFlights) {
            System.out.println(flight);
        }
    }
}
