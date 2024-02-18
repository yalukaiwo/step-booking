package flights;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.exceptions.FlightNotFoundException;

import java.io.*;
import java.util.List;
import java.util.Optional;

public class FlightsControllerTest {
    private final String fileName = "test.bin";
    private final FlightsDAO dao = new FlightsDAO(new File(fileName));
    private final FlightsService service = new FlightsService(dao);
    private final FlightsController controller = new FlightsController(service);
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
        Assertions.assertEquals(0, dao.readAll().size());
        Flight f = controller.create(City.AMSTERDAM, City.NEW_YORK, Airline.DELTA, 123, 123, 123, 123);
        Assertions.assertEquals(1, dao.readAll().size());
        Assertions.assertEquals(f, dao.read(f.getId()).orElse(null));
    }

    @Test
    public void testDelete() throws IOException {
        Flight f = controller.create(City.AMSTERDAM, City.NEW_YORK, Airline.DELTA, 123, 123, 123, 123);
        Assertions.assertEquals(1, dao.readAll().size());
        Assertions.assertEquals(f, dao.read(f.getId()).orElse(null));
        controller.delete(f);
        Assertions.assertEquals(0, dao.readAll().size());
        Assertions.assertEquals(Optional.empty(), dao.read(f.getId()));
    }

    @Test
    public void testGetById() throws IOException {
        Flight f = controller.create(City.AMSTERDAM, City.NEW_YORK, Airline.DELTA, 123, 123, 123, 123);
        Flight found;
        try {
            found = controller.getById(f.getId());
        } catch (FlightNotFoundException e) {
            throw new RuntimeException("");
        }

        Assertions.assertEquals(f, found);
    }

    @Test
    public void testSave() throws IOException {
        Flight f = new Flight(City.AMSTERDAM, City.NEW_YORK, Airline.DELTA, 123, 123, 123, 123);
        Assertions.assertEquals(0, dao.readAll().size());
        controller.save(f);
        Assertions.assertEquals(1, dao.readAll().size());
        Assertions.assertEquals(f, dao.read(f.getId()).orElse(null));
    }

    @Test
    public void testSaveAll() throws IOException {
        Flight f = new Flight(City.AMSTERDAM, City.NEW_YORK, Airline.DELTA, 123, 123, 123, 123);
        Assertions.assertEquals(0, dao.readAll().size());
        controller.save(List.of(f));
        Assertions.assertEquals(1, dao.readAll().size());
        Assertions.assertEquals(f, dao.read(f.getId()).orElse(null));
    }

    @Test
    public void testSearch() throws IOException {
        Flight f = new Flight(City.AMSTERDAM, City.NEW_YORK, Airline.DELTA, 123, 123, 123, 123);
        Flight f1 = new Flight(City.AMSTERDAM, City.BEIJING, Airline.DELTA, 123, 123, 123, 123);
        controller.save(List.of(f, f1));
        List<Flight> fs = controller.search(City.AMSTERDAM, City.NEW_YORK);
        Assertions.assertEquals(1, fs.size());
    }

    @Test
    public void testFindConnectingFlights() throws IOException {
        Flight f = new Flight(City.AMSTERDAM, City.NEW_YORK, Airline.DELTA, 123, 100, 200, 123);
        Flight f1 = new Flight(City.NEW_YORK, City.BEIJING, Airline.DELTA, 123, 3600*1001, 3600*1002, 123);
        controller.save(List.of(f, f1));
        List<Flight[]> fs = controller.findConnectingFlights(dao.readAll(), City.AMSTERDAM, City.BEIJING);
        Assertions.assertEquals(1, fs.size());
    }
}
