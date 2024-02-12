package bookings;

import console.MenuHelper;
import flights.Flight;
import passenger.Passenger;
import utils.interfaces.HasId;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;

public class Booking implements HasId, Serializable {
    private final String id;
    private final List<Passenger> passengers;
    private final Flight flight;

    public static String generateId(Flight flight, List<Passenger> passengers) {
        return "b@" + flight.getOrigin().toString().trim().toLowerCase().split(" ")[0] + "_" + flight.getDestination().toString().trim().toLowerCase().split(" ")[0] + "_" + flight.getDepartureTime() + "_" + flight.getMaxPassengers() + "_" + flight.getPassengers() + "_" + passengers.size();
    }

    public Booking(Flight flight, List<Passenger> passengers) {
        this.flight = flight;
        this.id = generateId(flight, passengers);
        this.passengers = passengers;
    }

    public Flight getFlight() {
        return flight;
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }


    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Booking booking = (Booking) object;
        return Objects.equals(id, booking.id) && Objects.equals(passengers, booking.passengers) && Objects.equals(flight, booking.flight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, passengers, flight);
    }
/*
    public String prettyFormat() {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(this.flight.getDepartureTime()), ZoneId.systemDefault());

        String paddedId = padString(getId(), 5);
        String paddedPassengers = padString(String.valueOf(passengers.size()), 5);
        String paddedAirline = padString(this.flight.getAirline(), 20);
        String paddedOrigin = padString(String.valueOf(this.flight.getOrigin()), 12);
        String paddedDestination = padString(String.valueOf(this.flight.getDestination()), 12);
        String paddedDateTime = padString(String.valueOf(dateTime), 20);

        return MenuHelper.colorize(
                "| " + paddedId + " | " + paddedPassengers + " | " + paddedAirline + " | " + paddedOrigin + " | " +
                        paddedDestination + " | " + paddedDateTime + " | ",
                MenuHelper.whiteBoldBackAttribute
        );
    }

 */

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
    public String toString() {
        return "Booking{id: " + this.id + "}";
    }
}
