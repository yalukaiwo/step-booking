package users;

import flights.Airline;
import flights.City;
import passenger.Passenger;
import users.User;
import bookings.Booking;
import flights.Flight;

import java.util.ArrayList;
import java.util.List;

public class UserFactory {

    public static User createUser() {
        return new User(User.generateId(), "testUser", "password", new Passenger("Max", "Zymyn"));
    }

    public static User createUserWithBooking() {
        User user = createUser();
        List<Passenger> passengers = new ArrayList<>();
        passengers.add(new Passenger("Luka", "Ponomarenko"));
        Flight flight = new Flight(City.BEIJING, City.ISTANBUL, Airline.AIR_SEUL, 100.0, System.currentTimeMillis(), System.currentTimeMillis() + 100000, 100);
        Booking booking = new Booking(flight, passengers);
        user.getBookings().add(booking);
        return user;
    }
}
