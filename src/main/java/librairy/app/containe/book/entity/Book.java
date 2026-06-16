package librairy.app.containe.book.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

import librairy.app.containe.Author.entity.Author;
import librairy.app.containe.category.entity.Category;
import lombok.*;

@Entity
@Table(name = "book")
@AllArgsConstructor
@NoArgsConstructor
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

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToMany
    @JoinTable(
            name = "book_author",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private List<Author> authors;

}
