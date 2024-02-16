package org.flights;

import org.flights.bookings.*;
import org.flights.console.ConsoleApp;
import org.flights.flights.*;
import org.flights.users.*;
import org.flights.utils.exceptions.UserNotFoundException;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, UserNotFoundException {
        UsersController usersController = new UsersController(new UsersService(new UsersDAO(new File("users.bin"))));
        BookingsController bookingsController = new BookingsController(new BookingsService(new BookingsDAO(new File("bookings.bin"))));
        FlightsController flightsController = new FlightsController(new FlightsService(new FlightsDAO(new File("flights.bin"))));

        ConsoleApp consoleApp = new ConsoleApp(usersController, bookingsController, flightsController);
        consoleApp.generateRandomFlights(20);
        consoleApp.start();
    }
}
