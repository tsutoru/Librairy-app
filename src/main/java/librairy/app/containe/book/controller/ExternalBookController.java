package librairy.app.containe.book.controller;

import java.util.List;
import java.util.Optional;
import librairy.app.containe.book.dto.BookExternalDTO;
import librairy.app.containe.book.entity.Book;
import librairy.app.containe.book.service.BookService;
import librairy.app.containe.book.service.ExternalBookIntegrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books/external")
public class ExternalBookController {

  private final ExternalBookIntegrationService externalBookIntegrationService;
  private final BookService bookService;

  public ExternalBookController(
      ExternalBookIntegrationService externalBookIntegrationService, BookService bookService) {
    this.externalBookIntegrationService = externalBookIntegrationService;
    this.bookService = bookService;
  }

  @GetMapping("/isbn/{isbn}")
  public ResponseEntity<List<BookExternalDTO>> getBookByIsbn(@PathVariable String isbn) {
    List<BookExternalDTO> books = externalBookIntegrationService.getBookFromExternalSources(isbn);
    return books.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(books);
  }

  @GetMapping("/search")
  public ResponseEntity<List<BookExternalDTO>> searchBooks(@RequestParam String isbn) {
    return ResponseEntity.ok(externalBookIntegrationService.searchBooksByIsbn(isbn));
  }

  @PostMapping("/import/{isbn}")
  public ResponseEntity<Book> importBookFromExternalSource(@PathVariable String isbn) {
    return externalBookIntegrationService.getBookFromExternalSources(isbn).stream()
        .findFirst()
        .map(this::toBook)
        .map(bookService::create)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  private Book toBook(BookExternalDTO externalBook) {
    return Book.builder()
        .title(externalBook.getTitle())
        .description(externalBook.getDescription())
        .price(Optional.ofNullable(externalBook.getPrice()).orElse(0.0))
        .isbn(externalBook.getIsbn())
        .build();
  }
}
