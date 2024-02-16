package org.flights.bookings;

import org.flights.console.MenuHelper;
import org.flights.flights.Flight;
import org.flights.passenger.Passenger;
import org.flights.utils.interfaces.HasId;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Booking implements HasId, Serializable {
    @Serial
    private static final long serialVersionUID = 1234567L;
    private final String id;
    private final List<Passenger> passengers;
    private final Flight flight;
    private final String passengerClass;

    public static String generateId() {
        return UUID.randomUUID().toString();
    }

    private static final List<String> passengerClasses = Arrays.asList(
            "ECONOMY",
            "PREMIUM_ECONOMY",
            "BUSINESS",
            "FIRST_CLASS"
    );

    public static String selectRandomPassengerClass() {
        Random random = new Random();
        int randomIndex = random.nextInt(passengerClasses.size());
        String randomClassName = passengerClasses.get(randomIndex);
        return randomClassName;
    }

    public Booking(Flight flight, List<Passenger> passengers) {
        this.id = generateId();
        this.passengers = passengers;
        this.flight = flight;
        this.passengerClass = selectRandomPassengerClass();
    }


    public Flight getFlight() {
        return flight;
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }

    public String getPassengerClass() {
        return passengerClass;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, passengers, flight);
    }

    public String prettyFormat() {
        Instant instant = Instant.ofEpochMilli(this.flight.getDepartureTime());
        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        String formattedDateTime = dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

        StringBuilder resultBuilder = new StringBuilder();

        for (Passenger passenger : passengers) {
            String bookingId = padString(getId(), getId().length());
            String paddedPassengerName = padString(passenger.getName() + " " + passenger.getSurname(), 22);
            String paddedPassengerClass = padString(getPassengerClass().toString(), 20);
            String paddedPassengerCost = padString(String.valueOf(this.flight.getTicketCost()), 5);
            String paddedFlightId = padString(this.flight.getId(), 36);
            String paddedAirline = padString(String.valueOf(this.flight.getAirline()), 18);
            String paddedOrigin = padString(String.valueOf(this.flight.getOrigin()), 12);
            String paddedDestination = padString(String.valueOf(this.flight.getDestination()), 12);
            String paddedDateTime = padString(formattedDateTime, 18);

            String formattedInfo = "| " + bookingId + " | " + paddedPassengerName + " | " + paddedPassengerClass + " | " + paddedPassengerCost + " | " + paddedFlightId + " | " + paddedAirline + " | " + paddedOrigin + " | " + paddedDestination + " | " + paddedDateTime + " | ";
            resultBuilder.append(MenuHelper.colorize(formattedInfo, MenuHelper.whiteBoldBackAttribute)).append("\n");
        }

        return resultBuilder.toString();
    }

    private String padString(String str, int length) {
        if (str.length() >= length) {
            return str.substring(0, length);
        } else {
            StringBuilder sb = new StringBuilder(str);
            while (sb.length() < length) {
                sb.append(" ");
            }
            return sb.toString();
        }
    }
  
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (this.getClass() != o.getClass()) return false;
        Booking b = (Booking) o;
        return this.getId().equals(b.getId());
    }

    @Override
    public String toString() {
        return "Booking{id: " + this.id + "}";
    }
}
