package librairy.app.containe.bookCopy.entity;

import jakarta.persistence.*;

import librairy.app.containe.book.entity.Book;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "book_copy")
@AllArgsConstructor
public class BookCopy {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @ManyToOne
  @JoinColumn(name = "book_id")
  private Book book;

  @Enumerated(EnumType.STRING)
  private CopyStatus status;

  @Enumerated(EnumType.STRING)
  private BookFormat format;


    private int stock;

    private double price;

  public BookCopy() {}


}
