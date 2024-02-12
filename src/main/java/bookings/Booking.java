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
}

    public static String generateId(Flight flight, List<Passenger> passengers) {
        return "b@" + flight.getOrigin().toString().trim().toLowerCase().split(" ")[0] + "_" + flight.getDestination().toString().trim().toLowerCase().split(" ")[0] + "_" + flight.getDepartureTime() + "_" + flight.getMaxPassengers() + "_" + flight.getPassengers() + "_" + passengers.size();
    }

    public Booking(Flight flight, List<Passenger> passengers) {
        this.id = generateId(flight, passengers);
        this.passengers = passengers;
        this.flight = flight;
    }

    @Override
    public String getId() {
        return id;
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

    public Flight getFlight() {
        return flight;
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }
}
