package console;

import console.colored_console.*;
import utils.exceptions.*;

import java.util.Random;
import java.util.Scanner;

public class MenuHelper {

    private static final Scanner scanner = new Scanner(System.in);
    public static final Attribute blueBoldAttribute = new Attribute().bold().withColor(Ansi.ColorFont.BLUE);
    public static final Attribute blueBoldBackAttribute = new Attribute().bold().withColor(Ansi.ColorFont.BLUE).withBackground(Ansi.ColorBack.BLACK);
    public static final Attribute whiteBoldBackAttribute = new Attribute().bold().withColor(Ansi.ColorFont.WHITE).withBackground(Ansi.ColorBack.BLACK);
    public static final Attribute cyanAttribute = new Attribute().withColor(Ansi.ColorFont.CYAN);
    public static final Attribute yellowAttribute = new Attribute().withColor(Ansi.ColorFont.YELLOW);
    public static final Attribute magentaAttribute = new Attribute().withColor(Ansi.ColorFont.MAGENTA);
    public static final Attribute redAttribute = new Attribute().withColor(Ansi.ColorFont.RED);
    public static final Attribute greenAttribute = new Attribute().withColor(Ansi.ColorFont.GREEN);
    public static final Attribute greenUnderlineAttribute = new Attribute().underline().withColor(Ansi.ColorFont.GREEN);
    private static final String FRAME_HORIZONTAL = "══";
    private static final String FRAME_VERTICAL = "║";
    private static final String FRAME_TOP_LEFT = "╔";
    private static final String FRAME_TOP_RIGHT = "╗";
    private static final String FRAME_BOTTOM_LEFT = "╚";
    private static final String FRAME_BOTTOM_RIGHT = "╝";
    private static final String FRAME_VERTICAL_RIGHT = "╠";
    private static final String FRAME_VERTICAL_LEFT = "╣";
    private static final String CAPITAL_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String SMALL_LETTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMBERS = "0123456789";
    private static final String SYMBOLS = "!@#$%^&*()-_+=<>?";

    public void userBoard() {
        head();
        System.out.println(colorize(FRAME_VERTICAL, yellowAttribute) + colorize("  1. View Timetable   ", cyanAttribute) + centerText("", 35) + colorize(FRAME_VERTICAL, yellowAttribute));
        System.out.println(colorize(FRAME_VERTICAL, yellowAttribute) + colorize("  2. View Flight Details  ", cyanAttribute) + centerText("", 30) + colorize(FRAME_VERTICAL, yellowAttribute));
        System.out.println(colorize(FRAME_VERTICAL, yellowAttribute) + colorize("  3. Search and Book  ", cyanAttribute) + centerText("", 34) + colorize(FRAME_VERTICAL, yellowAttribute));
        System.out.println(colorize(FRAME_VERTICAL, yellowAttribute) + colorize("  4. My Bookings  ", cyanAttribute) + centerText("", 38) + colorize(FRAME_VERTICAL, yellowAttribute));
        System.out.println(colorize(FRAME_VERTICAL, yellowAttribute) + colorize("  5. Cancel a Booking   ", cyanAttribute) + centerText("", 33) + colorize(FRAME_VERTICAL, yellowAttribute));
        System.out.println(colorize(FRAME_VERTICAL, yellowAttribute) + colorize("  6. End Session    ", cyanAttribute) + centerText("", 36) + colorize(FRAME_VERTICAL, yellowAttribute));
        end();
    }

    public void visitorBoard() {
        head();
        System.out.println(colorize(FRAME_VERTICAL, yellowAttribute) + colorize("  1. Log In ", cyanAttribute) + centerText("", 45) + colorize(FRAME_VERTICAL, yellowAttribute));
        System.out.println(colorize(FRAME_VERTICAL, yellowAttribute) + colorize("  2. Sign Up", cyanAttribute) + centerText("", 45) + colorize(FRAME_VERTICAL, yellowAttribute));
        System.out.println(colorize(FRAME_VERTICAL, yellowAttribute) + colorize("  3. Exit ", cyanAttribute) + centerText("", 47) + colorize(FRAME_VERTICAL, yellowAttribute));
        end();
    }
    public static void head() {
        System.out.println(colorize(FRAME_TOP_LEFT, yellowAttribute) + repeatString(colorize(FRAME_HORIZONTAL, yellowAttribute), 28) + colorize(FRAME_TOP_RIGHT, yellowAttribute));
        System.out.println(colorize(FRAME_VERTICAL, yellowAttribute) + colorize("  FLIGHT RESERVATION SYSTEM                             ", blueBoldAttribute) + colorize(FRAME_VERTICAL, yellowAttribute));
        System.out.println(colorize(FRAME_VERTICAL, yellowAttribute) + colorize("  Please choose one of the options below to continue.   ", blueBoldAttribute) + colorize(FRAME_VERTICAL, yellowAttribute));
        System.out.println(colorize(FRAME_VERTICAL_RIGHT, yellowAttribute) + repeatString(colorize(FRAME_HORIZONTAL, yellowAttribute), 28) + colorize(FRAME_VERTICAL_LEFT, yellowAttribute));
    }

