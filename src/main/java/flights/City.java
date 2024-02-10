package flights;

import java.util.Random;
import java.util.stream.Stream;

public enum City {
    KYIV,
    DELI,
    PARIS,
    LONDON,
    NEW_YORK,
    TOKYO,
    BEIJING,
    MOSCOW,
    ISTANBUL,
    DUBAI,
    AMSTERDAM,
    ROME,
    MADRID,
    SYDNEY,
    LOS_ANGELES,
    BERLIN,
    SINGAPORE,
    MEXICO_CITY,
    TORONTO,
    CAIRO;

    private static final Random r = new Random();
    private final String value;

    City() {
        this.value = this.name();
    }

    @Override
    public String toString() {
        return this.value;
    }

    public static City getRandom() {
        return values()[r.nextInt(values().length)];
    }

    public static City getRandom(City exception) {
        return Stream.of(values())
                .filter(c -> !c.equals(exception))
                .toList()
                .get(r.nextInt(values().length - 1));
    }
}
