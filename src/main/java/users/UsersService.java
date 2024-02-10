package users;

import java.io.IOException;
import java.util.*;

public class UsersService {
    private UsersDAO userDao;

    public UsersService(UsersDAO userDAO) {
        this.userDao = userDAO;
    }

    public Optional<User> read(String id) throws IOException {
        return userDao.read(id);
    }

    public void save(User u) throws IOException {
        userDao.save(u);
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

    public User getById(String id) throws IOException {
        return readAll()
                .stream()
                .filter(u -> u.getId() != null && u.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void addUser(User u) {
        Map<String, User> us = new HashMap<>();
        us.put(u.getUsername(), u);
    }

    public Optional<User> authenticate(String username, String password) {
        Map<String, User> users = new HashMap<>();
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            return Optional.of(user);
        } else {
            return Optional.empty();
        }
    }

    public boolean userExists(String username) {
        try {
            return readAll().stream().anyMatch(user -> user.getUsername().equals(username));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}