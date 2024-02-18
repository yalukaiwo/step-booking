package bookings;

import console.MenuHelper;
import flights.Flight;
import passenger.Passenger;
import utils.interfaces.HasId;

import java.io.Serial;
import java.io.Serializable;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Booking implements HasId, Serializable {
    @Serial
    private static final long serialVersionUID = 1234567L;
    private final String id;
    private final List<Passenger> passengers;
    private final Flight flight;
    private final PassengerClass passengerClass;

    public static String generateId() {
        return UUID.randomUUID().toString();
    }

    public Booking(Flight flight, List<Passenger> passengers) {
        this.id = generateId();
        this.passengers = passengers;
        this.flight = flight;
        // passenger class is randomized upon booking creation because I am too lazy to actually implement that :)
        this.passengerClass = PassengerClass.getRandom();
    }

    public Flight getFlight() {
        return flight;
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }

    public PassengerClass getPassengerClass() {
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

    private String formatDateTime(long time) {
        Instant instant = Instant.ofEpochMilli(time);
        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        return dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    public String prettyFormat() {
        StringBuilder resultBuilder = new StringBuilder();

        for (Passenger passenger : passengers) {
            String bookingId = padString(getId(), getId().length());
            String paddedPassengerName = padString(passenger.name() + " " + passenger.surname(), 22);
            String paddedPassengerClass = padString(getPassengerClass().toString(), 20);
            String paddedPassengerCost = padString(String.valueOf(this.flight.getTicketCost()), 5);
            String paddedFlightId = padString(this.flight.getId(), 36);
            String paddedAirline = padString(String.valueOf(this.flight.getAirline()), 18);
            String paddedOrigin = padString(String.valueOf(this.flight.getOrigin()), 12);
            String paddedDestination = padString(String.valueOf(this.flight.getDestination()), 12);
            String paddedDateTimeDeparture = padString(formatDateTime(this.flight.getDepartureTime()), 18);
            String paddedDateTimeArrival = padString(formatDateTime(this.flight.getArrivalTime()), 18);

            String formattedInfo = "| " + bookingId + " | " + paddedPassengerName + " | " + paddedPassengerClass + " | " + paddedPassengerCost + " | " + paddedFlightId + " | " + paddedAirline + " | " + paddedOrigin + " | " + paddedDestination + " | " + paddedDateTimeDeparture + " | " + paddedDateTimeArrival + " | ";
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
