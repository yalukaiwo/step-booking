package Flights;

import Utils.Exceptions.PassengerOverflowException;
import Utils.Interfaces.HasId;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class Flight implements HasId, Serializable {
    private final String origin;
    private final String destination;
    private final long departureTime;
    private final int maxPassengers;
    private int passengers;
    private final String id;

    public static String generateId(String origin, String destination, long departureTime, int maxPassengers) {
        return "f@" + origin.trim().toLowerCase().split(" ")[0] + "_" + destination.trim().toLowerCase().split(" ")[0] + "_" + departureTime + "_" + maxPassengers;
    }

    public Flight(String origin, String destination, long departureTime, int maxPassengers) {
        this.departureTime = departureTime;
        this.origin = origin;
        this.destination = destination;
        this.maxPassengers = maxPassengers;
        this.passengers = 0;
        this.id = generateId(origin, destination, departureTime, maxPassengers);
    }

    public void incrementPassengers(int amount) throws PassengerOverflowException {
        if (passengers + amount > maxPassengers) throw new PassengerOverflowException();
        this.passengers = passengers + amount;
    }

    public void decrementPassengers(int amount) throws PassengerOverflowException {
        if (passengers - amount < 0) throw new PassengerOverflowException();
        this.passengers = passengers - amount;
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
    public String toString() {
        return "Flight{origin: " + origin + ", destination: " + destination + ", departureTime: " + LocalDateTime.ofInstant(Instant.ofEpochMilli(departureTime), ZoneId.systemDefault()) + ", passengers: " + passengers + ", maxPassengers: " + maxPassengers + "}";
    }

    public String getDestination() {
        return destination;
    }

    public String getOrigin() {
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
}
