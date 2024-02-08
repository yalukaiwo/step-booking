package Bookings;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;

public class BookingsDAOTest {
    @Test
    public void testReadSaveDelete() throws IOException {
        HashMap<String, Booking> bs = new HashMap<>();
        BookingsDAO dao = new BookingsDAO(bs);

        Assertions.assertTrue(dao.read("someID").isEmpty());
        dao.save(new Booking("someOtherID"));
        Assertions.assertTrue(dao.read("someOtherID").isPresent());
        Assertions.assertTrue(dao.read("someOtherID2").isEmpty());
        Assertions.assertEquals(1, dao.readAll().size());
    }
}
