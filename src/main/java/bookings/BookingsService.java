package bookings;

import flights.Flight;
import passenger.Passenger;
import users.User;
import utils.exceptions.BookingNotFoundException;
import utils.exceptions.PassengerOverflowException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class BookingsService {
    private final BookingsDAO db;

    public BookingsService(BookingsDAO dao) {
        this.db = dao;
    }

    public Booking create(Flight flight, User user, List<Passenger> passengers) throws IOException {
        Booking b = new Booking(flight, passengers, user);
        db.save(b);
        return b;
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

    public List<Booking> getAll() throws IOException {
        return db.readAll();
    }

    public void clear() throws IOException {
        List<Booking> fs = db.readAll();
        for (Booking f : fs) {
            db.delete(f.getId());
        }
    }

}
