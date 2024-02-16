package org.flights.users;

import org.flights.bookings.Booking;
import org.flights.utils.exceptions.UserNotFoundException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

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

    public boolean updatePassword(String username, String newPassword) {
       return usersService.updatePassword(username, newPassword);
    }

    public void updateUser(User updatedUser) throws IOException, UserNotFoundException {
        usersService.updateUser(updatedUser);
    }

    public Optional<Booking> findUserBookingById(User user, String bookingId) {
        return usersService.findUserBookingById(user, bookingId);
    }

    public boolean deleteBooking(User user, String bookingId) {
        return usersService.deleteBooking(user, bookingId);
    }

    public void addBooking(User user, Booking booking) {
        usersService.addBooking(user, booking);
    }
}
