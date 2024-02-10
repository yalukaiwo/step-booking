package bookings;

import flights.Flight;
import passenger.Passenger;
import users.User;
import utils.interfaces.HasId;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

public class Booking implements HasId, Serializable {
    private final String id;
    private final List<Passenger> passengers;
    private final User user;
    private final Flight flight;

    public static String generateId(Flight flight, List<Passenger> passengers) {
        return "b@" + flight.getOrigin().toString().trim().toLowerCase().split(" ")[0] + "_" + flight.getDestination().toString().trim().toLowerCase().split(" ")[0] + "_" + flight.getDepartureTime() + "_" + flight.getMaxPassengers() + "_" + flight.getPassengers() + "_" + passengers.size();
    }

    public Booking(Flight flight, User user) {
        this.user = user;
        this.flight = flight;
        List<Passenger> passengers = new ArrayList<>();
        passengers.add(user.getPassenger());
        this.id = generateId(flight, passengers);
        this.passengers = passengers;
    }

    public Booking(Flight flight, List<Passenger> passengers, User user) {
        this.user = user;
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

    public User getUser() {
        return user;
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
        return Objects.equals(id, booking.id) && Objects.equals(passengers, booking.passengers) && Objects.equals(user, booking.user) && Objects.equals(flight, booking.flight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, passengers, flight);
    }

    @Override
    public String toString() {
        return "Booking{id: " + this.id + "}";
    }
}
