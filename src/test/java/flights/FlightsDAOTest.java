package flights;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;

public class FlightsDAOTest {
    @Test
    public void testReadSaveDelete() throws IOException {
        HashMap<String, Flight> bs = new HashMap<>();
        FlightsDAO dao = new FlightsDAO(bs);

        Assertions.assertTrue(dao.read("someID").isEmpty());
        dao.save(new Flight("Deli", 123, 3));
        Assertions.assertTrue(dao.read("f@kyiv_deli_123_3").isPresent());
        Assertions.assertTrue(dao.read("someOtherID2").isEmpty());
        Assertions.assertEquals(1, dao.readAll().size());
    }
}
