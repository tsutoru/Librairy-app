package librairy.app.containe.stockMovement.reservation.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import librairy.app.containe.book.entity.Book;
import librairy.app.containe.bookCopy.entity.BookCopy;
import librairy.app.containe.customers.entity.Customer;
import librairy.app.containe.stockMovement.entity.MovementType;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "reservation_movement")
@Getter
@Setter
public class ReservationMovement {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @ManyToOne
  @JoinColumn(name = "book_id")
  private Book book;

  @ManyToMany
  @JoinTable(
      name = "reservation_movement_book_copy",
      joinColumns = @JoinColumn(name = "reservation_movement_id"),
      inverseJoinColumns = @JoinColumn(name = "book_copy_id"))
  private List<BookCopy> bookCopies;

  @ManyToOne
  @JoinColumn(name = "customer_id")
  private Customer customer;

  private int quantity;
  private LocalDate expirationDate;

  @Enumerated(EnumType.STRING)
  private MovementType type;

  private LocalDateTime movementDate;

  public ReservationMovement() {
    this.movementDate = LocalDateTime.now();
    this.type = MovementType.OUT;
  }

  public ReservationMovement(
      Book book, List<BookCopy> bookCopies, Customer customer, LocalDate expirationDate) {
    this.book = book;
    this.bookCopies = bookCopies;
    this.customer = customer;
    this.quantity = bookCopies.size();
    this.expirationDate = expirationDate;
    this.type = MovementType.OUT;
    this.movementDate = LocalDateTime.now();
  }
}
