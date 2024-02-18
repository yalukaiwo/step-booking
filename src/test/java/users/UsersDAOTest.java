package users;

import bookings.Booking;
import org.junit.jupiter.api.*;

import java.io.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class UsersDAOTest {
    private final String fileName = "test.bin";
    private final UsersDAO usersDAO = new UsersDAO(new File(fileName));
    private final InputStream originalSystemIn = System.in;
    private ByteArrayInputStream inputStream;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    public void setUp() {
        redirectSystemInput("1\n9\n");
    }

    private void redirectSystemInput(String input) {
        inputStream = new ByteArrayInputStream(input.getBytes());
        outputStream = new ByteArrayOutputStream();
        System.setIn(inputStream);
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    public void tearDown() {
        restoreSystemInput();
        File testFile = new File(fileName);
        if (testFile.exists()) {
            testFile.delete();
        }
    }

    private void restoreSystemInput() {
        System.setIn(originalSystemIn);
    }

    User userWithBooking = UserFactory.createUserWithBooking();
    User user = UserFactory.createUser();

    @Test
    void saveAndReadUser() throws IOException {
        usersDAO.save(userWithBooking);
        Optional<User> savedUser = usersDAO.read(userWithBooking.getId());
        assertTrue(savedUser.isPresent());
        assertEquals(userWithBooking.getId(), savedUser.get().getId());
        assertEquals(1, savedUser.get().getBookings().size());
    }

    @Test
    void saveAll() throws IOException {
        List<User> users = new ArrayList<>();
        users.add(userWithBooking);
        users.add(userWithBooking);
        usersDAO.saveAll(users);
        List<User> savedUsers = usersDAO.readAll();
        assertEquals(users.size(), savedUsers.size());
        for (int i = 0; i < users.size(); i++) {
            assertEquals(users.get(i).getId(), savedUsers.get(i).getId());
            assertEquals(users.get(i).getBookings().size(), savedUsers.get(i).getBookings().size());
        }
    }

    @Test
    void updateUser() throws IOException {
        usersDAO.save(user);
        user.setUsername("newUsername");
        usersDAO.updateUser(user);
        Optional<User> updatedUser = usersDAO.read(user.getId());
        assertTrue(updatedUser.isPresent());
        assertEquals("newUsername", updatedUser.get().getUsername());
    }

    @Test
    void updatePassword() throws IOException {
        usersDAO.save(user);
        user = usersDAO.updatePassword(user.getUsername(), "newPassword");
        assertEquals("newPassword", user.getPassword());
    }

    @Test
    void delete() throws IOException {
        usersDAO.save(user);
        usersDAO.delete(user.getId());
        Optional<User> deletedUser = usersDAO.read(user.getId());
        assertTrue(deletedUser.isEmpty());
    }

    @Test
    void findUserBookingById() throws IOException {
        usersDAO.save(userWithBooking);
        Optional<Booking> foundBooking = usersDAO.findUserBookingById(userWithBooking, userWithBooking.getBookings().get(0).getId());
        assertTrue(foundBooking.isPresent());
        assertEquals(userWithBooking.getBookings().get(0), foundBooking.get());
    }

    @Test
    void deleteBooking() throws IOException {
        usersDAO.save(userWithBooking);
        String bookingId = userWithBooking.getBookings().get(0).getId();
        usersDAO.deleteBooking(userWithBooking, bookingId);
        Optional<Booking> deletedBooking = usersDAO.findUserBookingById(userWithBooking, bookingId);
        assertFalse(deletedBooking.isPresent());
    }

    @Test
    void getUserByUsername() throws IOException {
        usersDAO.save(user);
        User foundUser = usersDAO.getUserByUsername(user.getUsername());
        assertNotNull(foundUser);
        assertEquals(user.getUsername(), foundUser.getUsername());
    }

    @Test
    void readAll() throws IOException {
        List<User> users = new ArrayList<>();
        users.add(user);
        users.add(user);
        usersDAO.saveAll(users);
        List<User> allUsers = usersDAO.readAll();
        assertEquals(2, allUsers.size());
    }
}
