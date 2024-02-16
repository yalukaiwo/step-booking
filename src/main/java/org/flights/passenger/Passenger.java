package org.flights.passenger;

import java.io.Serializable;
import java.util.Objects;

public class Passenger implements Serializable {
    private final String name;
    private final String surname;

    public Passenger(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }
    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
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
    public int hashCode() {
        return Objects.hash(name, surname);
    }

    @Override
    public String toString() {
        return "Passenger{name: " + name + ", surname: " + surname + "}";
    }
}
