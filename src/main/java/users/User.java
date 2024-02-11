package users;

import bookings.Booking;
import passenger.Passenger;
import utils.interfaces.HasId;

import java.io.Serializable;
import java.util.*;

public class User implements HasId, Serializable {
    private String id;
    private String username;
    private String password;
    private List<Booking> bookings;
    private Passenger passenger;

    public User(String id, String username, String password, Passenger passenger) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.bookings = new ArrayList<>();
        this.passenger = passenger;
    }

    public static String generateId() {
        return UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }

    public void addBooking(Booking booking) {
        bookings.add(booking);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        User user = (User) object;
        return Objects.equals(id, user.id) && Objects.equals(passenger, user.passenger);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, passenger);
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", booking=" + bookings +
                ", passenger=" + passenger +
                '}';
    }
}
