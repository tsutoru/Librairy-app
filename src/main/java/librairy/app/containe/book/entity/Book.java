package librairy.app.containe.book.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;
import librairy.app.containe.bookCopy.entity.BookCopy;
import librairy.app.containe.entity.Author;
import librairy.app.containe.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "book")
@AllArgsConstructor
@Getter
@Setter
public class Book {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  private String title;
  private String description;
  private Double price;
  private LocalDate publicationDate;
  private String isbn;

  @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
  @JsonManagedReference
  private List<BookCopy> copies;

  @ManyToOne
  @JoinColumn(name = "category_id")
  private Category category;

  @ManyToMany
  @JoinTable(
      name = "book_author",
      joinColumns = @JoinColumn(name = "book_id"),
      inverseJoinColumns = @JoinColumn(name = "author_id"))
  private List<Author> authors;

  public Book() {}
}
