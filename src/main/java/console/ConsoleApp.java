package console;

import bookings.*;
import console.colored_console.Ansi;
import flights.*;
import passenger.Passenger;
import users.*;
import utils.exceptions.*;

import java.io.IOException;
import java.util.*;

public class ConsoleApp {
    private static final Scanner scanner = new Scanner(System.in);
    private final UsersController usersController;
    private final BookingsController bookingsController;
    private final FlightsController flightsController;
    private final OperationApp[] operations;
    private User currentUser;
    private final MenuHelper menuDisplayHelper;
    private boolean globalSessionActive;

    public ConsoleApp(UsersController usersController, BookingsController bookingsController, FlightsController flightsController) throws IOException {
        this.usersController = usersController;
        this.bookingsController = bookingsController;
        this.flightsController = flightsController;
        this.menuDisplayHelper = new MenuHelper();
        this.operations = new OperationApp[]{
                new OperationApp("View Timetable", this::viewTimetable),
                new OperationApp("View Flight Details", this::viewFlightDetails),
                new OperationApp("Search and Book Flights", this::searchAndBookFlights),
                new OperationApp("Show User Bookings", this::showUserBookings),
                new OperationApp("Cancel Booking", this::cancelBooking),
                new OperationApp("Logout", this::exit)
        };
    }

    public void start() throws IOException {
        while (globalSessionActive = true) {
            try {
                menuDisplayHelper.visitorBoard();
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
                        globalSessionActive = false;
                        exit();
                    default:
                        System.out.println(MenuHelper.colorize("Invalid choice. Please try again.", MenuHelper.redAttribute) + Ansi.RESET);
                }
            } catch (NumberFormatException e) {
                System.out.println(MenuHelper.colorize("Invalid input. Please enter a valid integer.", MenuHelper.redAttribute) + Ansi.RESET);
            } catch (InputMismatchException e) {
                System.out.println(MenuHelper.colorize(e.getMessage(), MenuHelper.redAttribute) + Ansi.RESET);
            } catch (ExitSessionException e) {
                System.out.println(MenuHelper.colorize(e.getMessage(), MenuHelper.magentaAttribute) + Ansi.RESET);
                return;
            }
        }
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    private void authentificationHead() {
        System.out.println(MenuHelper.colorize("Welcome to the Flight Reservation System!", MenuHelper.yellowAttribute) + Ansi.RESET);
        System.out.println(MenuHelper.colorize("-------------------------------------------", MenuHelper.greenAttribute) + Ansi.RESET);
    }

    private User authenticateUser(String username, String password) throws IOException {
        System.out.println();
        System.out.println(MenuHelper.colorize("Authenticating...", MenuHelper.cyanAttribute) + Ansi.RESET);
        System.out.println();
        return usersController.authenticate(username, password);
    }

    private String askUseGeneratedPassword() {
        String generatedPassword = menuDisplayHelper.generateRandomPassword();
        System.out.println(MenuHelper.colorize("Generated password:", MenuHelper.greenUnderlineAttribute)
                + MenuHelper.colorize(" " + generatedPassword, MenuHelper.greenAttribute) + Ansi.RESET);
        System.out.print(MenuHelper.colorize("Do you want to use this password? (yes/no): ", MenuHelper.cyanAttribute) + Ansi.RESET);
        return generatedPassword;
    }

