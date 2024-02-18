package users;

import bookings.Booking;
import passenger.Passenger;
import utils.interfaces.HasId;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

public class User implements Serializable, HasId {
    @Serial
    private static final long serialVersionUID = 1234567L;
    private String id;
    private String username;
    private String password;
    private List<Booking> bookings;
    private final Passenger passenger;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", bookings=" + bookings +
                ", passenger=" + passenger +
                '}';
    }
}
