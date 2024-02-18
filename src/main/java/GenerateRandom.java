import flights.Flight;
import flights.FlightsController;
import flights.FlightsDAO;
import flights.FlightsService;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class GenerateRandom {
    public static void main(String[] args) throws IOException {
        FlightsController flightsController = new FlightsController(new FlightsService(new FlightsDAO(new File("flights.bin"))));

        System.out.print("How many flights do you want to generate (they stack onto the existing ones): ");
        Scanner scanner = new Scanner(System.in);
        int answer = scanner.nextInt();
        List<Flight> fs = flightsController.generateRandom(answer);
        System.out.println("DONE! " + fs.size());
    }
}
