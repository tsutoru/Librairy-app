package librairy.app.containe.book.controller;

import librairy.app.containe.book.entity.Book;
import librairy.app.containe.book.service.BookService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public Book getBookById(@PathVariable String id) {
        return bookService.getBookById(Integer.parseInt(id));
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
        return "Liste des exemplaires du livre " + id;
    }

    @PutMapping("/{id}")
    public Book updateBook(@PathVariable String id, @RequestBody Book book) {
        return bookService.update(Integer.parseInt(id), book);
    }
}
