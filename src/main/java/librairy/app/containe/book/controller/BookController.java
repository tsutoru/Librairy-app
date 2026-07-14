package librairy.app.containe.book.controller;

import java.net.URI;
import java.util.List;
import librairy.app.containe.book.entity.Book;
import librairy.app.containe.book.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/books")
public class BookController {

  private final BookService bookService;

  public BookController(BookService service) {
    this.bookService = service;
  }

  @PostMapping
  public ResponseEntity<Book> createBook(@RequestBody Book book) {
    Book createdBook = bookService.create(book);
    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(createdBook.getId())
            .toUri();
    return ResponseEntity.created(location).body(createdBook);
  }

  @GetMapping
  public List<Book> getAllBooks() {
    return bookService.getAllBooks();
  }

  @GetMapping("/{id}")
  public Book getBookById(@PathVariable String id) {
    return bookService.getBookById(id);
  }

  @GetMapping("/search")
  public List<Book> searchBook(
      @RequestParam(required = false) String title,
      @RequestParam(required = false) String author,
      @RequestParam(required = false) String category,
      @RequestParam(required = false) String isbn) {
    return bookService.search(title, author, category, isbn);
  }

  @GetMapping("/{id}/copies")
  public String getCopiesByBookId(@PathVariable String id) {
    return "List of copies for book " + id;
  }

  @PutMapping("/{id}")
  public Book updateBook(@PathVariable String id, @RequestBody Book book) {
    return bookService.update(id, book);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteBook(@PathVariable String id) {
    bookService.delete(id);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/import-isbn/{isbn}")
  public ResponseEntity<Book> importBookByIsbn(@PathVariable String isbn) {
    Book createdBook = bookService.createBookFromIsbn(isbn);
    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(createdBook.getId())
            .toUri();
    return ResponseEntity.created(location).body(createdBook);
  }
}
