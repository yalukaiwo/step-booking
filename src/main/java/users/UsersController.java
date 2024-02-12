package users;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class UsersController {
    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    public Optional<User> read(String id) throws IOException {
        return usersService.read(id);
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

    public User getByUserName(String login) throws IOException {
        return usersService.getByUserName(login);
    }

    public User getById(String id) throws IOException {
        return usersService.getById(id);
    }

    public void addUser(User u) {
        usersService.addUser(u);
    }

    public Optional<User> authenticate(String username, String password) {
        return usersService.authenticate(username, password);
    }

    public boolean userExists(String username) throws IOException {
        return readAll().stream().anyMatch(user -> user.getUsername().equals(username));
    }

    public boolean userExists(String username, String password) throws IOException {
        return readAll().stream()
                .anyMatch(user -> user.getUsername().equals(username) && user.getPassword().equals(password));
    }
}
