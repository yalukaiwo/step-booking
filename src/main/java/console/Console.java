package console;

import bookings.BookingsController;
import flights.FlightsController;

public class Console {
    private final UserController userController;
    private final BookingsController bookingsController;
    private final FlightsController flightsController;


    public Console(UserController userController, BookingsController bookingsController,FlightsController flightsController) {
        this.userController = userController;
        this.bookingsController = bookingsController;
        this.flightsController = flightsController;
    }



}
