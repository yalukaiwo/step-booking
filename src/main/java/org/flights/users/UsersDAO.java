package org.flights.users;

import org.flights.utils.exceptions.UserNotFoundException;
import org.flights.workers.*;
import org.flights.utils.interfaces.DAO;
import org.flights.logger.LoggerService;

import java.io.File;
import java.io.IOException;
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

    // FOR TESTING PURPOSES
    public UsersDAO(HashMap<String, User> us) {
        this.worker = new MapWorker<>(us);
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

    public void updateUser(User updatedUser) throws IOException, UserNotFoundException {
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
        } else {
            LoggerService.error("User not found with ID: " + updatedUser.getId());
            throw new UserNotFoundException();
        }
    }
}
