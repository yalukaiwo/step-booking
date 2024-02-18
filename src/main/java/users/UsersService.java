package users;

import bookings.Booking;
import java.io.IOException;
import java.util.*;

public class UsersService {
    private final UsersDAO usersDao;

    public UsersService(UsersDAO userDAO) {
        this.usersDao = userDAO;
    }

    public void save(User u) throws IOException {
        List<User> users = readAll();
        users.add(u);
        usersDao.saveAll(users);
    }

    public void delete(String id) throws IOException {
        usersDao.delete(id);
    }

    public List<User> readAll() throws IOException {
        return usersDao.readAll();
    }

    public User authenticate(String username, String password) throws IOException {
        User user = getUserByUsername(username);
        if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }
  
    public User getUserByUsername(String username) throws IOException {
        return usersDao.getUserByUsername(username);
    }

    public User updateUser(User updatedUser) throws IOException {
        return usersDao.updateUser(updatedUser);
    }

    public User updatePassword(String username, String newPassword) throws IOException {
        return usersDao.updatePassword(username, newPassword);
    }

    public Optional<Booking> findUserBookingById(User user, String bookingId) {
        return usersDao.findUserBookingById(user, bookingId);
    }

    public void deleteBooking(User user, String bookingId) throws IOException {
        usersDao.deleteBooking(user, bookingId);
    }

    public void addBooking(User user, Booking booking) {
        user.getBookings().add(booking);
    }
}
