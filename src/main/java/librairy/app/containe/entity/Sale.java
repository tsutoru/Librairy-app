package librairy.app.containe.entity;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class Sale {

    private int id;
    private LocalDate saleDate;
    private int quantity;
    private Double totalAmount;
    private Customer customer;
    private BookCopy bookCopy;

    public Sale() {}

    public Sale(int id, LocalDate saleDate, int quantity,
                Double totalAmount, Customer customer, BookCopy bookCopy) {
        this.id = id;
        this.saleDate = saleDate;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
        this.customer = customer;
        this.bookCopy = bookCopy;
    }
}