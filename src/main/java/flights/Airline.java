package flights;

import java.util.Random;

public enum Airline {
    AZAL(0.05),
    AIR_SEUL(0.07),
    INTERJET(0.08),
    JET_STAR(0.06),
    LACOMPAGINE(0.09),
    DELTA(0.04),
    OMAN_AIR(0.07),
    MANGO(0.05),
    KOREAN_AIR(0.08),
    TURKISH_AIRLINES(0.06),
    WESTJET(0.04),
    XIANS_AIRLINES(0.07),
    PEGASUS_AIRLINES(0.06);

    private static final Random r = new Random();
    private final double markup;

    Airline(double markup) {
        this.markup = markup;
    }

    public static Airline getRandom() {
        return values()[r.nextInt(0, values().length)];
    }

    public double getCost(double initialCost) {
        return initialCost * markup * 100;
    }
}
