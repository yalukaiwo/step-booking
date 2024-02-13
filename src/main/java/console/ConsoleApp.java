package console;

import bookings.Booking;
import bookings.BookingsController;
import console.colored_console.Ansi;
import flights.City;
import flights.Flight;
import flights.FlightsController;
import passenger.Passenger;
import users.User;
import users.UsersController;
import utils.exceptions.FlightNotFoundException;
import utils.exceptions.InvalidPasswordException;
import utils.exceptions.UserRegisterException;

import java.io.IOException;
import java.util.*;

public class ConsoleApp {
    private static final Scanner scanner = new Scanner(System.in);
    private final UsersController usersController;
    private final BookingsController bookingsController;
    private final FlightsController flightsController;
    private final OperationApp[] operations;
    private final MenuHelper menuDisplayHelper;
    private Optional<User> currentUser;

    public ConsoleApp(UsersController usersController, BookingsController bookingsController, FlightsController flightsController) {
        this.usersController = usersController;
        this.bookingsController = bookingsController;
        this.flightsController = flightsController;
        this.currentUser = Optional.empty();
        this.menuDisplayHelper = new MenuHelper();

        this.operations = new OperationApp[]{
                new OperationApp("View Timetable", this::viewTimetable),
                new OperationApp("View Flight Details", this::viewFlightDetails),
                new OperationApp("Search and Book Flights", this::searchAndBookFlights),
                // new OperationApp("Show User Bookings", this::showUserBookings),
                new OperationApp("Cancel Booking", this::cancelBooking),
                new OperationApp("Logout", this::exit)
        };
    }

    public void start() throws IOException {
        menuDisplayHelper.visitorBoard();

        boolean sessionActive = true;

        while (sessionActive) {
            try {
                System.out.print(MenuHelper.colorize(">>> Enter your choice: ", MenuHelper.magentaAttribute));
                String input = scanner.nextLine();

                if (input.isEmpty()) {
                    throw new InputMismatchException("Invalid input. Please enter a valid integer.");
                }

                int choice = Integer.parseInt(input);

                switch (choice) {
                    case 1:
                        login();
                        break;
                    case 2:
                        signUp();
                        break;
                    case 3:
                        exit();
                        sessionActive = false;
                        break;
                    default:
                        System.out.println(MenuHelper.colorize("Invalid choice. Please try again.", MenuHelper.redAttribute));
                }
            } catch (NumberFormatException e) {
                System.out.println(MenuHelper.colorize("Invalid input. Please enter a valid integer.", MenuHelper.redAttribute));
            } catch (InputMismatchException e) {
                System.out.println(MenuHelper.colorize(e.getMessage(), MenuHelper.redAttribute));
            }
        }
    }

    public void login() throws IOException {
        System.out.println(MenuHelper.colorize("Welcome to the Flight Reservation System!", MenuHelper.yellowAttribute));
        System.out.println("-------------------------------------------");
        System.out.println(MenuHelper.colorize("Please log in to continue.", MenuHelper.yellowAttribute) + Ansi.RESET);
        System.out.println();
        System.out.print(MenuHelper.colorize("Enter your username: ", MenuHelper.blueBoldBackAttribute));
        String username = scanner.next().trim().toLowerCase();
        username = Character.toUpperCase(username.charAt(0)) + username.substring(1);
        System.out.print(MenuHelper.colorize("Enter your password: ", MenuHelper.blueBoldBackAttribute));
        String password = scanner.next().trim();
        System.out.println();
        System.out.println(MenuHelper.colorize("Authenticating...", MenuHelper.cyanAttribute) + Ansi.RESET);
        System.out.println();
        if (usersController.userExists(username, password)) {
            usersController.authenticate(username, password);
            System.out.println(MenuHelper.colorize("You have successfully logged in", MenuHelper.greenAttribute) + Ansi.RESET);
            startBooking();
        } else {
            System.out.println(MenuHelper.colorize("This user doesn't exist, please sign up", MenuHelper.redAttribute) + Ansi.RESET);
            signUp();
        }
    }

