package bookings;

import flights.Flight;
import passenger.Passenger;
import utils.exceptions.BookingNotFoundException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class BookingsService {
    private final BookingsDAO db;

    public BookingsService(BookingsDAO dao) {
        this.db = dao;
    }

    public Booking create(Flight flight, List<Passenger> passengers) throws IOException {
        Booking b = new Booking(flight, passengers);
        db.save(b);
        return b;
    }

    public void save(Booking b) throws IOException {
        db.save(b);
    }

    public void delete(Booking b) throws IOException {
        db.delete(b.getId());
    }

    public void delete(String id) throws IOException {
        db.delete(id);
    }

    public Booking getById(String id) throws IOException, BookingNotFoundException {
        Optional<Booking> b = db.read(id);
        if (b.isEmpty()) throw new BookingNotFoundException();

        return b.get();
    }
}
