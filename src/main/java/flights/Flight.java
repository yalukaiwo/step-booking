package flights;

import utils.exceptions.PassengerOverflowException;
import utils.interfaces.HasId;

import java.io.IOException;
import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;
import java.util.Optional;

public class Flight implements HasId, Serializable {
    private final City origin;
    private final City destination;
    private final long departureTime;
    private final int maxPassengers;
    private final String id;
    private final double ticketCost;
    private final Airline airline;
    private int passengers;

    public Flight(City origin, City destination, Airline airline, double ticketCost, long departureTime, int maxPassengers) {
        this.departureTime = departureTime;
        this.ticketCost = airline.getCost(ticketCost);
        this.origin = origin;
        this.destination = destination;
        this.maxPassengers = maxPassengers;
        this.passengers = 0;
        this.airline = airline;
        this.id = generateId(origin, destination, airline, departureTime, maxPassengers);
    }

    public static String generateId(City origin, City destination, Airline airline, long departureTime, int maxPassengers) {
        return "f@" + origin.toString().trim().toLowerCase().split(" ")[0] + "_" + destination.toString().trim().toLowerCase().split(" ")[0] + "_" + airline.toString() + "_" + departureTime + "_" + maxPassengers;
    }

    public void incrementPassengers(int amount) throws PassengerOverflowException {
        if (passengers + amount > maxPassengers) throw new PassengerOverflowException();
        this.passengers = passengers + amount;
    }

    public void decrementPassengers(int amount) throws PassengerOverflowException {
        if (passengers - amount < 0) throw new PassengerOverflowException();
        this.passengers = passengers - amount;
    }

    public long getHoursBeforeDeparting() {
        Duration d = Duration.between(Instant.now(), Instant.ofEpochMilli(departureTime));

        return d.toHours();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (this.getClass() != o.getClass()) return false;
        Flight f = (Flight) o;
        return this.getId().equals(f.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(origin, destination, departureTime, maxPassengers, id, ticketCost, airline, passengers);
    }

    @Override
    public String toString() {
        return "Flight{origin: " + origin + ", destination: " + destination + ", departureTime: " + LocalDateTime.ofInstant(Instant.ofEpochMilli(departureTime), ZoneId.systemDefault()) + ", passengers: " + passengers + ", maxPassengers: " + maxPassengers + "}";
    }

    public City getDestination() {
        return destination;
    }

    public City getOrigin() {
        return origin;
    }

    public long getDepartureTime() {
        return departureTime;
    }

    public int getMaxPassengers() {
        return maxPassengers;
    }

    public int getPassengers() {
        return passengers;
    }

    public String getAirline() {
        return airline.toString();
    }

    public double getTicketCost() {
        return ticketCost;
    }

    public int getFreeSeats() {
        return maxPassengers - passengers;
    }
}
