import bookings.BookingsController;
import bookings.BookingsDAO;
import bookings.BookingsService;
import console.ConsoleApp;
import flights.FlightsController;
import flights.FlightsDAO;
import flights.FlightsService;
import users.UsersController;
import users.UsersDAO;
import users.UsersService;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        UsersController usersController = new UsersController(new UsersService(new UsersDAO(new File("users.bin"))));
        BookingsController bookingsController = new BookingsController(new BookingsService(new BookingsDAO(new File("bookings.bin"))));
        FlightsController flightsController = new FlightsController(new FlightsService(new FlightsDAO(new File("flights.bin"))));
        ConsoleApp consoleApp = new ConsoleApp(usersController, bookingsController, flightsController);
        consoleApp.start();
    }
}
