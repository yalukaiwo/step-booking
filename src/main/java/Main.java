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
import utils.exceptions.FlightNotFoundException;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        UsersController usersController = new UsersController(new UsersService(new UsersDAO(new File("users.bin"))));
        BookingsController bookingsController = new BookingsController(new BookingsService(new BookingsDAO(new File("bookings.bin"))));
        FlightsController flightsController = new FlightsController(new FlightsService(new FlightsDAO(new File("flights.bin"))));

        ConsoleApp consoleApp = new ConsoleApp(usersController, bookingsController, flightsController);
        try {System.out.println(flightsController.getById("ab2b352c-3a10-41e6-87ee-a24a8a6771a"));} catch (FlightNotFoundException e) {
            System.out.println("NF");
        }
        consoleApp.start();
    }
}
