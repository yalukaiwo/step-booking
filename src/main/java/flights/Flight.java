package flights;

import console.MenuHelper;
import utils.exceptions.PassengerOverflowException;
import utils.interfaces.HasId;

import java.io.Serial;
import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;
import java.util.UUID;

public class Flight implements HasId, Serializable {
    @Serial
    private static final long serialVersionUID = 1234567L;
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
        this.id = generateId();
    }

    public static String generateId() {
        return UUID.randomUUID().toString();
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

    public String prettyFormat() {
        String paddedId = padString(getId(), getId().length());
        String paddedAirline = padString(String.valueOf(getAirline()), 20);
        String paddedOrigin = padString(String.valueOf(origin), 12);
        String paddedDestination = padString(String.valueOf(destination), 12);
        String paddedDateTime = padString(String.valueOf(LocalDateTime.ofInstant(Instant.ofEpochMilli(departureTime), ZoneId.systemDefault())), 20);
        String paddedSeats = padString(String.valueOf(passengers), 5);

        return MenuHelper.colorize("| " + paddedId + " | " + paddedAirline + " | " + paddedOrigin + " | " + paddedDestination + " | " + paddedDateTime + " | " + paddedSeats + " |", MenuHelper.whiteBoldBackAttribute);
    }

    private String padString(String str, int length) {
        if (str.length() >= length) {
            return str.substring(0, length - 1);
        } else {
            StringBuilder sb = new StringBuilder(str);
            while (sb.length() < length) {
                sb.append(" ");
            }
            return sb.toString();
        }
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
        return "Flight{id: " + id + ", origin: " + origin + ", destination: " + destination + ", departureTime: " + LocalDateTime.ofInstant(Instant.ofEpochMilli(departureTime), ZoneId.systemDefault()) + ", passengers: " + passengers + ", maxPassengers: " + maxPassengers + "}";
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
