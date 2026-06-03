package librairy.app.containe.entity;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class Arrival {

    private int id;
    private LocalDate arrivalDate;
    private int quantity;
    private String supplier;
    private Book book;

    public Arrival() {}

    public Arrival(int id, LocalDate arrivalDate,
                   int quantity, String supplier, Book book) {
        this.id = id;
        this.arrivalDate = arrivalDate;
        this.quantity = quantity;
        this.supplier = supplier;
        this.book = book;
    }
}