package flights;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Optional;

public class FlightsDaoTest {
    private final String fileName = "test.bin";
    private final FlightsDAO dao = new FlightsDAO(new File(fileName));
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
        Assertions.assertEquals(0, dao.readAll().size());
        dao.save(f);
        Assertions.assertEquals(1, dao.readAll().size());
        Assertions.assertEquals(f, dao.read(f.getId()).orElse(null));
        dao.delete(f.getId());
        Assertions.assertEquals(0, dao.readAll().size());
        Assertions.assertEquals(Optional.empty(), dao.read(f.getId()));

    }
}
