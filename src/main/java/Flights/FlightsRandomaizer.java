package Flights;

import java.text.ParseException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FlightsRandomaizer {
    private final int quantity;
    private final int lastMonthInSchedule;
    private final int firstMonthInSchedule;
    private List<Flight> flights;
    private final int minSeats;
    private final int maxSeats;

    public FlightsRandomaizer(int quantity, int firstMonthInSchedule, int lastMonthInSchedule, int minSeats, int maxSeats) {
        this.quantity = quantity;
        this.firstMonthInSchedule = firstMonthInSchedule;
        this.lastMonthInSchedule = lastMonthInSchedule;
        this.minSeats = minSeats;
        this.maxSeats = maxSeats;
    }

    int generateValueFromRange(int min, int max) {
        Random r = new Random();
        int val = r.nextInt(max - min);
        return min + val;
    }

    public List<Flight> get() throws ParseException {
        ArrayList<Flight> flights = new ArrayList<>();
        int homeQuantity = (int)(quantity * 0.3);
        for (int i = 0; i < homeQuantity; i++) {
            // Assuming tripTime, maxPassengers, and seats are available here
            flights.add(generateNewFlight("kyiv", generateTripTime(), generateMaxSeats(), generateSeats()));
        }
        for (int i = homeQuantity; i < quantity; i++) {
            // Assuming tripTime, maxPassengers, and seats are available here
            flights.add(generateNewFlight(null, generateTripTime(), generateMaxSeats(), generateSeats()));
        }
        return flights;
    }

    private long generateTripTime() {
        // Generate trip time between 1 and 6 hours (in milliseconds)
        return generateValueFromRange(1, 6) * 60 * 60 * 1000;
    }


    private String generateFlightNumber(String from, String destination, int id) {
        StringBuilder sb = new StringBuilder();
        sb.append(from.toUpperCase().charAt(0));
        sb.append(destination.toUpperCase().charAt(0));
        sb.append(id);
        return sb.toString();
    }

    private int generateSeats() {
        Random random = new Random();
        return random.nextInt(maxSeats - minSeats + 1) + minSeats;
    }

    private int generateMaxSeats() {
        Random random = new Random();
        int result = random.nextInt(maxSeats - minSeats + 1) + minSeats;
        int seats = generateSeats(); // Generate seats using the result of generateMaxSeats
        while (seats > result) { // Ensure generated seats are not greater than the result
            seats = generateSeats();
        }
        return result;
    }

    private String generateDateTime() {
        Random random = new Random();
        LocalDateTime dateTime = LocalDateTime.now()
                .plusDays(random.nextInt(30))
                .plusHours(random.nextInt(24))
                .plusMinutes(random.nextInt(60));
        return dateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm"));
    }

    private String generateArrivalTime(String date) {
        int tripTime = generateValueFromRange(1, 6) * 60 * 60 * 1000;
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm");

        try {
            LocalDateTime depart = LocalDateTime.parse(date, dateFormatter);
            LocalDateTime arrival = depart.plusSeconds(tripTime / 1000);
            return arrival.format(dateFormatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Error while parsing date from string");
        }
    }

    private String createStringDate(int day, int month, int year, int hour) {
        String dayString = Integer.toString(day);
        if (dayString.length() == 1) dayString = "0"+dayString;
        String monthString = Integer.toString(month);
        if (monthString.length() == 1) monthString = "0"+monthString;
        String yearString = Integer.toString(year);
        String hourString = Integer.toString(hour);
        if (hourString.length() == 1) hourString = "0"+hourString;

        return dayString + "." + monthString + "." + yearString + "-" + hourString + ":" + "00";
    }

    private Flight generateNewFlight(String homeCity, long tripTime, int maxPassengers, int passengers) {
        int id = generateValueFromRange(100, 1000);
        Airport[] airports = Airport.values();
        int arraySize = airports.length;

        Airport destination = airports[generateValueFromRange(0, arraySize)];
        Airport from;
        if (homeCity == null) {
            from = airports[generateValueFromRange(0, arraySize)];
            while (from == destination) {
                from = airports[generateValueFromRange(0, arraySize)];
            }
        } else {
            from = Airport.valueOf(homeCity.toUpperCase());
        }

        String flightNumber = generateFlightNumber(from.name(), destination.name(), id);
        String depart = generateDateTime();
        String arrival = generateArrivalTime(depart);

        return new Flight(from.name(), destination.name(), LocalDateTime.parse(depart, DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm")).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(), tripTime, maxPassengers, passengers);
    }

    // Method to search for flights between cities with connecting flights having a waiting time of no more than 12 hours
    public List<Flight> searchFlightsBetweenCitiesWithConnection() {
        List<Flight> foundFlights = new ArrayList<>();

        Airport[] airports = Airport.values();
        int numAirports = airports.length;

        for (int i = 0; i < numAirports; i++) {
            for (int j = 0; j < numAirports; j++) {
                if (i != j) { // Ensure origin and destination are different
                    Airport origin = airports[i];
                    Airport destination = airports[j];
                    List<Flight> connectingFlights = searchConnectingFlights(origin, destination);
                    foundFlights.addAll(connectingFlights);
                }
            }
        }

        return foundFlights;
    }

    private List<Flight> searchConnectingFlights(Airport origin, Airport destination) {
        List<Flight> connectingFlights = new ArrayList<>();

        for (Flight firstLeg : flights) {
            if (firstLeg.getOrigin().equals(origin) && !firstLeg.getDestination().equals(origin) && !firstLeg.getDestination().equals(destination)) {
                for (Flight secondLeg : flights) {
                    if (!secondLeg.getOrigin().equals(origin) && !secondLeg.getDestination().equals(origin) && // Ensure second leg is not starting or ending at origin
                            !secondLeg.getOrigin().equals(destination) && // Ensure second leg is not starting at destination
                            firstLeg.getDestination().equals(secondLeg.getOrigin())) {

                        Instant firstDeparture = Instant.ofEpochMilli(firstLeg.getDepartureTime());
                        Instant secondDeparture = Instant.ofEpochMilli(secondLeg.getDepartureTime());

                        Duration timeDifference = Duration.between(firstDeparture, secondDeparture);
                        Duration maxTimeDifference = Duration.ofHours(12);

                        if (timeDifference.compareTo(maxTimeDifference) > 0 &&
                                timeDifference.compareTo(maxTimeDifference.plusDays(1)) < 0 &&
                                secondLeg.getDestination().equals(destination)) {
                            connectingFlights.add(firstLeg);
                            connectingFlights.add(secondLeg);
                        }
                    }
                }
            }
        }

        return connectingFlights;
    }

    public static void main(String[] args) {
        try {
            FlightsRandomaizer randomizer = new FlightsRandomaizer(10, 1, 12, 50, 250);
            List<Flight> flights = randomizer.get();

            for (Flight flight : flights) {
                System.out.println("Flight Number: " + flight.getOrigin().charAt(0) + flight.getDestination().charAt(0) + flight.getDepartureTime());
                System.out.println("From: " + flight.getOrigin());
                System.out.println("To: " + flight.getDestination());
                System.out.println("Departure Time: " + flight.getDepartureTime());
                System.out.println("Arrival Time: " + flight.getArrivalTime());
                System.out.println("Seats: " + flight.getMaxPassengers());
                System.out.println("-----------------------");
            }
        } catch (ParseException e) {
            System.err.println("Error occurred while generating flights: " + e.getMessage());
        }
    }
}
