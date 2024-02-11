package bookings;

import flights.Flight;
import passenger.Passenger;
import utils.interfaces.HasId;

import java.io.Serializable;
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

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (this.getClass() != o.getClass()) return false;
        Booking b = (Booking) o;
        return this.getId().equals(b.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, passengers, flight);
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
