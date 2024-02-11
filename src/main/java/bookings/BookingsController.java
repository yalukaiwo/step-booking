package bookings;

import flights.Flight;
import flights.FlightsController;
import passenger.Passenger;
import utils.exceptions.BookingNotFoundException;
import utils.exceptions.FlightNotFoundException;
import utils.exceptions.PassengerOverflowException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BookingsController {
    private final BookingsService service;

    public BookingsController(BookingsService service) {
        this.service = service;
    }

    public Optional<Booking> create(Flight flight, List<Passenger> passengers) throws IOException {
        try {
            flight.incrementPassengers(1); // Increment passengers by 1 for the single user
            return Optional.of(service.create(flight, passengers)); // Create booking with single user
        } catch (PassengerOverflowException e) {
            return Optional.empty();
        }
    }

    public Optional<Booking> create(Flight flight, Passenger... passengers) throws IOException {
        try {
            flight.incrementPassengers(passengers.length); // Increment passengers by the number of provided passengers
            return Optional.of(service.create(flight, List.of(passengers))); // Create booking with provided passengers
        } catch (PassengerOverflowException e) {
            return Optional.empty();
        }
    }

    public void delete(Booking b, FlightsController fc) throws IOException {
        try {
            fc.getById(b.getFlight().getId()).decrementPassengers(b.getPassengers().size());
            service.delete(b);
        } catch (PassengerOverflowException e) {
            throw new RuntimeException(e);
        } catch (FlightNotFoundException e) {
            service.delete(b);
        }
    }

    public void delete(String id, FlightsController fc) throws IOException {
        try {
            Booking b = service.getById(id);
            fc.getById(b.getFlight().getId()).decrementPassengers(b.getPassengers().size());
            service.delete(id);
        } catch (PassengerOverflowException e) {
            throw new RuntimeException(e);
        } catch (FlightNotFoundException e) {
            service.delete(id);
        } catch (BookingNotFoundException e) {
            // do nothing
        }
    }

    public Booking getById(String id) throws IOException, BookingNotFoundException {
        return service.getById(id);
    }

    public List<Booking> getById(List<String> ids) throws IOException {
        return service.getAll().stream().filter(b -> ids.contains(b.getId())).toList();
    }

    public List<Booking> getAll() throws IOException {
        return service.getAll();
    }

    public void clear() throws IOException {
        service.clear();
    }
}
