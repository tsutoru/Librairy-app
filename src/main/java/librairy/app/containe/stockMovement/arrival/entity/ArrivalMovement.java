package librairy.app.containe.stockMovement.arrival.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import librairy.app.containe.book.entity.Book;
import librairy.app.containe.bookCopy.entity.BookCopy;
import librairy.app.containe.stockMovement.entity.MovementType;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "arrival_movement")
@Getter
@Setter
public class ArrivalMovement {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @ManyToOne
  @JoinColumn(name = "book_id")
  private Book book;

  @ManyToMany
  @JoinTable(
      name = "arrival_movement_book_copy",
      joinColumns = @JoinColumn(name = "arrival_movement_id"),
      inverseJoinColumns = @JoinColumn(name = "book_copy_id"))
  private List<BookCopy> bookCopies;

  private String supplier;
  private int quantity;

  @Enumerated(EnumType.STRING)
  private MovementType type = MovementType.IN;

  private LocalDateTime movementDate;

  public ArrivalMovement() {
    this.movementDate = LocalDateTime.now();
    this.type = MovementType.IN;
  }

  public ArrivalMovement(Book book, List<BookCopy> bookCopies, String supplier) {
    this.book = book;
    this.bookCopies = bookCopies;
    this.supplier = supplier;
    this.quantity = bookCopies.size();
    this.type = MovementType.IN;
    this.movementDate = LocalDateTime.now();
  }
}