    private void signUp() throws IOException {
        System.out.println(MenuHelper.colorize("Welcome to the Flight Reservation System!", MenuHelper.yellowAttribute));
        System.out.println("-------------------------------------------");
        System.out.println(MenuHelper.colorize("Please sign up to continue.", MenuHelper.yellowAttribute) + Ansi.RESET);
        String name = menuDisplayHelper.promptValidName(MenuHelper.colorize("Enter your name: ", MenuHelper.blueBoldAttribute));
        String surname = menuDisplayHelper.promptValidName(MenuHelper.colorize("Enter your surname: ", MenuHelper.blueBoldAttribute));
        String username = menuDisplayHelper.promptValidName(MenuHelper.colorize("Enter your username: ", MenuHelper.blueBoldBackAttribute));

        try {
            while (true) {
                if (usersController.userExists(username)) {
                    System.out.println(MenuHelper.colorize("Username already exists. Please choose a different one.", MenuHelper.redAttribute) + Ansi.RESET);
                    username = menuDisplayHelper.promptValidName(MenuHelper.colorize("Enter your username: ", MenuHelper.blueBoldBackAttribute));
                } else {
                    break;
                }
            }
            String password;

            while (true) {
                String generatedPassword = menuDisplayHelper.generateRandomPassword();
                System.out.println(MenuHelper.colorize("Generated password:", MenuHelper.greenUnderlineAttribute)
                        + MenuHelper.colorize(" " + generatedPassword, MenuHelper.greenAttribute) + Ansi.RESET);
                System.out.print(MenuHelper.colorize("Do you want to use this password? (yes/no): ", MenuHelper.cyanAttribute));
                String choice = scanner.nextLine().trim().toLowerCase();

                if (choice.equals("yes")) {
                    password = generatedPassword;
                    break;
                } else if (choice.equals("no")) {
                    System.out.println(MenuHelper.colorize("Password must be at least 8 characters long and must consist of capital, " +
                            "small letters, numbers, and symbols.", MenuHelper.greenAttribute) + Ansi.RESET);
                    System.out.print(MenuHelper.colorize("Enter your password: ", MenuHelper.blueBoldBackAttribute));
                    password = scanner.nextLine().trim();

                    if (!password.isEmpty()) {
                        try {
                            if (MenuHelper.checkPassword(password)) {
                                break;
                            } else {
                                MenuHelper.validatePassword(password);
                            }
                        } catch (InvalidPasswordException e) {
                            System.out.println(MenuHelper.colorize("Error: " + e.getMessage(), MenuHelper.redAttribute) + Ansi.RESET);
                        }
                    } else {
                        System.out.println(MenuHelper.colorize("Invalid input. Please enter a valid password.", MenuHelper.redAttribute) + Ansi.RESET);
                    }
                } else {
                    System.out.println(MenuHelper.colorize("Invalid input. Please enter either 'yes' or 'no'.", MenuHelper.redAttribute) + Ansi.RESET);
                }
            }

            Passenger passenger = new Passenger(name, surname);
            User newUser = new User(User.generateId(), username, password, passenger);
            usersController.save(newUser);
            System.out.println(MenuHelper.colorize("User registered successfully!", MenuHelper.greenAttribute) + Ansi.RESET);
            startBooking();
        } catch (UserRegisterException e) {
            System.out.println(MenuHelper.colorize("Error occurred while signing up: " + e.getMessage(), MenuHelper.redAttribute) + Ansi.RESET);
        }
    }

    private void exit() {
        System.out.println(MenuHelper.colorize("Exiting FLIGHT RESERVATION SYSTEM. Goodbye!", MenuHelper.magentaAttribute) + Ansi.RESET);
    }

    public void generateRandomFlights(int amount) throws IOException {
        List<Flight> xs = flightsController.generateRandom(amount);
        flightsController.save(xs);
    }

