package librairy.app.containe.book.controller;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.Id;
import librairy.app.containe.book.entity.Book;
import librairy.app.containe.book.service.BookService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
public class BookController {

  private final BookService bookService;

  public BookController(BookService service) {
    this.bookService = service;
  }

  @PostMapping
  public Book createBook(@RequestBody Book book) {
    return bookService.Create(book);
  }

  @GetMapping
  public List<Book> getAllBooks() {
    return bookService.getAllBooks();
  }

  @GetMapping("/{id}")
  public Book getBookById(@PathVariable UUID id) {

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
  public String getCopiesByBookId(@PathVariable UUID id) {
    return "Liste des exemplaires du livre " + id;
  }

  @PutMapping("/{id}")
  public Book updateBook(@PathVariable UUID id, @RequestBody Book book) {
    return bookService.update(id, book);
  }
}
