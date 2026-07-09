package librairy.app.containe.book.service;

import java.util.List;
import java.util.UUID;
import librairy.app.containe.book.entity.Book;
import librairy.app.containe.book.repository.BookRepository;
import librairy.app.containe.exception.BadRequestException;
import librairy.app.containe.exception.NotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class BookService {

  private final BookRepository bookRepository;

  public BookService(BookRepository bookRepository) {
    this.bookRepository = bookRepository;
  }

  public List<Book> getAllBooks() {
    return bookRepository.findAll();
  }

  public Book create(Book book) {
    try {
      return bookRepository.save(book);
    } catch (DataIntegrityViolationException e) {
      throw new BadRequestException("Book data is invalid: " + e.getMessage());
    }
  }

  public Book getBookById(String id) {
    try {
      UUID.fromString(id);
    } catch (IllegalArgumentException e) {
      throw new BadRequestException(
          "Invalid UUID format: " + id + ". UUID must be a valid 36-character string.");
    }

    return bookRepository
        .findById(id)
        .orElseThrow(() -> new NotFoundException("Book with id " + id + " not found"));
  }

  public Book update(String id, Book newBook) {
    try {
      UUID.fromString(id);
    } catch (IllegalArgumentException e) {
      throw new BadRequestException(
          "Invalid UUID format: " + id + ". UUID must be a valid 36-character string.");
    }

    Book book = getBookById(id);
    try {
      book.setTitle(newBook.getTitle());
      book.setDescription(newBook.getDescription());
      book.setPublicationDate(newBook.getPublicationDate());
      book.setIsbn(newBook.getIsbn());
      book.setCategory(newBook.getCategory());
      book.setAuthors(newBook.getAuthors());
      return bookRepository.save(book);
    } catch (DataIntegrityViolationException e) {
      throw new BadRequestException("Book data is invalid: " + e.getMessage());
    }
  }

  public List<Book> search(String title, String author, String category, String isbn) {
    return bookRepository.findAll().stream()
        .filter(
            book ->
                (title == null || book.getTitle().toLowerCase().contains(title.toLowerCase()))
                    && (isbn == null || book.getIsbn().toLowerCase().contains(isbn.toLowerCase())))
        .toList();
  }

  public void delete(String id) {
    try {
      UUID.fromString(id);
    } catch (IllegalArgumentException e) {
      throw new BadRequestException(
          "Invalid UUID format: " + id + ". UUID must be a valid 36-character string.");
    }

    Book book = getBookById(id);
    bookRepository.deleteById(id);
  }
}
