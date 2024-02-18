package users;

import bookings.Booking;
import workers.*;
import utils.interfaces.DAO;
import logger.LoggerService;

import java.io.*;
import java.util.*;

public class UsersDAO implements DAO<User> {
    private final DataWorker<User> worker;

    public UsersDAO(File f) {
        this.worker = new FileWorker<>(f);
    }

    public Optional<User> read(String id) throws IOException {
        try {
            LoggerService.info("Attempting to read user with id: " + id);
            List<User> bs = worker.readAll();
            Optional<User> userOptional = bs.stream().filter(b -> Objects.equals(b.getId(), id)).findFirst();
            if (userOptional.isPresent()) {
                LoggerService.info("User with id " + id + " was found.");
            } else {
                LoggerService.info("User with id " + id + " was not found.");
            }
            return userOptional;
        } catch (IOException e) {
            LoggerService.error("Error occurred while reading user with id: " + id);
            throw e;
        }
    }

    public void save(User u) throws IOException {
        ArrayList<User> us = new ArrayList<>(worker.readAll());
        us.remove(u);
        us.add(u);
        worker.saveAll(us);
        LoggerService.info("User saved successfully: " + u);
    }

    public void saveAll(List<User> users) throws IOException {
        worker.saveAll(users);
        LoggerService.info("Users saved successfully: " + users);
    }

    public void delete(String id) throws IOException {
        try {
            List<User> bs = worker.readAll().stream().filter(b -> !b.getId().equals(id)).toList();
            worker.saveAll(bs);
            LoggerService.info("User with ID " + id + " deleted successfully");
        } catch (IOException e) {
            LoggerService.error("Error occurred while deleting user with id: " + id);
            throw e;
        }
    }

    public List<User> readAll() throws IOException {
        try {
            List<User> allUsers = worker.readAll();
            LoggerService.info("All users retrieved successfully");
            return allUsers;
        } catch (IOException e) {
            LoggerService.error("Error occurred while reading all users");
            throw e;
        }
    }

    public User updateUser(User updatedUser) throws IOException {
        List<User> allUsers = worker.readAll();

        int index = -1;
        for (int i = 0; i < allUsers.size(); i++) {
            if (allUsers.get(i).getId().equals(updatedUser.getId())) {
                index = i;
                break;
            }
        }

        if (index != -1) {
            allUsers.set(index, updatedUser);
            worker.saveAll(allUsers);
            LoggerService.info("User updated successfully: " + updatedUser);
            return updatedUser;
        } else {
            LoggerService.error("User not found with ID: " + updatedUser.getId());
            return null;
        }
    }

    public User updatePassword(String username, String newPassword) throws IOException {
        User user = getUserByUsername(username);
        if (user != null) {
            user.setPassword(newPassword);
            save(user);
            return user;
        }
        return null;
    }

    public User getUserByUsername(String username) throws IOException {
        return readAll()
                .stream()
                .filter(u -> u.getUsername() != null && u.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    public Optional<Booking> findUserBookingById(User user, String bookingId) {
        return user.getBookings().stream()
                .filter(booking -> booking.getId().equals(bookingId))
                .findAny();
    }

    public void deleteBooking(User user, String bookingId) throws IOException {
        Optional<Booking> bookingToRemove = findUserBookingById(user, bookingId);
        if (bookingToRemove.isPresent()) {
            Booking bookingToDelete = bookingToRemove.get();
            boolean removed = user.getBookings().remove(bookingToDelete);
            if (removed) {
                save(user);
                LoggerService.info("Booking with ID " + bookingId + " deleted successfully for user " + user.getId());
            } else {
                LoggerService.error("Failed to delete booking with ID " + bookingId + " for user " + user.getId());
            }
        } else {
            LoggerService.error("Booking not found with ID: " + bookingId + " for user " + user.getId());
        }
    }
}
