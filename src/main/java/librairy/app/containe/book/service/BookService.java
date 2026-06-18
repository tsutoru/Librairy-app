package librairy.app.containe.book.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import librairy.app.containe.book.entity.Book;
import librairy.app.containe.exception.BadRequestException;
import org.springframework.stereotype.Service;

@Service
public class BookService {
  private final List<Book> books = new ArrayList<>();

  public List<Book> getAllBooks() {
    return books;
  }

  public Book Create(Book book) {
    books.add(book);
    return book;
  }

  public Book getBookById(UUID id) {
    return books.stream()
            .filter(book -> book.getId().equals(id))
            .findFirst()
            .orElseThrow(()-> new BadRequestException("Book not found"));

  }

  public List<Book> getBooksByCategoryId(
      String title, String author, String category, String isbn) {
    return books.stream()
        .filter(
            book ->
                (title == null || book.getTitle().toLowerCase().contains(title.toLowerCase()))
                    && (isbn == null || book.getIsbn().toLowerCase().contains(isbn.toLowerCase())))
        .toList();
  }

  public Book update(UUID id, Book newBook) {
    Book book = getBookById(id);
    if (book != null) {
      book.setTitle(newBook.getTitle());
      book.setDescription(newBook.getDescription());
      book.setPrice(newBook.getPrice());
      book.setPublicationDate(newBook.getPublicationDate());
      book.setIsbn(newBook.getIsbn());
      book.setCategory(newBook.getCategory());
      book.setAuthors(newBook.getAuthors());
    }
    return book;
  }

  public List<Book> search(String title, String author, String category, String isbn) {
    return books.stream()
        .filter(
            book ->
                (title == null || book.getTitle().toLowerCase().contains(title.toLowerCase()))
                    && (isbn == null || book.getIsbn().toLowerCase().contains(isbn.toLowerCase())))
        .toList();
  }

  public void delete(UUID id) {
    books.removeIf(book -> book.getId().equals(id));
  }
}
