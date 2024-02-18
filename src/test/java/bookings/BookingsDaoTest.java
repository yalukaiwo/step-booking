package bookings;

import flights.Airline;
import flights.City;
import flights.Flight;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.Optional;

public class BookingsDaoTest {
    private final String fileName = "test.bin";
    private final BookingsDAO dao = new BookingsDAO(new File(fileName));
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
    public void testSaveReadDelete() throws IOException {
        Flight f = new Flight(City.AMSTERDAM, City.BEIJING, Airline.DELTA, 123, 123, 123, 1234);
        Booking b = new Booking(f, new ArrayList<>());
        Assertions.assertEquals(0, dao.readAll().size());
        dao.save(b);
        Assertions.assertEquals(1, dao.readAll().size());
        Assertions.assertEquals(b, dao.read(b.getId()).orElse(null));
        dao.delete(b.getId());
        Assertions.assertEquals(0, dao.readAll().size());
        Assertions.assertEquals(Optional.empty(), dao.read(b.getId()));

    }
}
