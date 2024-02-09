import Bookings.Booking;
import Bookings.BookingsDAO;
import Flights.Flight;
import Flights.FlightsDAO;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException {
        File bf = new File("bookings.bin");
        File ff = new File("flights.bin");

        BookingsDAO b = new BookingsDAO(bf);
        FlightsDAO f = new FlightsDAO(ff);

        Flight fl = new Flight("Deli", 123, 3);

        f.save(fl);

        System.out.println(new Booking(fl, new ArrayList<>()));
        Booking bk = new Booking(new Flight( "Deli", 123, 421), new ArrayList<>());
        Booking bd = new Booking(new Flight( "Deli", 123, 421), new ArrayList<>());
        System.out.println(new Flight( "Deli", 123, 421));
        b.save(bk);
        System.out.println(b.readAll());
        System.out.println(b.read("123"));
        b.save(bd);
        System.out.println(b.readAll());
        System.out.println(b.read("123"));
        b.delete("1243");
        b.delete("123");
        System.out.println(b.readAll());
        System.out.println(f.readAll());
        System.out.println("Hello");
    }
}
