package librairy.app.containe.bookCopy.entity;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import librairy.app.containe.book.entity.Book;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "book_copy")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookCopy {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "book_id")
  @JsonBackReference
  private Book book;


  @Enumerated(EnumType.STRING)
  private CopyStatus status;

  @Enumerated(EnumType.STRING)
  private BookFormat format;

  private int stock;

  private double price;
}