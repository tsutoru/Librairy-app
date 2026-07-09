package librairy.app.containe.bookCopy.controller;

import java.util.List;
import java.util.Map;
import librairy.app.containe.bookCopy.entity.BookCopy;
import librairy.app.containe.bookCopy.entity.CopyStatus;
import librairy.app.containe.bookCopy.service.BookCopyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/book-copies")
public class BookCopyController {

  private final BookCopyService service;

  public BookCopyController(BookCopyService service) {
    this.service = service;
  }

  @PostMapping
  public ResponseEntity<BookCopy> create(@RequestBody BookCopy copy) {
    return ResponseEntity.status(HttpStatus.CREATED).body(service.create(copy));
  }

  @GetMapping
  public ResponseEntity<List<BookCopy>> getAll() {
    return ResponseEntity.ok(service.getAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<BookCopy> getById(@PathVariable String id) {
    return ResponseEntity.ok(service.getById(id));
  }

  @GetMapping("/available")
  public ResponseEntity<List<BookCopy>> getAvailable(
      @RequestParam(required = false) String bookId) {
    return ResponseEntity.ok(service.getAvailable(bookId));
  }

  @GetMapping("/{id}/stock")
  public ResponseEntity<Integer> getStockByCopyId(@PathVariable String id) {
    return ResponseEntity.ok(service.getStockByCopyId(id));
  }

  @PutMapping("/{id}/status")
  public ResponseEntity<BookCopy> updateStatus(
      @PathVariable String id, @RequestBody Map<String, String> body) {
    return ResponseEntity.ok(service.updateStatus(id, body.get("status")));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable String id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/book/{bookId}/status/{status}")
  public ResponseEntity<List<BookCopy>> getByStatus(
      @PathVariable String bookId, @PathVariable CopyStatus status) {
    return ResponseEntity.ok(service.getByBookAndStatus(bookId, status));
  }
}