    private User login() throws IOException, ExitSessionException {
        authentificationHead();
        System.out.println(MenuHelper.colorize("Please log in to continue.", MenuHelper.yellowAttribute) + Ansi.RESET);
        System.out.println();
        System.out.print(MenuHelper.colorize("Enter your username: ", MenuHelper.blueBoldBackAttribute));
        String username = scanner.nextLine().trim().toLowerCase();
        username = Character.toUpperCase(username.charAt(0)) + username.substring(1);

        this.currentUser = usersController.getUserByUserName(username);

        if (this.currentUser == null) {
            System.out.println(MenuHelper.colorize("This user doesn't exist. Please sign up.", MenuHelper.redAttribute) + Ansi.RESET);
            this.currentUser = signUpAfterFailedLogin();
            return currentUser;
        }

        System.out.print(MenuHelper.colorize("Enter your password: ", MenuHelper.blueBoldBackAttribute));
        String password = scanner.nextLine().trim();
        currentUser = authenticateUser(username, password);
        if (this.currentUser != null) {
            System.out.println(MenuHelper.colorize("You have successfully logged in", MenuHelper.greenAttribute) + Ansi.RESET);
            startBooking();
            return this.currentUser;
        } else {
            System.out.println(MenuHelper.colorize("The password is wrong.", MenuHelper.redAttribute) + Ansi.RESET);
            System.out.println(MenuHelper.colorize("We propose you to generate a new passport.", MenuHelper.cyanAttribute) + Ansi.RESET);
            String generatedPassword = askUseGeneratedPassword();
            String choice = scanner.nextLine().trim().toLowerCase();
            if (choice.equals("yes")) {
                password = generatedPassword;
                this.currentUser = usersController.updatePassword(username, password);
                this.currentUser = usersController.updateUser(this.currentUser);
                setCurrentUser(this.currentUser);
                System.out.println(MenuHelper.colorize("You have successfully logged in", MenuHelper.greenAttribute) + Ansi.RESET);
                startBooking();
                return this.currentUser;
            } else if (choice.equals("no")) {
                System.out.println(MenuHelper.colorize("Password must be at least 8 characters long and must consist of capital, " +
                        "small letters, numbers, and symbols.", MenuHelper.greenAttribute) + Ansi.RESET);
                System.out.print(MenuHelper.colorize("Enter your password: ", MenuHelper.blueBoldBackAttribute) + Ansi.RESET);
                password = scanner.nextLine().trim();

                if (!password.isEmpty()) {
                    try {
                        if (MenuHelper.checkPassword(password)) {
                            this.currentUser = usersController.updatePassword(username, password);
                            usersController.updateUser(this.currentUser);
                            setCurrentUser(this.currentUser);
                            System.out.println(MenuHelper.colorize("You have successfully logged in", MenuHelper.greenAttribute) + Ansi.RESET);
                            startBooking();
                            return this.currentUser;
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
        return this.currentUser;
    }

    private User signUpAfterFailedLogin() throws IOException {
        System.out.println(MenuHelper.colorize("Please sign up to continue.", MenuHelper.yellowAttribute) + Ansi.RESET);
        String name = menuDisplayHelper.promptValidName(MenuHelper.colorize("Enter your name: ", MenuHelper.blueBoldAttribute) + Ansi.RESET);
        String surname = menuDisplayHelper.promptValidName(MenuHelper.colorize("Enter your surname: ", MenuHelper.blueBoldAttribute) + Ansi.RESET);
        String username = menuDisplayHelper.promptValidName(MenuHelper.colorize("Enter your username: ", MenuHelper.blueBoldBackAttribute) + Ansi.RESET);

        try {
            while (true) {
                if (usersController.userExists(username)) {
                    System.out.println(MenuHelper.colorize("Username already exists. Please choose a different one.", MenuHelper.redAttribute) + Ansi.RESET);
                    username = menuDisplayHelper.promptValidName(MenuHelper.colorize("Enter your username: ", MenuHelper.blueBoldBackAttribute) + Ansi.RESET);
                } else {
                    break;
                }
            }
            String password;

            while (true) {
                String generatedPassword = askUseGeneratedPassword();
                String choice = scanner.nextLine().trim().toLowerCase();

                if (choice.equals("yes")) {
                    password = generatedPassword;
                    break;
                } else if (choice.equals("no")) {
                    System.out.println(MenuHelper.colorize("Password must be at least 8 characters long and must consist of capital, " +
                            "small letters, numbers, and symbols.", MenuHelper.greenAttribute) + Ansi.RESET);
                    System.out.print(MenuHelper.colorize("Enter your password: ", MenuHelper.blueBoldBackAttribute) + Ansi.RESET);
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
            this.currentUser = new User(User.generateId(), username, password, passenger);
            usersController.save(this.currentUser);
            System.out.println(MenuHelper.colorize("User registered successfully!", MenuHelper.greenAttribute) + Ansi.RESET);
            startBooking();
            return this.currentUser;
        } catch (UserRegisterException e) {
            System.out.println(MenuHelper.colorize("Error occurred while signing up: " + e.getMessage(), MenuHelper.redAttribute) + Ansi.RESET);
            return null;
        }
    }

    private User signUp() throws IOException, ExitSessionException {
        authentificationHead();
        this.currentUser = signUpAfterFailedLogin();
        setCurrentUser(this.currentUser);
        return this.currentUser;
    }

    private void exit() {
        System.out.println(MenuHelper.colorize("Exiting session.", MenuHelper.magentaAttribute) + Ansi.RESET);
        System.exit(0);
    }

    public void generateRandomFlights(int amount) throws IOException {
        List<Flight> xs = flightsController.generateRandom(amount);
        flightsController.save(xs);
    }

    public void startBooking() throws IOException {
        while (globalSessionActive) {
            try {
                menuDisplayHelper.userBoard();
                System.out.print(MenuHelper.colorize(">>> Enter your choice: ", MenuHelper.magentaAttribute) + Ansi.RESET);
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
                        showUserBookings();
                        break;
                    case 5:
                        cancelBooking();
                        break;
                    case 6:
                        globalSessionActive = false;
                        exit();
                    default:
                        System.out.println(MenuHelper.colorize("Invalid choice. Please try again.", MenuHelper.redAttribute) + Ansi.RESET);
                }
            } catch (InputMismatchException e) {
                System.out.println(MenuHelper.colorize("Invalid input. Please enter a valid integer.", MenuHelper.redAttribute) + Ansi.RESET);
                scanner.nextLine();
            } catch (UserNotFoundException e) {
                System.out.println(MenuHelper.colorize("User not found!", MenuHelper.redAttribute));
            } catch (ExitSessionException e) {
                System.out.println(MenuHelper.colorize(e.getMessage(), MenuHelper.magentaAttribute) + Ansi.RESET);
                return;
            }
        }
    }

    private void viewTimetable() throws IOException {
        List<Flight> flights = flightsController.getAll();
        System.out.println(MenuHelper.colorize("=============================================================    TIMETABLE    =================================================================", MenuHelper.greenAttribute) + Ansi.RESET);
        headSearchFlights();
        flights.stream().map(Flight::prettyFormat).forEach(System.out::println);
    }

    private void headSearchFlights() {
        System.out.println(MenuHelper.colorize("|                  ID                  |       AIRLINE        |   FLY FROM   |    FLY TO    |      DEPARTURE     |       ARRIVAL      | SEATS |", MenuHelper.greenAttribute) + Ansi.RESET);
    }

    private void headSearchBookings() {
        System.out.println(MenuHelper.colorize("|               BookingID              |       Passenger        |         Class        |  Cost |               FlightID               |       AIRLINE      |   FLY FROM   |    FLY TO    |      DEPARTURE     |       ARRIVAL      |", MenuHelper.greenAttribute) + Ansi.RESET);
    }

    private void searching() {
        System.out.println(MenuHelper.colorize("============================================================     SEARCHING     ================================================================", MenuHelper.greenAttribute) + Ansi.RESET);
    }

    private void viewFlightDetails() throws IOException {
        boolean exit = false;

        while (!exit) {
            System.out.print(MenuHelper.colorize("\nEnter the flight number or enter '-1' to exit: ", MenuHelper.magentaAttribute) + Ansi.RESET);
            String flightNumber = scanner.nextLine().trim();

            if ("-1".equals(flightNumber)) {
                exit = true;
            } else {
                try {
                    Flight flight = flightsController.getById(flightNumber);
                    searching();
                    headSearchFlights();
                    System.out.println(flight.prettyFormat());
                } catch (InputMismatchException e) {
                    System.out.println(MenuHelper.colorize("Invalid input. Please enter a valid flight number.", MenuHelper.redAttribute) + Ansi.RESET);
                } catch (FlightNotFoundException e) {
                    System.out.println(MenuHelper.colorize("Flight not found. Please enter a valid flight number.", MenuHelper.redAttribute) + Ansi.RESET);
                }
            }
        }
    }

    private void searchAndBookFlights() throws IOException, ExitSessionException {
        while (globalSessionActive) {
            try {
                MenuHelper.searchBoard();
                System.out.print(MenuHelper.colorize(">>> Enter your choice: ", MenuHelper.magentaAttribute) + Ansi.RESET);
                int choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                    case 1:
                        bookFlightsByCities();
                        break;
                    case 2:
                        bookFlightsByDate();
                        break;
                    case 3:
                        startBooking();
                        return;
                    default:
                        System.out.println(MenuHelper.colorize("Invalid choice. Please try again.", MenuHelper.redAttribute) + Ansi.RESET);
                }
            } catch (InputMismatchException e) {
                System.out.println(MenuHelper.colorize("Invalid input. Please enter a valid integer.", MenuHelper.redAttribute) + Ansi.RESET);
                scanner.nextLine();
            } catch (FlightNotFoundException e) {
                System.out.println(MenuHelper.colorize("Flight not found!", MenuHelper.redAttribute) + Ansi.RESET);
            } catch (BookingNotFoundException e) {
                System.out.println(MenuHelper.colorize("Booking isn't created!", MenuHelper.redAttribute) + Ansi.RESET);
            } catch (PassengerOverflowException e) {
                System.out.println(MenuHelper.colorize("Passenger amount exceeded!", MenuHelper.redAttribute) + Ansi.RESET);
            } catch (UserNotFoundException e) {
                System.out.println(MenuHelper.colorize("User not found!", MenuHelper.redAttribute) + Ansi.RESET);
            }
        }
    }

    private City promptCityInput(String promptMessage) throws IOException, ExitSessionException {
        while (true) {
            System.out.print(MenuHelper.colorize(promptMessage, MenuHelper.blueBoldAttribute) + Ansi.RESET);
            String cityInput = scanner.nextLine().trim();
            if (cityInput.isEmpty()) {
                System.out.println(MenuHelper.colorize("Invalid input. City name cannot be empty.", MenuHelper.redAttribute) + Ansi.RESET);
                continue;
            }
            if ("-1".equals(cityInput)) {
                searchAndBookFlights();
                break;
            }
            cityInput = cityInput.toUpperCase();
            try {
                City city = City.valueOf(cityInput);
                return city;
            } catch (IllegalArgumentException e) {
                System.out.println(MenuHelper.colorize("The city '" + cityInput + "' does not exist.", MenuHelper.redAttribute) + Ansi.RESET);
            }
        }
        return null;
    }

    private String promptFlightSelection() throws IOException {
        while (true) {
            try {
                System.out.print(MenuHelper.colorize("\nSelect a flight (enter the corresponding number, or -1 to cancel): ", MenuHelper.cyanAttribute) + Ansi.RESET);
                String selectedFlightNumber = scanner.nextLine().trim();
                if (selectedFlightNumber.isEmpty()) {
                    System.out.println(MenuHelper.colorize("Invalid input. Flight name cannot be empty.", MenuHelper.redAttribute) + Ansi.RESET);
                    continue;
                }

                if ("-1".equals(selectedFlightNumber)) {
                    return null;
                }
                flightsController.getById(selectedFlightNumber);
                return selectedFlightNumber;
            } catch (InputMismatchException e) {
                System.out.print(MenuHelper.colorize("Invalid input for flight selection. Please enter a valid integer or -1 to exit: ", MenuHelper.redAttribute) + Ansi.RESET);
                scanner.nextLine().trim();
            } catch (FlightNotFoundException e) {
                System.out.println(MenuHelper.colorize("Flight not found!", MenuHelper.redAttribute) + Ansi.RESET);
            }
        }
    }

    private int promptPassengerCount() {
        while (true) {
            System.out.print(MenuHelper.colorize("\nEnter the number of passengers: ", MenuHelper.greenAttribute) + Ansi.RESET);
            try {
                int numPassengers = scanner.nextInt();
                scanner.nextLine();
                if (numPassengers <= 0) {
                    throw new InvalidInputException(MenuHelper.colorize("Invalid number of passengers. Please enter a positive integer.", MenuHelper.redAttribute) + Ansi.RESET);
                }
                return numPassengers;
            } catch (InputMismatchException e) {
                scanner.nextLine();
                System.out.println(MenuHelper.colorize("Invalid input for number of passengers. Please enter a valid integer.", MenuHelper.redAttribute) + Ansi.RESET);
            }
        }
    }

    private void bookSelectedFlight(Flight bookFlight, int numPassengers) throws
            BookingNotFoundException, IOException, FlightNotFoundException {
        if (numPassengers <= 0) {
            throw new InvalidInputException(MenuHelper.colorize("Invalid number of passengers. Please enter a positive integer.", MenuHelper.redAttribute) + Ansi.RESET);
        }
        List<Passenger> passengers = promptPassengerDetails(numPassengers);
        bookSelectedFlight(bookFlight, passengers);
    }

    private void bookSelectedFlight(Flight bookFlight, List<Passenger> passengers) throws BookingNotFoundException, IOException {
        Optional<Booking> bookingResult = bookingsController.create(bookFlight, passengers);
        Booking booking = bookingResult.orElseThrow(BookingNotFoundException::new);
        usersController.addBooking(this.currentUser, booking);
        System.out.println(MenuHelper.colorize("Flight booked successfully!", MenuHelper.greenUnderlineAttribute) + Ansi.RESET);
    }

    private Flight bookFlight() throws IOException, FlightNotFoundException {
        String selectedFlightIndex = promptFlightSelection();
        if (selectedFlightIndex == null) {
            return null;
        }
        return flightsController.getById(selectedFlightIndex);
    }

    private void writeFlight(Flight flight) {
        System.out.println(MenuHelper.colorize("You selected the following flight:", MenuHelper.magentaAttribute) + Ansi.RESET);
        System.out.println(flight.prettyFormat());
    }

    private void printSearchResults(List<Flight> searchResults) {
        searching();
        headSearchFlights();
        for (Flight flight : searchResults) {
            System.out.println(flight.prettyFormat());
        }
    }

    private List<Passenger> promptPassengerDetails(int numPassengers) {
        List<Passenger> passengers = new ArrayList<>();
        for (int i = 0; i < numPassengers; i++) {
            String name = menuDisplayHelper.promptValidName(MenuHelper.colorize(String.format("Enter name for passenger %d: ", i + 1), MenuHelper.blueBoldAttribute) + Ansi.RESET);
            String surname = menuDisplayHelper.promptValidName(MenuHelper.colorize(String.format("Enter surname for passenger %d: ", i + 1), MenuHelper.blueBoldAttribute) + Ansi.RESET);
            passengers.add(new Passenger(name, surname));
        }
        return passengers;
    }

    private void bookFlightsByCities() throws IOException, FlightNotFoundException, PassengerOverflowException, BookingNotFoundException, UserNotFoundException, ExitSessionException {
        try {
            City origin = promptCityInput("Enter origin city (or enter -1 to exit), the names of a few words enter by '_': ");
            City destination = promptCityInput("Enter destination city (or enter -1 to exit), the names of a few words enter by '_': ");

            if (origin.equals(destination)) {
                throw new SameCityException(MenuHelper.colorize("Origin and destination cities cannot be the same.", MenuHelper.redAttribute) + Ansi.RESET);
            }

            List<Flight> searchResults = flightsController.search(origin, destination);
            if (searchResults.isEmpty()) {
                throw new FlightNotFoundException();
            }
            printSearchResults(searchResults);

            List<Flight> flights = flightsController.getAll();
            List<Flight[]> connectingFlights = flightsController.findConnectingFlights(flights, origin, destination);
            Map<String, Flight[]> connectingFlightsMap = null;
            if (!connectingFlights.isEmpty()) {
                connectingFlightsMap = new HashMap<>();
                for (Flight[] connectingFlight : connectingFlights) {
                    String connectingFlightUUID = UUID.randomUUID().toString();
                    connectingFlightsMap.put(connectingFlightUUID, connectingFlight);
                    System.out.println(MenuHelper.colorize("\nConnecting flight " + connectingFlightUUID + ":", MenuHelper.cyanAttribute) + Ansi.RESET);

                    System.out.println(MenuHelper.colorize("Outbound flight: ", MenuHelper.greenAttribute) + Ansi.RESET);
                    System.out.println(connectingFlight[0].prettyFormat());
                    System.out.println(MenuHelper.colorize("Inbound flight: ", MenuHelper.greenAttribute) + Ansi.RESET);
                    System.out.println(connectingFlight[1].prettyFormat());
                }
            } else {
                System.out.println(MenuHelper.colorize("No connecting flights found.", MenuHelper.yellowAttribute) + Ansi.RESET);
            }

            while (true) {
                Flight bookFlight = bookFlight();
                if (bookFlight == null) {
                    break;
                }
                writeFlight(bookFlight);

                int numPassengers = promptPassengerCount();
                if (numPassengers > bookFlight.getFreeSeats()) {
                    System.out.println(MenuHelper.colorize("Passenger amount exceeded!", MenuHelper.redAttribute) + Ansi.RESET);
                    continue;
                }

                bookSelectedFlight(bookFlight, numPassengers);
                break;
            }

            if (connectingFlights.isEmpty()) {
                return;
            }

            while (true) {
                System.out.print(MenuHelper.colorize("\nSelect the connecting flight you want to book (or -1 to exit): ", MenuHelper.cyanAttribute) + Ansi.RESET);
                String selectedConnectingFlightUUID = scanner.nextLine().trim();
                if ("-1".equals(selectedConnectingFlightUUID)) {
                    return;
                }

                Flight[] selectedConnectingFlights = connectingFlightsMap.get(selectedConnectingFlightUUID);

                if (selectedConnectingFlights == null) {
                    System.out.println(MenuHelper.colorize("Invalid id. Please enter a valid id.", MenuHelper.redAttribute) + Ansi.RESET);
                    continue;
                }

                for (Flight leg : selectedConnectingFlights) {
                    writeFlight(leg);
                }

                int numPassengers = promptPassengerCount();
                List<Passenger> passengers = promptPassengerDetails(numPassengers);

                for (Flight leg : selectedConnectingFlights) {
                    if (numPassengers > leg.getFreeSeats()) {
                        System.out.println(MenuHelper.colorize("Passenger amount exceeded!", MenuHelper.redAttribute) + Ansi.RESET);
                    }
                }
                bookSelectedFlight(selectedConnectingFlights[0], passengers);
                bookSelectedFlight(selectedConnectingFlights[1], passengers);
                break;
            }
        } catch (SameCityException e) {
            System.out.println(MenuHelper.colorize(e.getMessage(), MenuHelper.redAttribute) + Ansi.RESET);
        } catch (InputMismatchException e) {
            throw new InvalidInputException(MenuHelper.colorize("Invalid input for number of passengers. Please enter a valid integer.", MenuHelper.redAttribute) + Ansi.RESET);
        }
        this.currentUser = usersController.updateUser(currentUser);
    }

    private void bookFlightsByDate() throws IOException, UserNotFoundException, ExitSessionException {
        while (true) {
            try {
                System.out.print(MenuHelper.colorize("Enter the date (dd/MM/yyyy) or enter -1 to exit: ", MenuHelper.magentaAttribute) + Ansi.RESET);
                String dateString = scanner.nextLine().trim();
                if ("-1".equals(dateString)) {
                    break;
                }
                List<Flight> searchResults = flightsController.searchByDate(dateString);
                if (searchResults.isEmpty()) {
                    System.out.println(MenuHelper.colorize("No flights available for the specified date.", MenuHelper.redAttribute) + Ansi.RESET);
                } else {
                    printSearchResults(searchResults);

                    Flight bookFlight = bookFlight();

                    if (bookFlight == null) {
                        break;
                    }

                    writeFlight(bookFlight);

                    int numPassengers = promptPassengerCount();
                    if (numPassengers > bookFlight.getFreeSeats()) {
                        System.out.println(MenuHelper.colorize("Passenger amount exceeded!", MenuHelper.redAttribute) + Ansi.RESET);
                        continue;
                    }

                    bookSelectedFlight(bookFlight, numPassengers);
                    break;
                }
            } catch (IllegalArgumentException e) {
                System.out.println(MenuHelper.colorize("Invalid date format. Please try again.", MenuHelper.redAttribute) + Ansi.RESET);
            } catch (FlightNotFoundException e) {
                System.out.println(MenuHelper.colorize("Flight not found!", MenuHelper.redAttribute) + Ansi.RESET);
            } catch (BookingNotFoundException e) {
                System.out.println(MenuHelper.colorize("Booking isn't created!", MenuHelper.redAttribute) + Ansi.RESET);
            } catch (InputMismatchException e) {
                throw new InvalidInputException(MenuHelper.colorize("Invalid input for number of passengers. Please enter a valid integer.", MenuHelper.redAttribute) + Ansi.RESET);
            }
        }
        this.currentUser = usersController.updateUser(currentUser);
    }

    private void headBookings() {
        System.out.println(MenuHelper.colorize("=====================================================================================================     YOUR BOOKINGS     ========================================================================================================", MenuHelper.greenAttribute) + Ansi.RESET);
        headSearchBookings();
    }

    private void showUserBookings() throws UserNotFoundException {
        setCurrentUser(this.currentUser);
        if (this.currentUser != null) {
            List<Booking> userBookings = this.currentUser.getBookings();
            if (!userBookings.isEmpty()) {
                headBookings();
                userBookings.stream().map(Booking::prettyFormat).forEach(System.out::println);
                System.out.println(MenuHelper.colorize("====================================================================================================================================================================================================================================", MenuHelper.blueBoldBackAttribute) + Ansi.RESET);
                System.out.println(MenuHelper.colorize("Total bookings: " + userBookings.size(), MenuHelper.magentaAttribute) + Ansi.RESET);
            } else {
                System.out.println(MenuHelper.colorize("You have no bookings.", MenuHelper.greenUnderlineAttribute) + Ansi.RESET);
            }
        }
    }

    private void cancelBooking() throws IOException, UserNotFoundException, ExitSessionException {
        setCurrentUser(this.currentUser);
        List<Booking> userBookings = this.currentUser.getBookings();
        while (true) {
            try {
                if (!userBookings.isEmpty()) {
                    headBookings();
                    userBookings.stream().map(Booking::prettyFormat).forEach(System.out::println);
                    for (Booking ignored : userBookings) {
                        System.out.print(MenuHelper.colorize("Select the booking to cancel (enter the corresponding booking ID, or -1 to exit): ", MenuHelper.yellowAttribute) + Ansi.RESET);
                        String selectedBookingId = scanner.nextLine().trim();
                        if ("-1".equals(selectedBookingId)) {
                            startBooking();
                            break;
                        }
                        Optional<Booking> bookingToRemove = Optional.ofNullable(bookingsController.getById(selectedBookingId));
                        if (bookingToRemove.isPresent()) {
                            bookingsController.delete(selectedBookingId);
                            usersController.deleteBooking(this.currentUser, selectedBookingId);
                            System.out.println(MenuHelper.colorize("Booking cancelled successfully!", MenuHelper.greenAttribute) + Ansi.RESET);
                            break;
                        } else {
                            System.out.println(MenuHelper.colorize("Booking not found with ID: " + selectedBookingId, MenuHelper.redAttribute) + Ansi.RESET);
                        }
                    }
                } else {
                    System.out.println(MenuHelper.colorize("You have no bookings to cancel.", MenuHelper.redAttribute) + Ansi.RESET);
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println(MenuHelper.colorize("Invalid input. Please enter a valid booking ID.", MenuHelper.redAttribute) + Ansi.RESET);
            } catch (BookingNotFoundException e) {
                System.out.println(MenuHelper.colorize("Booking not found!", MenuHelper.redAttribute));
            }
        }
        this.currentUser = usersController.updateUser(this.currentUser);
    }
}
