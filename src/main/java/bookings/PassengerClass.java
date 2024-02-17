package bookings;

import java.util.Random;

public enum PassengerClass {
    ECONOMY,
    PREMIUM_ECONOMY,
    BUSINESS,
    FIRST_CLASS;

    private static final Random r = new Random();

    public static PassengerClass getRandom() {
        PassengerClass[] ps = values();
        return ps[r.nextInt(0, ps.length)];
    }
}
