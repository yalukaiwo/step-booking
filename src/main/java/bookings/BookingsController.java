package bookings;

import flights.Flight;
import utils.exceptions.*;
import passenger.Passenger;

import java.io.IOException;
import java.util.*;

public class BookingsController {
    private final BookingsService service;

    public BookingsController(BookingsService service) {
        this.service = service;
    }

    public Optional<Booking> create(Flight flight, List<Passenger> passengers) throws IOException {
        try {
            flight.incrementPassengers(passengers.size());
            return Optional.of(service.create(flight, passengers));
        } catch (PassengerOverflowException e) {
            return Optional.empty();
        }
    }

    public void delete(Booking b) throws IOException {
        try {
            b.getFlight().decrementPassengers(b.getPassengers().size());
            service.delete(b);
        } catch (PassengerOverflowException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(String id) throws IOException {
        try {
            Booking b = service.getById(id);
            b.getFlight().decrementPassengers(b.getPassengers().size());
            service.delete(id);
        } catch (PassengerOverflowException e) {
            throw new RuntimeException(e);
        } catch (BookingNotFoundException e) {
            // do nothing
        }
    }

    public Booking getById(String id) throws IOException, BookingNotFoundException {
        return service.getById(id);
    }

    public void save(List<Booking> bs) throws IOException {
        for (Booking b : bs) {
            service.save(b);
        }
    }
}
