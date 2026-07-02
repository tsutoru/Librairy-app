package librairy.app.containe.stockMovement.reservationCancelled.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import librairy.app.containe.book.entity.Book;
import librairy.app.containe.bookCopy.entity.BookCopy;
import librairy.app.containe.customers.entity.Customer;
import librairy.app.containe.stockMovement.MovementType;
import librairy.app.containe.stockMovement.reservation.entity.ReservationMovement;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "reservation_cancelled_movement")
@Getter
@Setter
public class ReservationCancelledMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToMany
    @JoinTable(
            name = "cancelled_movement_book_copy",
            joinColumns = @JoinColumn(name = "cancelled_movement_id"),
            inverseJoinColumns = @JoinColumn(name = "book_copy_id"))
    private List<BookCopy> bookCopies;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "original_reservation_id")
    private ReservationMovement originalReservation;

    private int quantity;

    @Enumerated(EnumType.STRING)
    private MovementType type;

    private LocalDateTime movementDate;

    public ReservationCancelledMovement() {
        this.movementDate = LocalDateTime.now();
        this.type = MovementType.IN;
    }

    public ReservationCancelledMovement(Book book,
                                        ReservationMovement originalReservation,
                                        Customer customer) {
        this.book = book;
        this.originalReservation = originalReservation;
        this.bookCopies = originalReservation.getBookCopies();
        this.customer = customer;
        this.quantity = originalReservation.getQuantity();
        this.type = MovementType.IN;
        this.movementDate = LocalDateTime.now();
    }
}