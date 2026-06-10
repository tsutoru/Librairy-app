package librairy.app.containe.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import librairy.app.containe.bookCopy.entity.BookCopy;
import librairy.app.containe.customers.entity.Customer;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "sale")
@Getter
@Setter
public class Sale {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private LocalDate saleDate;
  private int quantity;
  private Double totalAmount;

  @ManyToOne
  @JoinColumn(name = "customer_id")
  private Customer customer;

  @ManyToOne
  @JoinColumn(name = "book_copy_id")
  private BookCopy bookCopy;

  public Sale() {}

  public Sale(
      int id,
      LocalDate saleDate,
      int quantity,
      Double totalAmount,
      Customer customer,
      BookCopy bookCopy) {
    this.id = id;
    this.saleDate = saleDate;
    this.quantity = quantity;
    this.totalAmount = totalAmount;
    this.customer = customer;
    this.bookCopy = bookCopy;
  }
}
