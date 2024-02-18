package bookings;

import flights.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.exceptions.BookingNotFoundException;

import java.io.*;
import java.util.ArrayList;
import java.util.Optional;

public class BookingsServiceTest {
    private final String fileName = "test.bin";
    private final BookingsDAO dao = new BookingsDAO(new File(fileName));
    private final BookingsService service = new BookingsService(dao);
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

    @Test
    public void testCreate() throws IOException {
        Flight f = new Flight(City.AMSTERDAM, City.NEW_YORK, Airline.DELTA, 123, 123, 123, 123);
        Assertions.assertEquals(0, dao.readAll().size());
        Booking b = service.create(f, new ArrayList<>());
        Assertions.assertEquals(1, dao.readAll().size());
        Assertions.assertEquals(b, dao.read(b.getId()).orElse(null));
    }

    @Test
    public void testDelete() throws IOException {
        Flight f = new Flight(City.AMSTERDAM, City.NEW_YORK, Airline.DELTA, 123, 123, 123, 123);
        Booking b = service.create(f, new ArrayList<>());
        Assertions.assertEquals(1, dao.readAll().size());
        Assertions.assertEquals(b, dao.read(b.getId()).orElse(null));
        service.delete(b);
        Assertions.assertEquals(0, dao.readAll().size());
        Assertions.assertEquals(Optional.empty(), dao.read(f.getId()));
    }

    @Test
    public void testGetById() throws IOException {
        Flight f = new Flight(City.AMSTERDAM, City.NEW_YORK, Airline.DELTA, 123, 123, 123, 123);
        Booking b = service.create(f, new ArrayList<>());
        Booking found;
        try {
            found = service.getById(b.getId());
        } catch (BookingNotFoundException e) {
            throw new RuntimeException("");
        }

        Assertions.assertEquals(b, found);
    }

    @Test
    public void testSave() throws IOException {
        Flight f = new Flight(City.AMSTERDAM, City.NEW_YORK, Airline.DELTA, 123, 123, 123, 123);
        Booking b = new Booking(f, new ArrayList<>());
        Assertions.assertEquals(0, dao.readAll().size());
        service.save(b);
        Assertions.assertEquals(1, dao.readAll().size());
        Assertions.assertEquals(b, dao.read(b.getId()).orElse(null));
    }
}
