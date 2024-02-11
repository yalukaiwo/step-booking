package console;

import bookings.Booking;
import bookings.BookingsController;
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
    private final UsersController usersController;
    private final BookingsController bookingsController;
    private final FlightsController flightsController;
    private Optional<User> currentUser;
    private final OperationApp[] operations;
    private final MenuHelper menuDisplayHelper;
    private static final Scanner scanner = new Scanner(System.in);

    public ConsoleApp(UsersController usersController, BookingsController bookingsController, FlightsController flightsController) {
        this.usersController = usersController;
        this.bookingsController = bookingsController;
        this.flightsController = flightsController;
        this.currentUser = Optional.empty();
        this.menuDisplayHelper = new MenuHelper();

        // Initialize operations
        this.operations = new OperationApp[]{
                new OperationApp("View Timetable", this::viewTimetable),
                new OperationApp("View Flight Details", this::viewFlightDetails),
                new OperationApp("Search and Book Flights", this::searchAndBookFlights),
                new OperationApp("Show User Bookings", this::showUserBookings),
                new OperationApp("Cancel Booking", this::cancelBooking),
                new OperationApp("Logout", this::logout)
        };
    }

    public void start() throws IOException, FlightNotFoundException {
        menuDisplayHelper.visitor();

        boolean sessionActive = true;

        while (sessionActive) {
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

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
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    public void login() throws IOException, FlightNotFoundException {
        System.out.println("Welcome to the Flight Reservation System!");
        System.out.println("-------------------------------------------");
        System.out.println("Please log in to continue.");
        System.out.println();

        String username = menuDisplayHelper.promptValidName("Enter your username");

        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        try {
            PasswordValidator.validatePassword(password);
        } catch (InvalidPasswordException e) {
            System.out.println("Error: Password does not meet the requirements.");
            return;
        }

        System.out.println("Authenticating...");
        System.out.println();

        Optional<User> authenticatedUser = usersController.authenticate(username, password);
        if (authenticatedUser.isPresent()) {
            System.out.println("Login successful!");
            System.out.println();
            currentUser = authenticatedUser;
            showMainMenu();
        } else {
            System.out.println("Error: Invalid username or password. Please try again.");
            System.out.println();
        }
    }

    private void signUp() throws IOException {
        System.out.println("Sign Up");
        String username = menuDisplayHelper.promptValidName("Enter your username");

        // Generate a random password
        String generatedPassword = generateRandomPassword();

        // Prompt the user to accept or enter their own password
        System.out.println("Generated password: " + generatedPassword);
        System.out.print("Do you want to use this password? (yes/no): ");
        String choice = scanner.nextLine().trim().toLowerCase();

        String password;
        if (choice.equals("yes")) {
            password = generatedPassword;
        } else {
            System.out.print("Enter your password: ");
            password = scanner.nextLine();
        }

        // Validate the password
        if (!PasswordValidator.checkPassword(password)) {
            System.out.println("Password does not meet the requirements.");
            return;
        }

        try {
            if (!usersController.userExists(username)) {
                User newUser = new User(username);
                newUser.setPassword(password);
                usersController.save(newUser);
                System.out.println("User registered successfully!");
            } else {
                System.out.println("Username already exists. Please choose a different one.");
            }
        } catch (UserRegisterException e) {
            System.out.println("Error occurred while signing up: " + e.getMessage());
        }
    }

    private String generateRandomPassword() {
        StringBuilder password = new StringBuilder();
        Random random = new Random();
        String upperChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerChars = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String symbols = "!@#$%^&*()_+-=[]{}|;:,.<>?";

        // Add random characters to the password
        for (int i = 0; i < 8; i++) {
            switch (random.nextInt(4)) {
                case 0:
                    password.append(upperChars.charAt(random.nextInt(upperChars.length())));
                    break;
                case 1:
                    password.append(lowerChars.charAt(random.nextInt(lowerChars.length())));
                    break;
                case 2:
                    password.append(numbers.charAt(random.nextInt(numbers.length())));
                    break;
                case 3:
                    password.append(symbols.charAt(random.nextInt(symbols.length())));
                    break;
            }
        }

        return password.toString();
    }

    private void exit() {
        System.out.println("Exiting FLIGHT RESERVATION SYSTEM. Goodbye!");
    }

    private void showMainMenu() throws IOException, FlightNotFoundException {
        List<Flight> xs = flightsController.generateRandom(20);
        flightsController.saveAll(xs);
        boolean sessionActive = true;

        while (sessionActive) {
            // Check if user is not authenticated
            if (currentUser.isEmpty()) {
                login(); // If not authenticated, prompt for login
                if (currentUser.isEmpty()) {
                    // If login failed, continue to the next iteration
                    continue;
                }
            }

            menuDisplayHelper.user();

            System.out.println("Main Menu:");
            for (int i = 0; i < operations.length; i++) {
                System.out.println((i + 1) + ". " + operations[i].operationName);
            }

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            if (choice > 0 && choice <= operations.length) {
                operations[choice - 1].operation.operation();
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void viewTimetable() throws IOException, FlightNotFoundException {
        List<Flight> flights = flightsController.getAll();
        System.out.println("Flight Timetable:");
        for (Flight flight : flights) {
            System.out.println(flight);
        }
        showMainMenu();
    }

    private void viewFlightDetails() throws FlightNotFoundException, IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the flight number: ");
        String flightNumber = scanner.nextLine();
        Optional<Flight> optionalFlight = Optional.ofNullable(flightsController.getById(flightNumber));
        if (optionalFlight.isPresent()) {
            System.out.println("Flight Details: ");
            Flight flight = optionalFlight.get();
            System.out.println("Flight ID:" + flight.getId());
            System.out.println("Airline: " + flight.getAirline());
            System.out.println("From: " + flight.getDestination());
            System.out.println("To: " + flight.getOrigin());
            System.out.println("Date: " + flight.getDepartureTime());
        } else {
            System.out.println("Flight with number " + flightNumber + " not found.");
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

        List<Flight> searchResults = flightsController.searchFlight(origin, destination);

        // Step 2: Display Search Results
        if (!searchResults.isEmpty()) {
            System.out.println("Search Results:");
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

    private void showUserBookings() throws IOException {
        if (currentUser.isPresent()) {
            User user = currentUser.get();
            List<Booking> userBookings = user.getBookings();
            if (!userBookings.isEmpty()) {
                System.out.println("Your ID: " + user.getId());
                System.out.println("Name: " + user.getUsername());
                System.out.println("Your bookings:");

                // Print booking details
                for (Booking booking : userBookings) {
                    System.out.println("Flight ID: " + booking.getFlight().getId());
                    System.out.println("Airline: " + booking.getFlight().getAirline());
                    System.out.println("From: " + booking.getFlight().getOrigin());
                    System.out.println("To: " + booking.getFlight().getDestination());
                    System.out.println("Date: " + booking.getFlight().getDepartureTime());
                    System.out.println("Passengers: " + booking.getPassengers());
                    System.out.println("Cost: " + booking.getFlight().getTicketCost());
                    System.out.println("Class: " + booking.getFlight().getClass());
                }
                System.out.println("Total bookings: " + userBookings.size());
            } else {
                System.out.println("You have no bookings.");
            }
        } else {
            System.out.println("User not authenticated.");
        }
    }

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

    private void logout() {
        currentUser = Optional.empty();
        System.out.println("Logout successful.");
    }
}
