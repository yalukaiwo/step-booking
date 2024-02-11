package users;

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

    public User getByUserName(String login) throws IOException {
        return usersService.getByUserName(login);
    }

    // read = getById
    public Optional<User> getById(String id) throws IOException {
        return usersService.getById(id);
    }

    public Optional<User> authenticate(String username, String password) throws IOException {
        return usersService.authenticate(username, password);
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
