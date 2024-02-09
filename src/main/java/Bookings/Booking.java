package Bookings;

import Flights.Flight;
import Person.Person;
import Utils.Interfaces.HasId;

import java.io.Serializable;
import java.util.List;

public class Booking implements HasId, Serializable {
    private final String id;
    private final String flightId;
    private final List<Person> passengers;

    public static String generateId(Flight flight, List<Person> passengers) {
        return "b@" + flight.getOrigin().trim().toLowerCase().split(" ")[0] + "_" + flight.getDestination().trim().toLowerCase().split(" ")[0] + "_" + flight.getDepartureTime() + "_" + flight.getMaxPassengers() + "_" + flight.getPassengers() + "_" + passengers.size();
    }

    public Booking(Flight flight, List<Person> passengers) {
        this.id = generateId(flight, passengers);
        this.passengers = passengers;
        this.flightId = flight.getId();
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
    public String toString() {
        return "Booking{id: " + this.id + "}";
    }
}
