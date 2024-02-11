package console;

import utils.exceptions.InvalidNameException;

import java.util.InputMismatchException;
import java.util.Scanner;

public class MenuHelper {

    public static final Scanner scanner = new Scanner(System.in);

    public String promptValidName(String message) {
        while (true) {
            try {
                String input = promptString(message);
                validateName(input);
                return formatName(input);
            } catch (InvalidNameException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    public void validateName(String name) throws InvalidNameException {
        if (name.matches(".*\\d.*")) {
            throw new InvalidNameException("Name cannot contain digits.");
        }

        if (!name.matches("^[a-zA-Z]+(-[a-zA-Z]+)*(\\s[a-zA-Z]+(-[a-zA-Z]+)*)*$")) {
            throw new InvalidNameException("Name must contain only Latin letters, hyphens, and spaces.");
        }
    }

    public String formatName(String name) {
        String[] words = name.split("[\\s-]+");
        StringBuilder formattedName = new StringBuilder();
        for (String word : words) {
            if (formattedName.length() > 0) {
                formattedName.append(" ");
            }
            formattedName.append(word.substring(0, 1).toUpperCase())
                    .append(word.substring(1).toLowerCase());
        }
        return formattedName.toString();
    }

    public String promptString(String message) {
        System.out.print(message + ": ");
        return scanner.nextLine();
    }

    public int promptInt(String message, int minValue, int maxValue) {
        int value;
        do {
            value = validateInput(message + " (" + minValue + " - " + maxValue + "): ", minValue, maxValue);
        } while (value < minValue || value > maxValue);

        return value;
    }

    public int validateInput(String prompt, int minValue, int maxValue) {
        int value;
        while (true) {
            try {
                System.out.print(prompt);
                while (!scanner.hasNextInt()) {
                    System.out.print("Invalid input. Please enter a valid integer: ");
                    scanner.next(); // consume the invalid input
                }
                value = scanner.nextInt();
                scanner.nextLine(); // consume the newline

                if (value < minValue || value > maxValue) {
                    System.out.println("Value must be between " + minValue + " and " + maxValue + ".");
                } else {
                    break; // valid input, exit the loop
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: Invalid input. Please enter a valid integer.");
                scanner.nextLine(); // consume the invalid input
            }
        }
        return value;
    }

    public void handleInputMismatchException() {
        System.out.println("Error: Invalid input. Please enter a valid integer.");
        scanner.nextLine();
    }

    public void user() {
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║                   FLIGHT RESERVATION SYSTEM              ║");
        System.out.println("║     Please choose one of the options below to continue.  ║");
        System.out.println("║──────────────────────────────────────────────────────────║");
        System.out.println("║                 1. View Timetable                        ║");
        System.out.println("║                 2. View Flight Details                   ║");
        System.out.println("║                 3. Search and Bookings                   ║");
        System.out.println("║                 4. My Bookings                           ║");
        System.out.println("║                 5. Cancel a Booking                      ║");
        System.out.println("║                 6. End Session                           ║");
        System.out.println("║──────────────────────────────────────────────────────────║");
        System.out.print(">>> Your selection: ");
    }

    public void visitor() {
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║                FLIGHT RESERVATION SYSTEM                 ║");
        System.out.println("║  Please choose one of the options below to continue.     ║");
        System.out.println("║──────────────────────────────────────────────────────────║");
        System.out.println("║                      1. Log In.                          ║");
        System.out.println("║                      2. Sign Up.                         ║");
        System.out.println("║                      3. Exit.                            ║");
        System.out.println("║──────────────────────────────────────────────────────────║");
        System.out.print(">>> Your selection: ");
    }
}
