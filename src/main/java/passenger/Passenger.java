package passenger;

import java.io.Serializable;

public record Passenger(String name, String surname) implements Serializable {

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
        return "Passenger{name: " + name + ", surname: " + surname + "}";
    }
}
