package librairy.app.containe.entity;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class Reservation {

    private int id;
    private LocalDate reservationDate;
    private LocalDate expirationDate;
    private ReservationStatus status;
    private Customer customer;
    private BookCopy bookCopy;

    public Reservation() {}

    public Reservation(int id, LocalDate reservationDate, LocalDate expirationDate,
                       ReservationStatus status, Customer customer, BookCopy bookCopy) {
        this.id = id;
        this.reservationDate = reservationDate;
        this.expirationDate = expirationDate;
        this.status = status;
        this.customer = customer;
        this.bookCopy = bookCopy;
    }
}