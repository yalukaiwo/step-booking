package org.flights.users;

import org.flights.bookings.Booking;
import org.flights.logger.LoggerService;
import org.flights.utils.exceptions.UserNotFoundException;

import java.io.IOException;
import java.util.*;

public class UsersService {
    private final UsersDAO userDao;

    public UsersService(UsersDAO userDAO) {
        this.userDao = userDAO;
    }

    public void save(User u) throws IOException {
        List<User> users = readAll();
        users.add(u);
        userDao.saveAll(users);
    }

    public void delete(String id) throws IOException {
        userDao.delete(id);
    }

    public List<User> readAll() throws IOException {
        return userDao.readAll();
    }

    public User getByUserName(String login) throws IOException {
        return readAll()
                .stream()
                .filter(u -> u.getUsername() != null && u.getUsername().equals(login))
                .findFirst()
                .orElse(null);
    }

    public User authenticate(String username, String password) throws IOException {
        User user = getByUserName(username);
        if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public User getUserByUsername(String username) throws IOException {
        List<User> userList = readAll();
        for (User user : userList) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public boolean updatePassword(String username, String newPassword) {
        try {
            User user = getByUserName(username);
            if (user != null) {
                user.setPassword(newPassword);
                userDao.save(user);
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void updateUser(User updatedUser) throws IOException, UserNotFoundException {
        userDao.updateUser(updatedUser);
    }

    public Optional<Booking> findUserBookingById(User user, String bookingId) {
        return user.getBookings().stream()
                .filter(booking -> booking.getId().equals(bookingId))
                .findFirst();
    }

    public boolean deleteBooking(User user, String bookingId) {
        Optional<Booking> bookingToRemove = findUserBookingById(user, bookingId);
        Booking bookingToDelete = bookingToRemove.get();
        if (bookingToDelete != null) {
            return user.getBookings().remove(bookingToRemove.get());
        }
        return false;
    }

    public void addBooking(User user, Booking booking) {
        user.getBookings().add(booking);
    }
}