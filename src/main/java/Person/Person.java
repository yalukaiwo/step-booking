package Person;

import java.io.Serializable;

public class Person implements Serializable {
    private final String name;
    private final String surname;

    public Person(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (this.getClass() != o.getClass()) return false;
        Person p = (Person) o;
        return (name.equals(p.name) && surname.equals(p.surname));
    }

    @Override
    public String toString() {
        return "Person{name: " + name + ", surname: " + surname + "}";
    }
}
