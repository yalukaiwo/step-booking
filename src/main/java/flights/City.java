package flights;

import java.util.Random;
import java.util.stream.Stream;

public enum City {
    KYIV("Kyiv"),
    DELI("Deli");

    private static final Random r = new Random();
    private final String value;

    City(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public static City getRandom() {
        return values()[r.nextInt(0, values().length)];
    }

    public static City getRandom(City exception) {
        return Stream.of(values()).filter(c -> !c.equals(exception)).toList().get(r.nextInt(0, values().length-1));
    }
}
