package librairy.app.containe.bookCopy.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import librairy.app.containe.bookCopy.entity.BookCopy;
import librairy.app.containe.bookCopy.service.BookCopyService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/book-copies")
public class BookCopyController {
  private final BookCopyService service;

  public BookCopyController(BookCopyService service) {
    this.service = service;
  }

  @PostMapping
  public BookCopy create(@RequestBody BookCopy copy) {
    return service.create(copy);
  }

  @GetMapping
  public List<BookCopy> getAll() {
    return service.getAll();
  }

  @GetMapping("/{id}")
  public BookCopy getById(@PathVariable UUID id) {
    return service.getById(id);
  }

  @GetMapping("/available")
  public List<BookCopy> getAvailable(@RequestParam(required = false) UUID bookId) {
    return service.getAvailable(bookId);
  }

  @PutMapping("/{id}/status")
  public BookCopy updateStatus(@PathVariable UUID id, @RequestBody Map<String, String> body) {

    return service.updateStatus(id, body.get("status"));
  }

  @DeleteMapping("/{id}")
  public String delete(@PathVariable UUID id) {
    service.delete(id);
    return "Exemplaire supprimé";
  }
}
