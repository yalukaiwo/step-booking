package users;

import Bookings.Booking;
import Flights.Flight;
import Flights.FlightService;
import Flights.FlightsRandomaizer;
import console.BookingService;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Scanner;

public class UserController {
    private final UserService userService;
    private final BookingService bookingService;
    private final FlightService flightService;
    private Optional<User> currentUser;
    private final DisplayMenu displayMenu;

    private final OperationApp[] operations;

    public UserController(UserService userService, BookingService bookingService, FlightService flightService) {
        this.userService = userService;
        this.bookingService = bookingService;
        this.flightService = flightService;
        this.currentUser = Optional.empty();
        this.displayMenu = new DisplayMenu();

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

    public Optional<User> read(String id) throws IOException {
        return userService.read(id);
    }

    public void save(User u) throws IOException {
        userService.save(u);
    }

    public void delete(String id) throws IOException {
        userService.delete(id);
    }

    public List<User> readAll() throws IOException {
        return userService.readAll();
    }

    public User getByUserName(String login) throws IOException {
        return userService.getByUserName(login);
    }

    public User getById(String id) throws IOException {
        return userService.getById(id);
    }

    public void addUser(User u) {
        userService.addUser(u);
    }

    public Optional<User> authenticate(String username, String password) {
        return userService.authenticate(username, password);
    }

    public boolean userExists(String username) {
        try {
            return readAll().stream().anyMatch(user -> user.getUsername().equals(username));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void startSession() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the FLIGHT RESERVATION SYSTEM!");
        displayMenu.visitor();

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

    public void login() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome! Please log in.");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        Optional<User> authenticatedUser = userService.authenticate(username, password);
        if (authenticatedUser.isPresent()) {
            System.out.println("Login successful!");
            currentUser = authenticatedUser;
            showMainMenu();
        } else {
            System.out.println("Invalid username or password. Please try again.");
        }
    }

    private void signUp() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Sign Up");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

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
            if (!userService.userExists(username)) {
                User newUser = new User(username);
                newUser.setPassword(password);
                userService.save(newUser);
                System.out.println("User registered successfully!");
            } else {
                System.out.println("Username already exists. Please choose a different one.");
            }
        } catch (IOException e) {
            System.out.println("Error occurred while signing up: " + e.getMessage());
        }
    }

    private String generateRandomPassword() {
        // Generate a random password with a combination of uppercase letters, lowercase letters, numbers, and symbols
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

    private void showMainMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean sessionActive = true;

        while (sessionActive) {
            // Check if user is not authenticated
            if (!currentUser.isPresent()) {
                login(); // If not authenticated, prompt for login
                if (!currentUser.isPresent()) {
                    // If login failed, continue to the next iteration
                    continue;
                }
            }

            displayMenu.user();

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

    private void viewTimetable() {
        try {
            FlightsRandomaizer randomizer = new FlightsRandomaizer(10, 1, 12, 50, 300);
            List<Flight> flights = randomizer.get();
            System.out.println("Flight Timetable:");
            for (Flight flight : flights) {
                System.out.println(flight);
            }
            showMainMenu();
        } catch (ParseException e) {
            System.err.println("Error occurred while generating flights: " + e.getMessage());
        }
    }

    private void viewFlightDetails() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the flight number: ");
        String flightNumber = scanner.nextLine();

        Optional<Flight> optionalFlight = flightService.getFlightDetails(flightNumber);
        if (optionalFlight.isPresent()) {
            System.out.println("Flight Details: ");
            Flight flight = optionalFlight.get();
            System.out.println("№: " + flight.getNumber());
            System.out.println("Flight ID:" + flight.getId());
            System.out.println("From: " + flight.getDate());
            System.out.println("To: " + flight.getOrigin());
            System.out.println("Date: " + flight.getDestination());
            System.out.println("Airline: " + flight.getAirline());
            System.out.println("Airplane: " + flight.getAirplane());
        } else {
            System.out.println("Flight with number " + flightNumber + " not found.");
        }
    }

    private void searchAndBookFlights() {
        Scanner scanner = new Scanner(System.in);

        // Step 1: Search Flights
        System.out.print("Enter origin city: ");
        String origin = scanner.nextLine();
        System.out.print("Enter destination city: ");
        String destination = scanner.nextLine();
        // You can add more search criteria such as date, number of passengers, etc.

        List<Flight> searchResults = flightService.searchFlights(origin, destination);

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
                System.out.print("Do you want to book this flight? (yes/no): ");
                String bookChoice = scanner.next().toLowerCase();
                if (bookChoice.equals("yes")) {
                    // Book the flight
                    if (currentUser.isPresent()) {
                        bookingService.bookFlight(selectedFlight, currentUser.get());
                        System.out.println("Flight booked successfully!");
                    } else {
                        System.out.println("Please log in to book the flight.");
                    }
                } else {
                    System.out.println("Booking cancelled.");
                }
            } else {
                System.out.println("Invalid selection.");
            }
        } else {
            System.out.println("No flights found for the given criteria.");
        }
    }

    private void showUserBookings() {
        if (currentUser.isPresent()) {
            User user = currentUser.get();
            List<Booking> userBookings = bookingService.getUserBookings(user);
            if (!userBookings.isEmpty()) {
                System.out.println("Your ID: " + user.getId());
                System.out.println("Name: " + user.getUsername());
                System.out.println("Your bookings:");

                // Print booking details
                for (Booking booking : userBookings) {
                    Ticket ticket = booking.getTicket();
                    System.out.println("Flight ID: " + ticket.getFlight().getId());
                    System.out.println("Cost: " + ticket.getCost());
                    System.out.println("Ticket Type: " + ticket.getType());
                    System.out.println("Airline: " + ticket.getFlight().getAirline());
                    // You can add more details as needed
                    System.out.println();
                }
                System.out.println("Total bookings: " + userBookings.size());
            } else {
                System.out.println("You have no bookings.");
            }
        } else {
            System.out.println("User not authenticated.");
        }
    }

    private void cancelBooking() {
        Scanner scanner = new Scanner(System.in);
        if (currentUser.isPresent()) {
            List<Booking> userBookings = bookingService.getUserBookings(currentUser.get());
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
                    bookingService.cancelBooking(selectedBooking);
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