    public void startBooking() throws IOException {
        menuDisplayHelper.userBoard();
        boolean sessionActive = true;

        while (sessionActive) {
            try {
                System.out.print(MenuHelper.colorize(">>> Enter your choice: ", MenuHelper.magentaAttribute));
                System.out.flush();
                int choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                    case 1:
                        viewTimetable();
                        break;
                    case 2:
                        viewFlightDetails();
                        break;
                    case 3:
                        searchAndBookFlights();
                        break;
                    case 4:
                        // showUserBookings();
                        break;
                    case 5:
                        cancelBooking();
                        break;
                    case 6:
                        exit();
                        sessionActive = false;
                        break;
                    default:
                        System.out.println(MenuHelper.colorize("Invalid choice. Please try again.", MenuHelper.redAttribute));
                }
            } catch (InputMismatchException e) {
                System.out.println(MenuHelper.colorize("Invalid input. Please enter a valid integer.", MenuHelper.redAttribute));
                scanner.nextLine();
            }
        }
    }

    private void viewTimetable() throws IOException {
        List<Flight> flights = flightsController.getAll();
        System.out.println(MenuHelper.colorize("===================================    TIMETABLE    =======================================", MenuHelper.greenAttribute));
        headSearchFlights();
        flights.stream().map(Flight::prettyFormat).forEach(System.out::println);
    }

    private void headSearchFlights() {
        System.out.println(MenuHelper.colorize("|                  ID                 |       AIRLINES       |   FLY FROM   |    FLY TO    |      DATE-TIME      | SEATS |", MenuHelper.greenAttribute));
    }

    private void headSearchBookings() {
        System.out.println(MenuHelper.colorize("|       Passenger     |    Class    |       Cost       |  ID  |       AIRLINES       |   FLY FROM   |    FLY TO    |      DATE-TIME      |", MenuHelper.greenAttribute));
    }

    private void viewFlightDetails() throws IOException {
        System.out.print(MenuHelper.colorize("Enter the flight number: ", MenuHelper.magentaAttribute));
        String flightNumber = scanner.nextLine();

        try {
            Flight flight = flightsController.getById(flightNumber);
            if (flight != null) {
                // System.out.println(flight.prettyFormat());
                System.out.println(flight);
            } else {
                System.out.println("Flight with number " + flightNumber + " not found.");
            }
        } catch (FlightNotFoundException e) {
            System.out.println(MenuHelper.colorize("Flight with number " + flightNumber + " not found.", MenuHelper.redAttribute));
        }
    }

    private void searchAndBookFlights() throws IOException {
        Scanner scanner = new Scanner(System.in);

        // Step 1: Search Flights
        System.out.print("Enter origin city: ");
        City origin = City.valueOf(scanner.nextLine());
        System.out.print("Enter destination city: ");
        City destination = City.valueOf(scanner.nextLine());
        // You can add more search criteria such as date, number of passengers, etc.

        List<Flight> searchResults = flightsController.search(origin, destination);

        // Step 2: Display Search Results
        if (!searchResults.isEmpty()) {
            System.out.println("===================================     SEARCHING     ======================================");
            headSearchFlights();
            for (int i = 0; i < searchResults.size(); i++) {
                System.out.println((i + 1) + ". " + searchResults.get(i));
            }

            // Step 3: Select a Flight
            System.out.print("Select a flight (enter the corresponding number): ");
            int selectedFlightIndex = scanner.nextInt();
            if (selectedFlightIndex > 0 && selectedFlightIndex <= searchResults.size()) {
                Flight selectedFlight = searchResults.get(selectedFlightIndex - 1);

                // Step 4: Book a Flight
                System.out.println("You selected the following flight:");
                System.out.println(selectedFlight);
                System.out.print("Enter the number of passengers: ");
                int numPassengers = scanner.nextInt();
                if (numPassengers > 0) {
                    // Book the flight
                    if (currentUser.isPresent()) {
                        User user = currentUser.get();
                        List<Passenger> passengers = new ArrayList<>();
                        for (int i = 0; i < numPassengers; i++) {
                            // Assuming you have methods to retrieve the name and surname of the user
                            String name = passengers.get(i).getName();
                            String surname = passengers.get(i).getSurname();
                            passengers.add(new Passenger(name, surname));
                        }
                        Optional<Booking> bookingResult = bookingsController.create(selectedFlight, passengers);
                        if (bookingResult.isPresent()) {
                            user.addBooking(bookingResult.get());
                            System.out.println("Flight booked successfully!");
                        } else {
                            System.out.println("Failed to book the flight. Please try again later.");
                        }
                    } else {
                        System.out.println("Please log in to book the flight.");
                    }
                } else {
                    System.out.println("Invalid number of passengers.");
                }
            } else {
                System.out.println("Invalid selection.");
            }
        } else {
            System.out.println("No flights found for the given criteria.");
        }
    }
/*
    private void showUserBookings() {
        if (currentUser.isPresent()) {
            User user = currentUser.get();
            List<Booking> userBookings = user.getBookings();
            if (!userBookings.isEmpty()) {
                System.out.println("==========================================================================");
                headSearchBookings();
                userBookings.stream().map(Booking::prettyFormat).forEach(System.out::println);
                System.out.println("==========================================================================");
                System.out.println("Total bookings: " + userBookings.size());
            } else {
                System.out.println("You have no bookings.");
            }
        }
    }

 */

    private void cancelBooking() throws IOException {
        Scanner scanner = new Scanner(System.in);
        if (currentUser.isPresent()) {
            List<Booking> userBookings = bookingsController.getAll();
            if (!userBookings.isEmpty()) {
                System.out.println("Your bookings:");
                for (int i = 0; i < userBookings.size(); i++) {
                    System.out.println((i + 1) + ". " + userBookings.get(i));
                }

                System.out.print("Select the booking to cancel (enter the corresponding number): ");
                int selectedBookingIndex = scanner.nextInt();
                if (selectedBookingIndex > 0 && selectedBookingIndex <= userBookings.size()) {
                    Booking selectedBooking = userBookings.get(selectedBookingIndex - 1);
                    // Cancel the booking
                    bookingsController.delete(selectedBooking);
                    System.out.println("Booking cancelled successfully!");
                } else {
                    System.out.println("Invalid selection.");
                }
            } else {
                System.out.println("You have no bookings to cancel.");
            }
        } else {
            System.out.println("User not authenticated.");
        }
    }
}
