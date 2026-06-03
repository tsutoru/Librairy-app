package librairy.app.containe.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookCopy {

    private int id;
    private Book book;
    private CopyStatus status;

    public BookCopy() {}

    public BookCopy(int id, Book book, CopyStatus status) {
        this.id = id;
        this.book = book;
        this.status = status;
    }
}