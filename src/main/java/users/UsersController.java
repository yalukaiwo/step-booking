package users;

import bookings.Booking;

import java.io.IOException;
import java.util.*;

public class UsersController {
    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    public void save(User u) throws IOException {
        usersService.save(u);
    }

    public void delete(String id) throws IOException {
        usersService.delete(id);
    }

    public List<User> readAll() throws IOException {
        return usersService.readAll();
    }

    public User authenticate(String username, String password) throws IOException {
        return usersService.authenticate(username, password);
    }

    public boolean userExists(String username) throws IOException {
        return readAll().stream().anyMatch(user -> user.getUsername().equals(username));
    }

    public User getUserByUsername(String username) throws IOException {
        return usersService.getUserByUsername(username);
    }

    public User updateUser(User updatedUser) throws IOException {
        return usersService.updateUser(updatedUser);
    }

    public User updatePassword(String username, String newPassword) throws IOException {
        return usersService.updatePassword(username, newPassword);
    }

    public Optional<Booking> findUserBookingById(User user, String bookingId) {
        return usersService.findUserBookingById(user, bookingId);
    }

    public void deleteBooking(User user, String bookingId) throws IOException {
        usersService.deleteBooking(user, bookingId);
    }

    public void addBooking(User user, Booking booking) {
        usersService.addBooking(user, booking);
    }


}
