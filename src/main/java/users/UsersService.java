package users;

import java.io.IOException;
import java.util.*;

public class UsersService {
    private UsersDAO userDao;

    public UsersService(UsersDAO userDAO) {
        this.userDao = userDAO;
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

    public Optional<User> getById(String id) throws IOException {
        return userDao.read(id);
    }

    public Optional<User> authenticate(String username, String password) throws IOException {
        // possibly, you could throw a UserNotFound exception if user.isEmpty(). (.findFirst().orElseThrow())
        List<User> us = userDao.readAll();
        Optional<User> user = us.stream().filter(u -> u.getUsername().equals(username)).findFirst();
        if (user.isPresent() && user.get().getPassword().equals(password)) {
            return user;
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