    public static void end() {
        System.out.println(colorize(FRAME_BOTTOM_LEFT, yellowAttribute) + repeatString(colorize(FRAME_HORIZONTAL, yellowAttribute), 28) + colorize(FRAME_BOTTOM_RIGHT, yellowAttribute));
    }

    public static String colorize(String text, Attribute attribute) {
        return attribute.escapeSequence() + text + Ansi.RESET;
    }

    public static String centerText(String text, int width) {
        int padding = (width - text.length()) / 2;
        return repeatString(" ", padding) + text + repeatString(" ", padding);
    }

    public static String repeatString(String str, int count) {
        return String.valueOf(str).repeat(Math.max(0, count));
    }

    public String promptValidName(String message) {
        while (true) {
            try {
                String input = promptString(message);
                validateName(input);
                return formatName(input);
            } catch (InvalidInputException e) {
                System.out.println(colorize("Error: " + e.getMessage(), redAttribute));
            }
        }
    }

    public void validateName(String name) throws InvalidInputException {
        if (name.matches(".*\\d.*")) {
            throw new InvalidInputException(colorize("Name cannot contain digits.", redAttribute));
        }

        if (!name.matches("^[a-zA-Z]+(-[a-zA-Z]+)*(\\s[a-zA-Z]+(-[a-zA-Z]+)*)*$")) {
            throw new InvalidInputException(colorize("Name must contain only Latin letters, hyphens, and spaces.", redAttribute));
        }
    }

    public String formatName(String name) {
        String[] words = name.split("[\\s-]+");
        StringBuilder formattedName = new StringBuilder();
        for (String word : words) {
            if (!formattedName.isEmpty()) {
                formattedName.append(" ");
            }
            formattedName.append(word.substring(0, 1).toUpperCase())
                    .append(word.substring(1).toLowerCase());
        }
        return formattedName.toString();
    }

    public String promptString(String message) {
        System.out.print(message);
        return scanner.nextLine();
    }

    public static boolean checkPassword(String s) {
        return hasMinimumLength(s) && containsCapital(s)
                && containsSmall(s) && containsSymbol(s)
                && containsNumber(s);
    }

    private static boolean hasMinimumLength(String string) {
        return string.length() >= 8;
    }

    private static boolean containsCapital(String s) {
        return containsCharacter(s, CAPITAL_LETTERS);
    }

    private static boolean containsSmall(String s) {
        return containsCharacter(s, SMALL_LETTERS);
    }

    private static boolean containsNumber(String s) {
        return containsCharacter(s, NUMBERS);
    }

    private static boolean containsSymbol(String s) {
        return containsCharacter(s, SYMBOLS);
    }

    private static boolean containsCharacter(String s, String characters) {
        for (char c : s.toCharArray()) {
            if (characters.indexOf(c) != -1) {
                return true;
            }
        }
        return false;
    }

    public static void validatePassword(String password) throws InvalidPasswordException {
        if (!checkPassword(password)) {
            throw new InvalidPasswordException("Password does not meet the requirements.");
        }
    }

    public String generateRandomPassword() {
        Random random = new Random();
        StringBuilder password = new StringBuilder();

        password.append(CAPITAL_LETTERS.charAt(random.nextInt(CAPITAL_LETTERS.length())));
        password.append(SMALL_LETTERS.charAt(random.nextInt(SMALL_LETTERS.length())));
        password.append(NUMBERS.charAt(random.nextInt(NUMBERS.length())));
        password.append(SYMBOLS.charAt(random.nextInt(SYMBOLS.length())));

        for (int i = 4; i < 8; i++) {
            String charSet = getCharacterSet(random.nextInt(3));
            password.append(charSet.charAt(random.nextInt(charSet.length())));
        }

        char[] passwordArray = password.toString().toCharArray();
        for (int i = 0; i < passwordArray.length; i++) {
            int randomIndex = random.nextInt(passwordArray.length);
            char temp = passwordArray[i];
            passwordArray[i] = passwordArray[randomIndex];
            passwordArray[randomIndex] = temp;
        }

        return new String(passwordArray);
    }

    private static String getCharacterSet(int index) {
        return switch (index) {
            case 0 -> CAPITAL_LETTERS;
            case 1 -> SMALL_LETTERS;
            case 2 -> NUMBERS;
            default -> SYMBOLS;
        };
    }
}
