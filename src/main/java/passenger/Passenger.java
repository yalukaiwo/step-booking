package passenger;

import java.io.Serializable;

public class Passenger implements Serializable {
    private final String name;
    private final String surname;

    public Passenger(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (this.getClass() != o.getClass()) return false;
        Passenger p = (Passenger) o;
        return (name.equals(p.name) && surname.equals(p.surname));
    }

    @Override
    public String toString() {
        return "Person{name: " + name + ", surname: " + surname + "}";
    }
}
