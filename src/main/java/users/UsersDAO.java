package users;

import workers.DataWorker;
import workers.FileWorker;
import workers.MapWorker;
import utils.interfaces.DAO;
import logger.LoggerService;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class UsersDAO implements DAO<User> {
    private final DataWorker<User> worker;

    public DataWorker<User> getWorker() {
        return worker;
    }

    public UsersDAO(File f) {
        this.worker = new FileWorker<>(f);
    }

    // FOR TESTING PURPOSES
    public UsersDAO(HashMap<String, User> us) {
        this.worker = new MapWorker<>(us);
    }

    public Optional<User> read(String id) throws IOException {
        try {
            LoggerService.info("Attempting to read user with id: " + id);
            List<User> bs = worker.readAll();
            Optional<User> userOptional = bs.stream().filter(b -> Objects.equals(b.getId(), id)).findFirst();
            if(userOptional.isPresent()) {
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
        try {
            LoggerService.info("Saving user: " + u);
            ArrayList<User> us = new ArrayList<>(worker.readAll());
            us.remove(u);
            us.add(u);
            worker.saveAll(us);
            LoggerService.info("User saved successfully: " + u);
        } catch (IOException e) {
            LoggerService.error("Error occurred while saving user: " + u);
            throw e;
        }
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
}