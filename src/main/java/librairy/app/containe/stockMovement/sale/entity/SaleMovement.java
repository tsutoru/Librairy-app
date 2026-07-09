package librairy.app.containe.stockMovement.sale.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import librairy.app.containe.book.entity.Book;
import librairy.app.containe.bookCopy.entity.BookCopy;
import librairy.app.containe.customers.entity.Customer;
import librairy.app.containe.stockMovement.entity.MovementType;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "sale_movement")
@Getter
@Setter
public class SaleMovement {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @ManyToOne
  @JoinColumn(name = "book_id")
  private Book book;

  @ManyToMany
  @JoinTable(
      name = "sale_movement_book_copy",
      joinColumns = @JoinColumn(name = "sale_movement_id"),
      inverseJoinColumns = @JoinColumn(name = "book_copy_id"))
  private List<BookCopy> bookCopies;

  @ManyToOne
  @JoinColumn(name = "customer_id")
  private Customer customer;

  private int quantity;
  private double totalAmount;

  @Enumerated(EnumType.STRING)
  private MovementType type;

  private LocalDateTime movementDate;

  public SaleMovement() {
    this.movementDate = LocalDateTime.now();
    this.type = MovementType.OUT;
  }

  public SaleMovement(Book book, List<BookCopy> bookCopies, Customer customer) {
    this.book = book;
    this.bookCopies = bookCopies;
    this.customer = customer;
    this.quantity = bookCopies.size();
    this.totalAmount = bookCopies.stream()
            .mapToDouble(BookCopy::getPrice)
            .sum();
    this.type = MovementType.OUT;
    this.movementDate = LocalDateTime.now();
  }
}
