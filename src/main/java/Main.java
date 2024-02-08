import Bookings.Booking;
import Bookings.BookingsDAO;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        File f = new File("bookings.bin");

        BookingsDAO b = new BookingsDAO(f);

        System.out.println(b.read("Some id"));
        Booking bk = new Booking("123");
        Booking bd = new Booking("123");
        b.save(bk);
        System.out.println(b.readAll());
        System.out.println(b.read("123"));
        b.save(bd);
        System.out.println(b.readAll());
        System.out.println(b.read("123"));
        b.delete("1243");
        b.delete("123");
        System.out.println(b.readAll());
    }
}
