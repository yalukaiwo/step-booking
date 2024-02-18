package users;

import bookings.Booking;
import flights.*;
import org.junit.jupiter.api.*;
import passenger.Passenger;

import java.io.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class UsersControllerTest {
    private final String fileName = "test.bin";
    private final UsersDAO usersDAO = new UsersDAO(new File(fileName));
    private final UsersService usersService = new UsersService(usersDAO);
    private final UsersController usersController = new UsersController(usersService);

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
    void save() throws IOException {
        List<User> users = new ArrayList<>();
        users.add(user);
        usersController.save(user);
        List<User> savedUsers = usersController.readAll();
        assertEquals(users.size(), savedUsers.size());
        assertEquals(users.get(0), savedUsers.get(0));
    }

    @Test
    void delete() throws IOException {
        usersController.save(user);
        usersController.delete(user.getId());
        List<User> users = usersController.readAll();
        assertTrue(users.isEmpty());
    }

    @Test
    void getByUserName() throws IOException {
        String username = "testUser";
        User user = new User("1", username, "password", new Passenger("Max", "Zymyn"));
        usersController.save(user);
        User foundUser = usersController.getUserByUsername(username);
        assertNotNull(foundUser);
        assertEquals(user, foundUser);
    }

    @Test
    void authenticate_ValidCredentials_ReturnsUser() throws IOException {
        String username = "testUser";
        String password = "password";
        User user = new User("1", username, password, new Passenger("Max", "Zymyn"));
        usersController.save(user);
        User authenticatedUser = usersController.authenticate(username, password);
        assertNotNull(authenticatedUser);
        assertEquals(user, authenticatedUser);
    }

    @Test
    void authenticate_InvalidCredentials_ReturnsNull() throws IOException {
        String username = "testUser";
        String password = "password";
        User user = new User("1", username, password, new Passenger("Max", "Zymyn"));
        usersController.save(user);
        User authenticatedUser = usersController.authenticate(username, "wrongPassword");
        assertNull(authenticatedUser);
    }

    @Test
    void updateUser() throws IOException {
        usersController.save(user);

        user.setUsername("newUsername");
        usersController.updateUser(user);

        User updatedUser = usersController.getUserByUsername("newUsername");
        assertEquals("newUsername", user.getUsername());
        assertEquals(user, updatedUser);
    }

    @Test
    void readAll() throws IOException {
        usersController.save(user);
        usersController.save(user);
        List<User> allUsers = usersController.readAll();
        assertEquals(2, allUsers.size());
    }

    @Test
    void updatePassword() throws IOException {
        usersController.save(user);
        user = usersController.updatePassword(user.getUsername(), "newPassword");
        assertEquals("newPassword", user.getPassword());
    }

    @Test
    void findUserBookingById() throws IOException {
        usersController.save(userWithBooking);
        Optional<Booking> foundBooking = usersController.findUserBookingById(userWithBooking, userWithBooking.getBookings().get(0).getId());
        assertTrue(foundBooking.isPresent());
        assertEquals(userWithBooking.getBookings().get(0), foundBooking.get());
    }

    @Test
    void deleteBooking() throws IOException {
        usersController.save(userWithBooking);
        String bookingId = userWithBooking.getBookings().get(0).getId();
        usersController.deleteBooking(userWithBooking, bookingId);
        Optional<Booking> deletedBooking = usersController.findUserBookingById(userWithBooking, bookingId);
        assertFalse(deletedBooking.isPresent());
    }

    @Test
    void addBooking() {
        List<Passenger> passengers = new ArrayList<>();
        passengers.add(new Passenger("Luka", "Ponomarenko"));
        Flight flight = new Flight(City.KYIV, City.TORONTO, Airline.AZAL, 100.0, System.currentTimeMillis(), System.currentTimeMillis() + 100000, 100);
        Booking booking = new Booking(flight, passengers);

        usersController.addBooking(user, booking);

        assertEquals(1, user.getBookings().size());
        assertTrue(user.getBookings().contains(booking));
    }

    @Test
    void userExists_UserExists_ReturnsTrue() throws IOException {
        String existingUsername = "existingUser";
        usersController.save(new User("1", existingUsername, "password", null));
        boolean exists = usersController.userExists(existingUsername);
        assertTrue(exists);
    }

    @Test
    void userExists_UserDoesNotExist_ReturnsFalse() throws IOException {
        String nonExistingUsername = "nonExistingUser";
        boolean exists = usersController.userExists(nonExistingUsername);
        assertFalse(exists);
    }
}
