package librairy.app.containe.book.service;

import java.util.List;
import java.util.UUID;
import librairy.app.containe.book.dto.BookExternalDTO;
import librairy.app.containe.book.entity.Book;
import librairy.app.containe.book.repository.BookRepository;
import librairy.app.containe.exception.BadRequestException;
import librairy.app.containe.exception.NotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class BookService {

  private final BookRepository bookRepository;
  private final ExternalBookIntegrationService externalBookIntegrationService;

  public BookService(
      BookRepository bookRepository,
      ExternalBookIntegrationService externalBookIntegrationService) {
    this.bookRepository = bookRepository;
    this.externalBookIntegrationService = externalBookIntegrationService;
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
    validateUuid(id);
    Book book = getBookById(id);

    book.setTitle(newBook.getTitle());
    book.setDescription(newBook.getDescription());
    book.setPrice(newBook.getPrice());
    book.setPublicationDate(newBook.getPublicationDate());
    book.setIsbn(newBook.getIsbn());
    book.setCategory(newBook.getCategory());
    book.setAuthors(newBook.getAuthors());

    return save(book);
  }

  public List<Book> search(String title, String author, String category, String isbn) {
    return bookRepository.findAll().stream()
        .filter(book -> matches(book.getTitle(), title) && matches(book.getIsbn(), isbn))
        .toList();
  }

  public void delete(String id) {
    validateUuid(id);
    getBookById(id);
    bookRepository.deleteById(id);
  }

  public Book createBookFromIsbn(String isbn) {
    BookExternalDTO externalBook =
        externalBookIntegrationService.getBookFromExternalSources(isbn).stream()
            .findFirst()
            .orElseThrow(() -> new NotFoundException("Aucun livre trouvé pour l'ISBN: " + isbn));

    return save(toBook(externalBook));
  }

  public List<BookExternalDTO> searchExternalBooksByIsbn(String isbn) {
    return externalBookIntegrationService.searchBooksByIsbn(isbn);
  }

  private Book toBook(BookExternalDTO externalBook) {
    return Book.builder()
        .title(externalBook.getTitle())
        .description(externalBook.getDescription())
        .price(externalBook.getPrice() != null ? externalBook.getPrice() : 0.0)
        .isbn(externalBook.getIsbn())
        .build();
  }

  private Book save(Book book) {
    try {
      return bookRepository.save(book);
    } catch (DataIntegrityViolationException e) {
      throw new BadRequestException("Book data is invalid: " + e.getMessage());
    }
  }

  private void validateUuid(String id) {
    try {
      UUID.fromString(id);
    } catch (IllegalArgumentException e) {
      throw new BadRequestException(
          "Invalid UUID format: " + id + ". UUID must be a valid 36-character string.");
    }
  }

  private boolean matches(String value, String filter) {
    return filter == null || value.toLowerCase().contains(filter.toLowerCase());
  }
}
